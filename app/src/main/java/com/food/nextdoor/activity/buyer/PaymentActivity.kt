package com.food.nextdoor.activity.buyer

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import com.food.nextdoor.R
import java.util.ArrayList

class PaymentActivity : AppCompatActivity() {

    private var txt: String=""
    val UPI_PAYMENT = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
    }

    private fun payUsingUPI(amountTxt: String, nametTxt: String, upiTxt: String, notetTxt: String) {
            val uri = Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa", upiTxt)
                .appendQueryParameter("pn", nametTxt)
                .appendQueryParameter("tn", notetTxt)
                .appendQueryParameter("am", amountTxt)
                .appendQueryParameter("cu", "INR")
                .build()
            val payUPI = Intent(Intent.ACTION_VIEW)
            payUPI.data = uri
            val chooser = Intent.createChooser(payUPI, "Pay with")
            if (null != chooser.resolveActivity(packageManager)) {
                startActivityForResult(chooser, UPI_PAYMENT)
            } else {
                Toast.makeText(this, "No UPI App found", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            UPI_PAYMENT -> {
                //(findViewById(R.id.result) as TextView).text = "request code"
                if (RESULT_OK === resultCode) {
                   // result.setText("request code$resultCode")
                    if (data != null) {
                        txt = data.getStringExtra("response")
                        //(findViewById(R.id.result) as TextView).setText(txt)
                        Log.e("UPI", "" + txt)
                        val datalist = ArrayList<String>()
                        datalist.add(txt)
                        //(findViewById(R.id.result) as TextView).text = "data not null"
                        upiPaymentOpration(datalist)
                    } else {

                        Log.e("UPI", "data is null")
                        val datalist = ArrayList<String>()
                        datalist.add("Nothing")
                        upiPaymentOpration(datalist)
                    }

                } else {

                    Log.e("UPI", "data is null")
                    val datalist = ArrayList<String>()
                    datalist.add("Nothing")
                    upiPaymentOpration(datalist)
                }
            }
        }
    }
    private fun upiPaymentOpration(datalist: ArrayList<String>) {
        if (conntionIsAvailable(this)) {
            var str: String? = datalist[0]
            // ((TextView)findViewById(R.id.result)).setText("data is "+datalist.get(0));
            val paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approveRef = ""
            val response = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val append = StringBuilder()
            for (i in response.indices) {
                val equlStr = response[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                append.append(response[i] + "\n")

                if (equlStr.size >= 2) {
                    if (equlStr[0].toLowerCase() == "Status".toLowerCase()) {
                        status = equlStr[1].toLowerCase()
                        //  ((TextView)findViewById(R.id.result)).setText("status is "+status);
                        if (status == "success") {
                            //result.setText("status"+status);
                            Log.e("UPI", "" + status)
                            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                        }
                    } else if (equlStr[0].toLowerCase() == "approval Ref".toLowerCase() || equlStr[0].toLowerCase() == "txnRef".toLowerCase()) {
                        approveRef = equlStr[1].toLowerCase()
                        Log.e("UPI", "" + approveRef)
                    }
                }

            }
           // result.setText("" + append.toString() + "\n" + txt)
        }
    }

    private fun conntionIsAvailable(mainActivity: PaymentActivity): Boolean {
        return true
    }

}
