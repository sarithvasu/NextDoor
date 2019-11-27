package com.food.nextdoor.activity.seller

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.food.nextdoor.R
import com.food.nextdoor.database.NextDoorDB
import com.food.nextdoor.model.BuyerInfo
import com.food.nextdoor.model.post.*
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.gson.Gson
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.seller_registration.*
import kotlinx.android.synthetic.main.seller_upload.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import system.*
import java.io.File

class SellerRegistration : AppCompatActivity() {

    private lateinit var mUserInfo: HashMap<String, Int>
    private var mUserId: Int = -1
    private var mUserTypeId: Int? = 0
    private lateinit var buyerInfo: BuyerInfo
    private var isIdProof = false
    private var isAddressProof = false
    private var idProofFileName = ""
    private var addressProofFileName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_registration)
        mUserInfo = Preference(this).getUserBasicInfo()
        mUserId = mUserInfo[Utility.USER_ID]!!
        mUserTypeId = mUserInfo.get(Utility.USER_TYPE_ID)
        buyerInfo = NextDoorDB.invoke(this).daoAccess.getBuyerInfo(mUserId)
        tv_user_name_seller_registration.text = "Hi ${buyerInfo.fullName}"
        tv_reg_slr.setOnClickListener {
            registerPost()
        }
        tv_upload_id_proof_slr_registration.setOnClickListener {
            isIdProof = true
            this.loadImage()
        }
        tv_upload_address_proof_slr_registration.setOnClickListener {
            this.loadImage()
            isAddressProof = true
        }

    }

    private fun loadImage() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this);
    }

    private fun registerPost() {
        if (validationDone()) {
            postSellerRegistration()
        }
    }


    private fun postSellerRegistration() {



        val sellerinfo= creatPostSellerInfo()
        val json=Gson().toJson(sellerinfo)
        val sellerInfoPostService: RetrofitService =
            RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
        sellerInfoPostService.postSellerrDeatails(
            "application/JSON",
            "application/JSON",
            creatPostSellerInfo()
        ).also {
            it.enqueue(object : Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                    val res = t.localizedMessage
                    val s = "sarith vasu"
                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                    if (response.isSuccessful) {
                        val preference = Preference(this@SellerRegistration)
                        preference.saveUserChefId(response.body()!!.Id)
                        Utility.createCustomDialog(
                            this@SellerRegistration,
                            "Documents submitted successfully, the status will be updated in next 1 to 2 days.",
                            View.OnClickListener {
                                finish()
                            })

                        val res = response

                        val s = "sarith vasu"
                    }
                }

            })
        }

    }

    private fun creatPostSellerInfo(): PostSeller {

        val documentProof = DocumentProof(
            FullName = buyerInfo.fullName,
            AddressProofUrl = addressProofFileName,
            IdProofUrl = idProofFileName,
            ApartmentId = buyerInfo.apartmentId,
            FlatNumber = buyerInfo.flatNumber.toString()
        )
        val chef = Chef(
            AboutChef = tv_about_seller_slr_registration.text.toString(),
            SpecializedOption = getSpecilization(),
            UserId = mUserId
        )
        return PostSeller(documentProof = documentProof, chef = chef)
    }

    private fun getSpecilization(): Int {
        rb_speciality_veg_slr_registration.isChecked
        when ((findViewById(radioGroup.checkedRadioButtonId) as RadioButton).text.toString()) {
            "Veg" -> return SpecilizeOption.VEG.value
            "Non-Veg" -> return SpecilizeOption.NON_VEG.value
            "Both" -> return SpecilizeOption.BOTH.value
            else -> {
                return 0
            }
        }
    }


    private fun validationDone(): Boolean {
        var msg: String = ""
        val v = Validation()
        if (!v.isValid250Character(tv_about_seller_slr_registration.text.toString())) msg =
            "please fill about chef field."
        else if (radioGroup.checkedRadioButtonId == -1) msg = "Please select a speciality"
        else if(addressProofFileName.equals("")||idProofFileName.equals("")) msg="upload your complete document"
        if (msg != "") {
            Utility.createCustomDialog(this, msg, true)
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
                val file = File(resultUri.path)
                if (isAddressProof) {
                    addressProofFileName = file.name
                    isAddressProof = false
                    textView118.text = "You uploaded the file $addressProofFileName"
                    textView118.setTextColor(Color.RED)
                }
                if (isIdProof) {
                    idProofFileName = file.name
                    isIdProof = false
                    textView108.text = "You uploaded the file $idProofFileName"
                    textView108.setTextColor(Color.RED)

                }


            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}
