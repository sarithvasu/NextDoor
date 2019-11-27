package com.food.nextdoor.activity.seller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.food.nextdoor.R
import com.food.nextdoor.dropbox.DropboxClient
import com.food.nextdoor.dropbox.UploadTask
import com.food.nextdoor.model.post.NewDish
import com.food.nextdoor.model.post.PostAddNewDish
import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.gson.Gson
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.seller_upload.*
import kotlinx.android.synthetic.main.seller_upload_secondary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import system.Preference
import system.Utility
import system.Validation
import java.io.File

class SellerUpload : AppCompatActivity(), View.OnClickListener {

    private var afterServing: Int = 0
    private var filname:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_upload)

       // tv_save_and_continue_seller_upload.setOnClickListener(this)
        tv_update_seller_upload.setOnClickListener(this)
        tv_save_seller_upload.setOnClickListener (this)
        tv_save_and_active_seller_upload.setOnClickListener (this)
        img_upload_image_seller_upload.setOnClickListener(this)
        tv_dish_price_seller_upload.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(!p0.toString().equals("")){
                    var unit = p0.toString().toInt()
                    afterServing = unit + (unit.toDouble() * .02).toInt()
                    tv_earnings_after_service_charge_seller_upload.text = afterServing.toString()
                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })



    }
    enum class SaveType{
        SAVE,SAVE_AND_ACTIVE
    }
    private var saveType : SaveType=SaveType.SAVE


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_update_seller_upload -> {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
            }
            R.id.tv_save_seller_upload -> {
                if(validationDone()) {
                    saveType = SaveType.SAVE
                    uploadNewDishwithOutTimeSlot()
                }
            }
            R.id.tv_save_and_active_seller_upload ->{
                if(validationDone()) {
                    saveType = SaveType.SAVE_AND_ACTIVE
                    uploadNewDishwithOutTimeSlot()
                }
            }
            R.id.img_upload_image_seller_upload->{
                this.loadImage()
            }
        }
    }
    private fun loadImage() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this);

    }

    private fun uploadNewDishwithOutTimeSlot() {
        val dish=createDishInfo()
        busy_bar_seller_upload.visibility=VISIBLE
        val dishInfoPostService: RetrofitService =
            RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
        dishInfoPostService.postAddNewDish(
            "application/JSON",
            "application/JSON",
            dish
        ).also {
            it.enqueue(object : Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                    val res = t.localizedMessage
                    val s = "sarith vasu"
                    busy_bar_seller_upload.visibility= GONE
                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                    busy_bar_seller_upload.visibility=GONE
                  if(response.isSuccessful&&response.body()!=null){
                      if(saveType==SaveType.SAVE){
                          Utility.createCustomDialog(this@SellerUpload,"Dish has been uploaded",View.OnClickListener {  finish() })
                      }
                      else if(saveType==SaveType.SAVE_AND_ACTIVE){
                          val intent1 = Intent(this@SellerUpload,SellerUploadSecondary::class.java)
                          intent1.putExtra(Utility.DISH_ID_KEY,response.body()!!.Id)
                          startActivity(intent1)
                          finish()
                      }
                  }
                }

            })
        }
    }

    private fun createDishInfo(): PostAddNewDish {
        return PostAddNewDish(
            DishId = -1,
            CategoryId = 1,
            ChefId = Preference(this).getUserChefId(),
            DishDescription = tv_dish_description_seller_upload.text.toString(),
            DishName = tv_dish_name_seller_upload.text.toString(),
            DishImageUrl = filname,
            DishType = (findViewById(rg_veg_or_non_veg1.checkedRadioButtonId) as RadioButton).text.toString(),
            EarningAfterServiceCharges =afterServing,//tv_earnings_after_service_charge_seller_upload.text.toString().toInt(),
            UnitPrice = tv_dish_price_seller_upload.text.toString().toInt(),
            SubCategoryId = 1,
            MealType =  (findViewById(rg_meal_type.checkedRadioButtonId) as RadioButton).text.toString(),
            IsSignatureDish =false,
            ServingsPerPlate = tv_serving_per_plate_seller_upload.text.toString().toInt(),
            UserId = Preference(this).getUserId()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri

                img_upload_image_seller_upload.setImageURI(resultUri)
                val file = File( resultUri.path)
                filname=file.name
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
    private fun validationDone():Boolean{
        var msg=""
        val v = Validation()
        if (!v.isValidFullName(tv_dish_name_seller_upload.text.toString())) msg =
            "invalid Dish name"
        else if (!v.isValid250Character(tv_dish_description_seller_upload.text.toString())) msg =
            "invalid text"
        else if (!v.isValidDishPrice(tv_dish_price_seller_upload.text.toString())) msg =
            "invalid Dish Price"
        else if(rg_veg_or_non_veg1.checkedRadioButtonId == -1){
            msg = "Select a time slot"
        }
        else if(rg_meal_type.checkedRadioButtonId == -1){
            msg = "Select a meal type"
        }
        else if(filname.equals("")){
            msg = "Upload dish image"
        }
        if(!msg.equals("")){
            Utility.createCustomDialog(this, msg ,true)
            return false
        }
        return true
    }


}
