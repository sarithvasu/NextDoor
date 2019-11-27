package com.food.nextdoor.cloud

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.registration.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FirebaseTest {

    private var mAuth: FirebaseAuth? = null
//    fun SaveImageInFirebase(){
//        var currentUser = mAuth!!.currentUser
//        val email: String= currentUser!!.email.toString()
//        val storage = FirebaseStorage.getInstance()
//        val storgaRef = storage.getReferenceFromUrl("gs://nextdoor-29d7a.appspot.com")
//        val df = SimpleDateFormat("ddMMyyHHmmss")
//        val dateobj = Date() // java Date
//        val imagepath = SplitString(email) + "."+ df.format(dateobj) + ".jpg"
//        val ImageRef = storgaRef.child("FoodImages/" + imagepath)
//
//        img_customer_image_reg.isDrawingCacheEnabled = true
//        img_customer_image_reg.buildDrawingCache()
//
//
//        val drawable = img_customer_image_reg.drawable as BitmapDrawable
//        val bitmap = drawable.bitmap
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
//        val data = baos.toByteArray()
//        val uploadTask = ImageRef.putBytes(data)
//
//        uploadTask.addOnFailureListener {
//            Toast.makeText(applicationContext,"Fail to upload", Toast.LENGTH_LONG).show()
//        }.addOnSuccessListener {  taskSnapshot ->
//            val DownloadURL = taskSnapshot.uploadSessionUri
//            Log.d("FirebaseManager", DownloadURL.toString())
//
//            // To register to database
//            //mvRef.child("Users").child(currentUser.uid).child("email").setValue(currentUser.email)
//            // mvRef.child("Users").child(currentUser.uid).child("ProfileImage").setValue(currentUser.email)
//        }
//    }


//    private fun signInAnonymously() {
//        mAuth!!.signInAnonymously().addOnCompleteListener(this,{task->
//            Log.d("LoginIfo:", task.isSuccessful.toString())
//        })
//    }


}