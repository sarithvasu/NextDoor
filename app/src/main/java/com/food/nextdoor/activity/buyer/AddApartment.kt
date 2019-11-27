package com.food.nextdoor.activity.buyer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.food.nextdoor.R
import com.food.nextdoor.activity.MainActivity
import com.food.nextdoor.model.post.NewAppartment
import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import kotlinx.android.synthetic.main.add_apartment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import system.Utility
import system.Validation

class AddApartment : AppCompatActivity() {
    val validation: Validation = Validation()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_apartment)
        tv_submit_add_apartment.setOnClickListener {
            if (validationDone()) {
                val addPostService: RetrofitService =
                    RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
                addPostService.postAddNewApartment(
                    "application/json",
                    "application/json",
                    creatAddAppartment()
                ).also {
                    it.enqueue(object : Callback<ResponseCode> {
                        override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                            val res = t.localizedMessage
                            /* val s="sarith vasu"
                             startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))*/
                            Utility.createCustomDialog(this@AddApartment,  "Something went wrong \n"+res,true)

                        }

                        override fun onResponse(
                            call: Call<ResponseCode>,
                            response: Response<ResponseCode>
                        ) {

                            Utility.createCustomDialog(this@AddApartment,  "validation may take 24 hours we will let you know the update",true)

                        }

                    })
                }

            }

        }


    }

    fun creatAddAppartment(): NewAppartment {
        return NewAppartment(
            Address = tv_address_line_1_add_apartment.text.toString() + " " + tv_address_line_2_add_apartment,
            ApartmentName = tv_apartment_name_add_apartment.text.toString(),
            Area = tv_area_locality_add_apartment.text.toString(),
            PinCode = tv_pincode_add_apartment.text.toString(),
            RequesterFullName = tv_person_name_add_apartment.text.toString(),
            RequesterPhoneNumber = "91"+tv_person_mobile_number_add_apartment.text.toString()
        )
    }

    private fun validationDone(): Boolean {
        var msg: String = ""
        val v = Validation()
        if (!v.isValidFullName(tv_person_name_add_apartment.text.toString())) msg = "invalid name"
        else if (!v.isValidAddress(tv_apartment_name_add_apartment.text.toString())) msg =
            "invalid Apparment Name"
        else if (!v.isValidPhone(tv_person_mobile_number_add_apartment.text.toString())) msg =
            "invalid mobile number"
        else if (!v.isValidAddress(tv_address_line_1_add_apartment.text.toString())) msg =
            "invalid address line 1"
        else if (!v.isValidAddress(tv_address_line_2_add_apartment.text.toString())) msg =
            "invalid address line 2 "
        else if (!v.isValidAddress(tv_area_locality_add_apartment.text.toString())) msg =
            "invalid Area or locality"
        else if (!v.isValidPin(tv_pincode_add_apartment.text.toString())) msg = "invalid PINy"
        if (msg != "") {
            Utility.createCustomDialog(this,  msg,true)
            return false
        }
        return true
    }
}
