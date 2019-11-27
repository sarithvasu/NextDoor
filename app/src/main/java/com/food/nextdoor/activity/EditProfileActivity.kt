package com.food.nextdoor.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.food.nextdoor.R
import com.food.nextdoor.database.NextDoorDB
import com.food.nextdoor.dropbox.*
import com.food.nextdoor.model.BuyerInfo
import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.model.post.UpdateUser
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.gson.Gson
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.edit_profile.*
import kotlinx.android.synthetic.main.registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import system.Gender
import system.Preference
import system.Utility
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    private var filepath: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)
        val userId = Preference(this).getUserId()
        val buyerInfo = NextDoorDB.invoke(this).daoAccess.getBuyerInfo(userId)
        tv_edit_full_name_edit_profile.setText(buyerInfo.fullName.toString())
        tv_edit_email_edit_profile.setText(buyerInfo.email.toString())
        tv_mobile_number_edit_profile.setText(buyerInfo.mobileNumber.toString())
        tv_mobile_number_edit_profile.isEnabled=false
        tv_address_edit_profile.setText(buyerInfo.flatNumber.toString()+", "+buyerInfo.apparmentName.toString()+", "+buyerInfo.pinCode.toString())
        tv_edit_flat_number_edit_profile.setText(buyerInfo.flatNumber.toString())
        PicassoClient.init(this, DropboxClientFactory.init(Utility.DB_ACCESS_TOKEN)!!);
        PicassoClient.picasso?.load(ImagePiccassoRequestHadler.buildPicassoUri(Utility.SLASH +buyerInfo.profileImageUrl))!!
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(img_profile_pic_edit_profile)
        tv_upload_profile_pic_edit_profile.setOnClickListener {
            onclickProfileImage()
        }
        filepath=buyerInfo.profileImageUrl!!
        setRadioGroup(buyerInfo.gender)
        tv_save_changes_edit_profile.setOnClickListener {
           updateUser(buyerInfo)
        }
        tv_change_address_edit_profile.setOnClickListener {
            updateAdress()
        }
    }

    private fun setRadioGroup(gender: String?) {
        when (gender) {
            Gender.MALE.value -> rb_gender_male_edit_profile.isChecked = true
            Gender.FEMALE.value -> rb_gender_female_edit_profile.isChecked = true
            Gender.OTHERS.value -> rb_gender_other_edit_profile.isChecked = true
        }

    }

    private fun updateAdress() {
        Utility.createCustomTwoButtonDialog(this,"Change address will go through admin verification process. Your Account will be temporarily inactive for 24 to 48 hours.", View.OnClickListener{   startEditAdressScreen()     })
    }

    private fun startEditAdressScreen() {
        startActivityForResult(Intent(this@EditProfileActivity,LaunchActivity::class.java),100)
    }

    private fun updateUser(buyerInfo: BuyerInfo) {
        val updateService=RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        updateService?.postUpdateUser("application/json",
            "application/json",createUser(buyerInfo)).also {
            it?.enqueue(object : Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                  if(response.isSuccessful&&response.body()!=null)
                   updateBuerDB(response.body()?.Id)
                    finish()
                }

            })

        }
    }

    private fun updateBuerDB(id: Int?) {
        NextDoorDB.invoke(this@EditProfileActivity).daoAccess.updateBuyerProfile(user_id = id!!,fullName = tv_edit_full_name_edit_profile.text.toString(),gender=(findViewById(gender_edit.checkedRadioButtonId) as RadioButton).text.toString(),flatNumber= tv_edit_flat_number_edit_profile.text.toString(),email= tv_edit_email_edit_profile.text.toString(),profileImageUrl = filepath)
    }



    private fun createUser(buyerInfo: BuyerInfo): UpdateUser {
       val uset= UpdateUser(FullName = tv_edit_full_name_edit_profile.text.toString(),FlatNumber = tv_edit_flat_number_edit_profile.text.toString(),ProfileImageUrl = filepath,UserId = buyerInfo.userId,Email = tv_edit_email_edit_profile.text.toString(),Gender = (findViewById(gender_edit.checkedRadioButtonId) as RadioButton).text.toString())
        val gson= Gson()

        val s= gson.toJson(uset)
        var sr="sarith"
        return uset
    }

    private fun onclickProfileImage() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
                img_profile_pic_edit_profile.setImageURI(resultUri)
                val file = File( resultUri.path)
                filepath=file.name
                if (file != null) {
                    //Initialize UploadTask
                    UploadTask(
                        DropboxClient.getClient(Utility.DB_ACCESS_TOKEN!!),
                        file,
                        applicationContext
                    ).execute()
                }

            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

}
