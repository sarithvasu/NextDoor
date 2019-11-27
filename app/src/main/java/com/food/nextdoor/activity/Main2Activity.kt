package com.food.nextdoor.activity

import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast


import androidx.core.app.ActivityCompat
import com.food.nextdoor.R
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog

import system.SaveImageHelper
import java.lang.ref.WeakReference
import java.util.*


class Main2Activity : AppCompatActivity() {
// https://www.youtube.com/watch?v=cF84eWt-3y4
    private val PERMISSION_REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }



        //val dialog: AlertDialog = SpotsDialog
        val fileName: String = UUID.randomUUID().toString()
        val builder = androidx.appcompat.app.AlertDialog.Builder(baseContext)
        val dialog = builder.create()
        dialog.setMessage("dfdsfsd")
        Picasso.with(baseContext).load("https://www.ndtv.com/cooks/images/kadhai%20paneer.jpg")
            .into( SaveImageHelper(WeakReference(dialog),WeakReference(applicationContext.contentResolver) ,fileName,"Image Desc",baseContext))



            //.into(SaveImageHelper().SaveImageHelper(baseContext,applicationContext.contentResolver,fileName,"Image Desc"))
        // https://www.ndtv.com/cooks/images/kadhai%20paneer.jpg
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            PERMISSION_REQUEST_CODE -> {
               /* if(grantResults > 0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED  ) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }*/
            }
        }
    }
}
