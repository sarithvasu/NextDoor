package com.food.nextdoor.activity.buyer

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ListView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.food.nextdoor.R
import com.food.nextdoor.activity.LaunchActivity
import com.food.nextdoor.activity.MainActivity
import com.food.nextdoor.database.DbOperation
import com.food.nextdoor.model.BuyerInfo
import com.food.nextdoor.model.UserInfo
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.otp.*
import kotlinx.android.synthetic.main.registration.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import system.Manager
import system.Preference
import system.UserType
import system.Utility
import java.util.concurrent.TimeUnit


class OTPActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var listView: ListView
    private lateinit var phonenumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp)
        mAuth = FirebaseAuth.getInstance()
        phonenumber = intent.getStringExtra("phonenumber")





        textView114.text = "+91 - $phonenumber"
        editText4.addTextChangedListener(GenericTextWatcher(editText4))
        editText5.addTextChangedListener(GenericTextWatcher(editText5))
        editText8.addTextChangedListener(GenericTextWatcher(editText8))
        editText7.addTextChangedListener(GenericTextWatcher(editText7))
        editText6.addTextChangedListener(GenericTextWatcher(editText6))
        editText3.addTextChangedListener(GenericTextWatcher(editText3))
        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                tv_timer.setText("seconds remaining: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                tv_resend_code_otp.visibility = View.VISIBLE
                textView133.visibility = View.VISIBLE
                tv_timer.setText("")
            }
        }.start()

        tv_resend_code_otp.setOnClickListener {
            sendVerificationCode("+91$phonenumber")
            tv_resend_code_otp.visibility = View.GONE
            object : CountDownTimer(60000, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    tv_timer.setText("seconds remaining: " + millisUntilFinished / 1000)
                }

                override fun onFinish() {
                    tv_timer.setText("")
                    tv_resend_code_otp.visibility = View.VISIBLE
                    textView133.visibility = View.VISIBLE
                }
            }.start()
        }


        //unComment in prodution

        checkUserInDb()

  /*      PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$phonenumber",
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallBack
        )*/
        tv_verify_otp.setOnClickListener {
            val code1 = editText4.getText().toString().trim()
            val code2 = editText5.getText().toString().trim()
            val code3 = editText8.getText().toString().trim()
            val code4 = editText7.getText().toString().trim()
            val code5 = editText6.getText().toString().trim()
            val code6 = editText3.getText().toString().trim()
            val code = "$code1$code2$code3$code4$code5$code6"
            if (code.isEmpty() || code.length < 6) {
                Toast.makeText(this, "ERROR CODE", Toast.LENGTH_SHORT).show();
            } else {
                //change when production
               verifyCode(code)
                //verifyCode("123456")
            }
        }


    }


    private fun sendVerificationCode(number: String) {


    }

    private var verificationid: String? = ""
    private val mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(
            s: String?,
            forceResendingToken: PhoneAuthProvider.ForceResendingToken?
        ) {
            super.onCodeSent(s, forceResendingToken)

            verificationid = s

        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode
            if (code != null) {

                verifyCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Utility.createDialog(this@OTPActivity, "ERROR", e.message!!)

        }
    }

    private fun verifyCode(code: String) {
        try {
            editText4.setText(code.get(0).toString())
            editText5.setText(code.get(1).toString())
            editText8.setText(code.get(2).toString())
            editText7.setText(code.get(3).toString())
            editText6.setText(code.get(4).toString())
            editText3.setText(code.get(5).toString())
            val credential = PhoneAuthProvider.getCredential(verificationid!!, code)
            signInWithCredential(credential)
        } catch (e: Exception) {
            Utility.createCustomDialog(this@OTPActivity,  e.localizedMessage!!,true)
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    checkUserInDb()




                } else {
                    Toast.makeText(this@OTPActivity, task.exception!!.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun checkUserInDb() {
        val checkService: RetrofitService? =
            RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        checkService?.getUserDetailByMobileNumber(
            "application/json",
            "application/json", "91$phonenumber"
        ).also {
            it?.enqueue(object : Callback<UserInfo> {
                override fun onFailure(call: Call<UserInfo>, t: Throwable) {

                }

                override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                    if (response.code() == 404) {
                        try {
                            val s = response.errorBody()
                            val jObjError: JSONObject = JSONObject(response.errorBody()?.string());
                            val msg = jObjError.getString("Message");
                            if (msg.isNotEmpty()) {
                                Handler().postDelayed({
                                    val intent1 =
                                        Intent(this@OTPActivity, LaunchActivity::class.java)

                                    intent1.putExtra("phone", phonenumber);
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                                    startActivity(intent1)
                                    finish()
                                }, 1000)
                            }
                        }catch (e:JSONException){

                        }
                    }
                    else if (response.code()==200){
                        val userInfo=response.body()
                        val buyerInfo=setProperty(userInfo)
                        saveBuyerInfoInDbAndPreferenceAsync(buyerInfo)
                        val intent1 =
                            Intent(this@OTPActivity, MainActivity::class.java)

                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                        startActivity(intent1)
                        finish()
                    }
                }


            })
        }
    }
    private fun saveBuyerInfoInDbAndPreferenceAsync(buyerInfo: BuyerInfo) {
        val execute = DbOperation.InsertBuyerInfo(this).execute(buyerInfo)
        val basicInfo: HashMap<String, Int> = hashMapOf()
        basicInfo.put(Utility.USER_ID, buyerInfo.userId)
        basicInfo.put(Utility.USER_TYPE_ID, buyerInfo.userTypeId)

        val preference = Preference(this)
        preference.saveUserBasicInfo(basicInfo)

        //Save buyer Info in Manager object
        Manager.buyerInfo = buyerInfo


    }
    internal inner class GenericTextWatcher(val view: View) :
        TextWatcher {

        override fun afterTextChanged(editable: Editable) {
            // TODO Auto-generated method stub
            val text = editable.toString()
            when (view.id) {

                R.id.editText4 -> if (text.length == 1)
                    editText5.requestFocus()
                R.id.editText5 -> if (text.length == 1)
                    editText8.requestFocus()
                R.id.editText8 -> if (text.length == 1)
                    editText7.requestFocus()
                R.id.editText7 -> if (text.length == 1)
                    editText6.requestFocus()
                R.id.editText6 -> if (text.length == 1)
                    editText3.requestFocus()
                R.id.editText3 -> if (text.length == 0)
                    editText3.clearFocus()
            }
        }

        override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
            // TODO Auto-generated method stub
        }

        override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
            // TODO Auto-generated method stub
        }
    }
    private fun setProperty(userInfo: UserInfo?): BuyerInfo {


        val buyerInfo = BuyerInfo()

        buyerInfo.userId = userInfo?.User?.UserId!!// Its -1 by By default
        buyerInfo.userTypeId = userInfo?.User.UserTypeId
        buyerInfo.apartmentId = userInfo?.Apartment.ApartmentId
        buyerInfo.fullName =userInfo?.User.FullName
        buyerInfo.flatNumber =  userInfo?.User.FlatNumber
        buyerInfo.mobileNumber = userInfo?.User.MobileNumber
        buyerInfo.email = userInfo?.User.Email
        buyerInfo.gender =userInfo?.User.Gender
        buyerInfo.profileImageUrl =userInfo?.User.ProfileImageUrl
        buyerInfo.isActive = true
        buyerInfo.pinCode= userInfo.Apartment.PinCode
        buyerInfo.apparmentName=userInfo.Apartment.ApartmentName


        /* Return */
        return buyerInfo
    }

}
