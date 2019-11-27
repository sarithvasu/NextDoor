package com.food.nextdoor.activity.buyer


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.food.nextdoor.activity.MainActivity
import com.food.nextdoor.database.DbOperation
import com.food.nextdoor.database.NextDoorDB
import com.food.nextdoor.model.*
import com.food.nextdoor.model.post.Buyer
import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import system.*

import java.io.File
import android.util.Log
import com.dropbox.core.android.Auth
import com.food.nextdoor.R
import com.food.nextdoor.dropbox.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.seller_dish_list_row.view.*
import org.json.JSONObject
import system.Utility.Companion.DB_ACCESS_TOKEN


class RegistrationActivity : AppCompatActivity() {

    companion object {
        private const val READ_IMAGE: Int = 253
    }


    private lateinit var launcherApartment: LauncherApartment
    private lateinit var phoneNumber: String
    private  var fromEdit: String?=null
    private  var userId:Int=-1
    private var filepath=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)
    /*    if(tokenExists())
        Auth.startOAuth2Authentication(getApplicationContext(), getString(R.string.APP_KEY))
        else*/


        var values: String = ""
        val intent = intent
        intent.getStringExtra("values")?.let {
            values = it
        }

        launcherApartment = intent.getParcelableExtra("apartment")
        phoneNumber = intent.getStringExtra("phone")
        fromEdit=intent.getStringExtra("from_edit")
        if(fromEdit!=null){
            setControl()
        }
        et_customer_mobile_num_reg.setText(phoneNumber)
        et_customer_mobile_num_reg.isEnabled = false

        tv_verify_phone_num_reg.setOnClickListener {
            val v = Validation()
            if (v.isValidPhone(et_customer_mobile_num_reg.text.toString())) {
                var intent = Intent(this, OTPActivity::class.java)
                intent.putExtra("phonenumber", et_customer_mobile_num_reg.text.toString().trim())
                startActivity(intent)
            } else {
                Utility.createCustomDialog(this, "invalid mobile number",true)
            }
        }
        if (intent.hasExtra(Utility.ACTIVITY_NAME)) {
            val activityName = intent.getStringExtra(Utility.ACTIVITY_NAME);
            if (activityName == "From CheckoutActivity") {
                // ToDO
                setControl()

            }
        }


        /* Set click listener for profile image */
        img_customer_image_reg.setOnClickListener {
            onclickProfileImage()
        }


        /* Set click listener for register button */
        tv_reg.setOnClickListener {
            onClickRegistrationButton()
        }

    }







    private fun setControl() {
        val userId = Preference(this).getUserId()
        val buyerInfo = NextDoorDB.invoke(this).daoAccess.getBuyerInfo(userId)

        et_full_name_of_customer_reg.setText(buyerInfo.fullName)
        et_full_name_of_customer_reg.isEnabled=false
        et_flat_no_of_customer_reg.setText("")
        et_flat_no_of_customer_reg.requestFocus()
        et_customer_mobile_num_reg.setText(buyerInfo.mobileNumber)
        et_customer_mobile_num_reg.isEnabled=false
        et_customer_email_reg.setText(buyerInfo.email)
        et_customer_email_reg.isEnabled=false
        PicassoClient.init(this, DropboxClientFactory.init(Utility.DB_ACCESS_TOKEN)!!);
        PicassoClient.picasso?.load(ImagePiccassoRequestHadler.buildPicassoUri(Utility.SLASH +buyerInfo.profileImageUrl))!!
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(img_customer_image_reg)

        setRadioGroup(buyerInfo.gender)

        tv_reg.text = "UPDATE ADDRESS"
    }

    private fun setRadioGroup(gender: String?) {
        when (gender) {
            Gender.MALE.value -> rb_gender_male_reg.isChecked = true
            Gender.FEMALE.value -> rb_gender_female_reg.isChecked = true
            Gender.OTHERS.value -> rb_gender_other_reg.isChecked = true
        }
            rg_gender_reg.isEnabled=false
        for (i in 0 until rg_gender_reg.getChildCount()) {
            (rg_gender_reg.getChildAt(i) as RadioButton).setEnabled(false)
        }

    }


    private fun onclickProfileImage() {
      this.loadImage()
    }


    private fun loadImage() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this);

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_IMAGE -> {
/*

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(applicationContext, "Cannot access your images", Toast.LENGTH_LONG).show()
                }
*/

                if (grantResults.size > 0) {
                    val cameraPermission = grantResults[1] === PackageManager.PERMISSION_GRANTED
                    val readExternalFile = grantResults[0] === PackageManager.PERMISSION_GRANTED

                    if (cameraPermission && readExternalFile) {

                    } else {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Please Grant Permissions to upload profile photo",
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction("ENABLE",
                            object : View.OnClickListener {
                                override fun onClick(v: View) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(
                                            arrayOf(
                                                Manifest.permission
                                                    .READ_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA
                                            ),
                                            READ_IMAGE

                                        )
                                    }
                                }
                            }).show()
                    }
                }
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    override fun onActivityResult(requstCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requstCode, resultCode, data)

        if (requstCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri

                img_customer_image_reg.setImageURI(resultUri)


                val file = File( resultUri.path)
                filepath=file.name
                if (file != null) {
                    //Initialize UploadTask
                    UploadTask(
                        DropboxClient.getClient(DB_ACCESS_TOKEN!!),
                        file,
                        applicationContext
                    ).execute()
                }
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
    private fun onClickRegistrationButton() {
        if(fromEdit!=null){
            val adressUpdateputService: RetrofitService =
                RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
            adressUpdateputService.postUpdateBuyerAddress(
                "application/json",
                "application/erjson",Preference(this).getUserId(),launcherApartment.ApartmentId,et_flat_no_of_customer_reg.text.toString()
            ).also { it.enqueue(object:Callback<ResponseCode>{
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {

                  //  val  jObjError = JSONObject(response.errorBody()?.string())
                        if(response.errorBody()!=null){
                            val  jObjError = (JSONObject(response.errorBody()?.string())).getString("Message")
                            Utility.createCustomDialog(
                                this@RegistrationActivity,
                                jObjError,true);
                        }else {
                            Utility.createCustomDialog(
                                this@RegistrationActivity,
                                response.body()?.Message.toString(),
                                View.OnClickListener {
                                    if( response.body()?.Message.toString().trim().equals("Address updated successfully".trim())){
                                    NextDoorDB.invoke(this@RegistrationActivity)
                                        .daoAccess.updateBuyerAddress(
                                        Preference(this@RegistrationActivity).getUserId(),
                                        et_flat_no_of_customer_reg.text.toString(),
                                        launcherApartment.ApartmentId,
                                        launcherApartment.ApartmentName,
                                        launcherApartment.PinCode
                                    )
                                }
                                    val intent1 =
                                        Intent(this@RegistrationActivity, HomeActivity::class.java)
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent1)
                                    finish()
                                })

                        }
                }

            }) }
        }else {


            try {
                if (validationDone()) {
                    val gson:Gson= Gson()
                    val stringJson=gson.toJson(creatBuyerInfo())

                    savePackingDeliveryPaymentModeReviewTagInDbAsync()


                    // Soumen Temp call
                    readInfoForCheckingNotNeeded()
                    val buyerInfoPostService: RetrofitService =
                        RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
                    buyerInfoPostService.postUserDeatails(
                        "application/json",
                        "application/json",
                        creatBuyerInfo()
                    ).also {
                        it.enqueue(object : Callback<ResponseCode> {
                            override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                                val res = t.localizedMessage
                                /* val s="sarith vasu"
                             startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))*/
                                finish()
                            }

                            override fun onResponse(
                                call: Call<ResponseCode>,
                                response: Response<ResponseCode>
                            ) {

                                startActivity(
                                    Intent(
                                        this@RegistrationActivity,
                                        MainActivity::class.java
                                    )
                                )

                                val res = response.body()

                                val buyerInfo = setProperty(res?.Id)
                                saveBuyerInfoInDbAndPreferenceAsync(buyerInfo)

                                val s = "sarith vasu"
                            }

                        })
                    }

                }
            } catch (e: Exception) {
                Utility.createCustomDialog(this, e.localizedMessage,true)

            }
        }
    }

    private fun tokenExists(): Boolean {

        val preference = Preference(this)

        val accessToken = preference.getAuthToken()
        return accessToken.equals("")
    }

    private fun retrieveAccessToken(): String? {
        //check if ACCESS_TOKEN is stored on previous app launches
        val preference = Preference(this)

        val accessToken = preference.getAuthToken()
        if (accessToken.equals("")) {
            Log.d("AccessToken Status", "No token found")
            return ""
        } else {
            //accessToken already exists
            Log.d("AccessToken Status", "Token exists")
            return accessToken
        }
    }

    private fun creatBuyerInfo(): Buyer {
        return Buyer(
            ApartmentId = launcherApartment.ApartmentId,
            Email = et_customer_email_reg.text.toString(),
            FlatNumber = et_flat_no_of_customer_reg.text.toString(),
            FullName = et_full_name_of_customer_reg.text.toString(),
            Gender = (findViewById(rg_gender_reg.checkedRadioButtonId) as RadioButton).text.toString(),
            IsEmailVerified = false,
            IsMobileNumberVerified = true,
            ProfileImageUrl = filepath,
            UserTypeId = 1, MobileNumber ="91"+ et_customer_mobile_num_reg.text.toString()

        )

    }

    private fun validationDone(): Boolean {
        var msg: String = ""
        val v = Validation()
        if (!v.isValidFullName(et_full_name_of_customer_reg.text.toString())) msg = "invalid name"
        else if (!v.isValidAddress(et_flat_no_of_customer_reg.text.toString())) msg =
            "invalid flat No."
        else if (!v.isValidPhone(et_customer_mobile_num_reg.text.toString())) msg =
            "invalid mobile number"
        else if (!v.isValidEmail(et_customer_email_reg.text.toString())) msg = "invalid email"
        else if (rg_gender_reg.checkedRadioButtonId == -1) msg = "Please select a gender"
        if (msg != "") {
            Utility.createCustomDialog(this, msg,true )
            return false
        }
        return true
    }


    private fun setProperty(id: Int?): BuyerInfo {


        val buyerInfo = BuyerInfo()

        buyerInfo.userId =id!! // Its -1 by By default
        buyerInfo.userTypeId = UserType.BUYER.value
        buyerInfo.apartmentId = launcherApartment.ApartmentId
        buyerInfo.fullName =et_full_name_of_customer_reg.text.toString()
        buyerInfo.flatNumber =  et_flat_no_of_customer_reg.text.toString()
        buyerInfo.mobileNumber ="91"+ et_customer_mobile_num_reg.text.toString()
        buyerInfo.email = et_customer_email_reg.text.toString()
        buyerInfo.gender =(findViewById(rg_gender_reg.checkedRadioButtonId) as RadioButton).text.toString()
        buyerInfo.profileImageUrl =filepath
        buyerInfo.isActive = true
        buyerInfo.dateInsertion = "14-06-2019 | 02.56 PM"
        buyerInfo.dateRevision = null
        buyerInfo.apparmentName=launcherApartment.ApartmentName
        buyerInfo.pinCode=launcherApartment.PinCode

        /* Return */
        return buyerInfo
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

        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonBuyerInfo: String = gson.toJson(buyerInfo)
        println(jsonBuyerInfo)
    }

    private fun savePackingDeliveryPaymentModeReviewTagInDbAsync() {
        // Create feed Object for Packing delivery, Payment mode and review
        val pDPRFeed = this.readFromAsset("packing_delivery_payment_review_feed.json")
        pDPRFeed.let {
            val packingTypes: List<PackingType> = pDPRFeed.packingTypes
            packingTypes.forEach {
                val execute = DbOperation.InsertPackingType(this).execute(it)
            }


            val deliveryTypes: List<DeliveryType> = pDPRFeed.deliveryTypes
            deliveryTypes.forEach {
                val execute = DbOperation.InsertDeliveryType(this).execute(it)
            }


            val paymentModes: List<PaymentMode> = pDPRFeed.paymentModes
            paymentModes.forEach {
                val execute = DbOperation.InsertPaymentMode(this).execute(it)
            }


            val reviewTags: List<ReviewTag> = pDPRFeed.reviewTags
            reviewTags.forEach {
                val execute = DbOperation.InsertReviewTag(this).execute(it)
            }
        }
    }

    private fun readFromAsset(fileName: String): PackingDispatchPaymentReviewFeed {
        val jsonString = application.assets.open(fileName).bufferedReader().use {
            it.readText()
        }

        val gson = GsonBuilder().serializeNulls().create()
        val feed = gson.fromJson(jsonString, PackingDispatchPaymentReviewFeed::class.java)

        return feed
    }


    private fun readInfoForCheckingNotNeeded() = Thread {
        val userId = Preference(this).getUserId()
        val buyerInfo = NextDoorDB.invoke(this).daoAccess.getBuyerInfo(userId)

        val packing = NextDoorDB.invoke(this).daoAccess.getPackingTypeList()
        val packingId =
            NextDoorDB.invoke(this).daoAccess.getPackingTypeIdByDescription("Get your own box")
        val packingDesc = NextDoorDB.invoke(this).daoAccess.getPackingDescriptionById(1)

        val deliver = NextDoorDB.invoke(this).daoAccess.getDeliveryTypeList()
        val deliveryId =
            NextDoorDB.invoke(this).daoAccess.getDeliveryTypeIdByDescription("Home delivery")
        val deliveryDesc = NextDoorDB.invoke(this).daoAccess.getDeliveryDescriptionById(1)

        val paymentModeId = NextDoorDB.invoke(this).daoAccess.getPaymentModeIdByDescription("COD")

        val review = NextDoorDB.invoke(this).daoAccess.getReviewTagList()

    }.start()




}
