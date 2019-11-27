package com.food.nextdoor.activity.buyer

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.food.nextdoor.R
import com.food.nextdoor.activity.EditProfileActivity
import com.food.nextdoor.adapter.buyer.BillDetailAdapter
import com.food.nextdoor.adapter.buyer.CheckoutAdapter
import com.food.nextdoor.listeners.OnItemCountListener
import com.food.nextdoor.model.CheckoutBillSummery
import com.food.nextdoor.model.VerifyDish
import com.food.nextdoor.model.post.*
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.gson.Gson
import kotlinx.android.synthetic.main.checkout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import system.*


class CheckoutActivity : AppCompatActivity(), OnItemCountListener {

    private var cartTotal: Int = 0
    private var totalPackingCharges: Int = 0
    private var totalDeliveryCharges: Int = 0
    private var billTotal: Int = 0
    val UPI_PAYMENT = 0
    private var txt: String = ""
    private var payment: CheckoutByUPIPayment = CheckoutByUPIPayment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkout)
        ConfigureCheckoutRecycleView()
        ConfigureBillSummeryRecycleView()
        // Set Controls
        setControls()
        tv_add_more_items_checkout.setOnClickListener {
            val intent = Intent(tv_add_more_items_checkout.context, HomeActivity::class.java)
            tv_add_more_items_checkout.context.startActivity(intent)
        }

        tv_edit_checkout.setOnClickListener {
            // startActivity(Intent(this, RegistrationActivity::class.java))
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra(Utility.ACTIVITY_NAME, "From CheckoutActivity" as String)
            startActivity(intent)
        }



        tv_place_order_checkout.setOnClickListener {

            if (tv_payment_cash_checkout.isChecked) {
                postOrderRetrofit()

            } else {

                checkAvalilibitiy()
            }
            // payUsingUPI(ShoppingCart.getTotalCartAmount().toString(),"Sarith","Nextdoor Payment","sarithvasu@oksbi")
            // startActivity(Intent(this, OrderConfirmationActivity::class.java))
        }
    }

    private fun checkAvalilibitiy(): Boolean {
        var flag = false
        val checkOrderService: RetrofitService =
            RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
        checkOrderService.postVerifyStock(
            "application/json",
            "application/json",
            creatVerify()
        ).also {
            it.enqueue(object : Callback<ArrayList<VerifyDish>> {
                override fun onFailure(call: Call<ArrayList<VerifyDish>>, t: Throwable) {
                    val sarith = "SARITH"
                }

                override fun onResponse(
                    call: Call<ArrayList<VerifyDish>>,
                    response: Response<ArrayList<VerifyDish>>
                ) {

                    val data = response.body()
                    if (data!!.isEmpty()) {
                        payUsingUPINewABc(
                            "1",
                            "Sarith",
                            "keerthireddybc@okhdfcbank",
                            "Nextdoor Payment"
                        )
                        flag = true
                    } else {
                        val verficationList = response.body()
                        if(verficationList!![0].StockValue.toIntOrNull()!=null) {
                            Utility.createCustomDialog(
                                this@CheckoutActivity,
                                "Only ${verficationList!![0].StockValue} is/are avalable",true
                            )
                        }else{
                            Utility.createCustomDialog(
                                this@CheckoutActivity,
                                "avail upto   ${verficationList!![0].StockValue}",true
                            )
                        }
                    }
                }

            })
        }
        return flag
    }

    private fun creatVerify(): java.util.ArrayList<VerifyCheckOut> {

        val cartItems = ShoppingCart.getCartItems()
        var verifyCheckOuts: ArrayList<VerifyCheckOut> = ArrayList<VerifyCheckOut>()
        for (cart in cartItems) {
            val verifyCheckOut: VerifyCheckOut = VerifyCheckOut(
                DishAvailableEndTime = cart.dishItem?.deliveryEndTime,
                DishId = cart.dishItem.dishId,
                Quantity = cart.dishItem.quantity
            )
            verifyCheckOuts.add(verifyCheckOut)

        }
        val gson = Gson()
        val s = gson.toJson(verifyCheckOuts)
        return verifyCheckOuts


    }

    private fun postOrderRetrofit() {
        val placeOrderService: RetrofitService =
            RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
        placeOrderService.postCheckoutByCOD(
            "application/json",
            "application/json",
            creatCODOrder()
        ).also {
            it.enqueue(object : Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                    Utility.createCustomTwoButtonDialog(this@CheckoutActivity,"Please check your order history wheather order placeor not",
                        View.OnClickListener { startActivity(Intent(this@CheckoutActivity,
                            MyOrderActivity::class.java)) })

                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                    //val s=   JSONObject(response.errorBody()?.string()).getString("Message")
                    if(response.errorBody()!=null){
                        val gson=Gson()
                        var verifyDish = gson.fromJson(response.errorBody()?.string(), Array<VerifyDish>::class.java)
                        if(verifyDish!![0].StockValue.toIntOrNull()!=null) {
                            Utility.createCustomDialog(
                                this@CheckoutActivity,
                                "Only ${verifyDish!![0].StockValue} is/are avalable",true
                            )
                        }else {
                            Utility.createCustomDialog(
                                this@CheckoutActivity,
                                "avail upto   ${verifyDish!![0].StockValue}",true
                            )
                        }
                    }
                    else{
                        Utility.createCustomDialog(this@CheckoutActivity,"Your Order has been placed",
                            View.OnClickListener {ShoppingCart.clearCart()
                                finish() })

                    }

                    val sarith = "SARITH"
                }

            })
        }


    }

    private fun creatCODOrder(): CheckoutByCOD {

        return CheckoutByCOD(creatCODOrderList(), null)
    }

    private fun creatCODOrderList(): List<CheckoutByCODOrder> {

        val cartItems = ShoppingCart.getCartItems()
        var checkoutByCODs: ArrayList<CheckoutByCODOrder> = ArrayList<CheckoutByCODOrder>()
        for (cart in cartItems) {
            val checkoutByCOD: CheckoutByCODOrder = CheckoutByCODOrder(
                BuyerId = Preference(this).getUserId(),
                ChefId = cart.dishItem.chefId,
                DeliverStartTime = cart.dishItem.deliveryStartTime,
                DeliverEndTime = cart.dishItem.deliveryEndTime,
                DeliveryCharges = (DataHolder.homeFeedInstance.dishes.find { dish -> dish.DishId == cart.dishItem.dishId })!!.DeliveryCharge,
                DishId = cart.dishItem.dishId,
                PackingCharges = (DataHolder.homeFeedInstance.dishes.find { dish -> dish.DishId == cart.dishItem.dishId })!!.PackageCharge,
                PackingDescription = "TEST PACKAGE" /*(DataHolder.homeFeedInstance.dishes.find { dish -> dish.DishId == cart.dishItem.dishId  })?.PackingDescription*/,
                DeliveryDescription = "TEST DELIVERY"/*(DataHolder.homeFeedInstance.dishes.find { dish -> dish.DishId == cart.dishItem.dishId  })?.DeliveryDescription*/,
                Discount = 0,
                ItemTotal = cart.dishItem.unitPrice,
                Quantity = cart.dishItem.quantity,
                TotalDeliveryCharges =50,/* cart.dishItem.quantity,*/
                TotalPackingCharges = 0,
                OrderTotal = cartTotal,
                PaymentMode = "COD"
            )
            checkoutByCODs.add(checkoutByCOD)

        }
        val gson = Gson()
        val s = gson.toJson(checkoutByCODs)
        return checkoutByCODs
    }


    private fun ConfigureBillSummeryRecycleView() {
        val checkoutBillSummery = getCheckoutBillSummery()
        val lLayoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(this)
        lLayoutManager1.orientation = RecyclerView.VERTICAL
        recyclerView_bill_details_checkout.apply {
            adapter = BillDetailAdapter(this@CheckoutActivity, checkoutBillSummery)
            layoutManager = lLayoutManager1
        }
    }

    private fun ConfigureCheckoutRecycleView() {
        val layoutManager1 = androidx.recyclerview.widget.LinearLayoutManager(this)
        layoutManager1.orientation = RecyclerView.VERTICAL
        recyclerView_checkout.layoutManager = layoutManager1
        recyclerView_checkout.apply {
            recyclerView_checkout.adapter = CheckoutAdapter(this@CheckoutActivity)
        }
    }

    private fun getCheckoutBillSummery(): ArrayList<CheckoutBillSummery> {
        cartTotal = ShoppingCart.getTotalCartAmount()
        totalPackingCharges = ShoppingCart.getTotalPackingCharges()
        totalDeliveryCharges = ShoppingCart.getTotalDeliveryCharges()
        billTotal = (totalPackingCharges + totalDeliveryCharges + cartTotal)

        val checkoutBillDetails = ArrayList<CheckoutBillSummery>()



        if (cartTotal > 0) {
            val cartTotal = CheckoutBillSummery(::cartTotal.name, cartTotal)
            checkoutBillDetails.add(cartTotal)
        }

        if (totalPackingCharges > 0) {
            val packingCharges =
                CheckoutBillSummery(::totalPackingCharges.name, totalPackingCharges)
            checkoutBillDetails.add(packingCharges)
        }

        if (totalDeliveryCharges > 0) {
            val deliveryCharges =
                CheckoutBillSummery(::totalDeliveryCharges.name, totalDeliveryCharges)
            checkoutBillDetails.add(deliveryCharges)
        }

        if (billTotal > 0) {
            val billtotal = CheckoutBillSummery(::billTotal.name, billTotal)
            checkoutBillDetails.add(billtotal)
        }

        // Return
        return checkoutBillDetails
    }

    //region set control
    private fun setControls() {
        val buyerInfo = Manager.Companion.buyerInfo
        // img_buyer_profile_checkout neeed toa add
        tv_buyer_name_checkout.setText(buyerInfo.fullName + ", ")
        tv_buyer_address_with_flat_number_checkout.setText(buyerInfo.flatNumber + ", ")
//        tv_buyer_email_checkout.setText( buyerInfo.email)
        tv_buyer_phone_number_checkout.setText(buyerInfo.mobileNumber)
    }
    //endregion


    fun onClickPlaceOrder(view: View) {
        // this.postOrder()
        startActivity(Intent(this, OrderConfirmationActivity::class.java))
        //startActivity(Intent(this, MyOrderActivity::class.java))
    }

    private fun postOrder() {
        var postOrder = PostOrder()
        val cartItems: ArrayList<CartItem> = ShoppingCart.getCartItems()
        val abcc: List<DishItem> = cartItems.map { r -> r.dishItem } as List<DishItem>
        postOrder.dishes = abcc

        val z = cartItems[0]
        z.dishItem

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


    fun payUsingUPINew(amountTxt: String, nametTxt: String, upiTxt: String, notetTxt: String) {

        val uri = Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa", upiTxt)
            .appendQueryParameter("pn", nametTxt)
            .appendQueryParameter("tn", notetTxt)
            .appendQueryParameter("am", amountTxt)
            .appendQueryParameter("cu", "INR")
            .build()
        val targetShareIntents = ArrayList<Intent>()
        val shareIntent = Intent()
        shareIntent.setAction(Intent.ACTION_VIEW)
        shareIntent.data = uri
        val resInfos = getPackageManager().queryIntentActivities(shareIntent, 0)
        if (!resInfos.isEmpty()) {
            println("Have package")
            for (resInfo in resInfos) {
                val packageName1 = resInfo.activityInfo.packageName
                Log.i("Package Name", packageName)
                if (packageName1.contains("net.one97.paytm") || packageName1.equals("com.google.android.apps.nbu.paisa.user") || packageName1.contains(
                        "com.phonepe.app"
                    )
                ) {
                    val intent = Intent()
                    intent.setComponent(ComponentName(packageName, resInfo.activityInfo.name))
                    intent.setAction(Intent.ACTION_VIEW)
                    intent.data = uri
                    intent.setPackage(packageName1)
                    targetShareIntents.add(intent)
                }
            }
            if (!targetShareIntents.isEmpty()) {
                println("Have Intent")
                // val chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to share")
                val firstIntent = targetShareIntents[0]
                val chooserIntent = Intent.createChooser(firstIntent, "Choose app pay")
                targetShareIntents.remove(firstIntent)

                chooserIntent.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    targetShareIntents.toArray(arrayOf<Parcelable>())
                )
                startActivity(chooserIntent)
            }
        }
    }

    //
    private fun payUsingUPINewABc(
        amountTxt: String,
        nametTxt: String,
        upiTxt: String,
        notetTxt: String
    ) {

        val uri = Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa", upiTxt)
            .appendQueryParameter("pn", nametTxt)
            .appendQueryParameter("tn", notetTxt)
            .appendQueryParameter("am", amountTxt)
            .appendQueryParameter("cu", "INR")
            .build()

        val s = uri.path
        payment.CurrencyCode = "INR"
        payment.TransactionAmount = amountTxt.toInt()
        payment.TransactionNote = nametTxt


        val targets = ArrayList<Intent>()
        val template = Intent(Intent.ACTION_VIEW)
        template.data = uri
        val candidates = this.getPackageManager().queryIntentActivities(template, 0)
        // filter package here
        for (candidate in candidates) {
            val packageName = candidate.activityInfo.packageName
            if (packageName.equals("net.one97.paytm") || packageName.equals("com.google.android.apps.nbu.paisa.user") || packageName.equals(
                    "com.phonepe.app"
                ) || packageName.equals("in.org.npci.upiapp")
            ) {
                val target = Intent(android.content.Intent.ACTION_VIEW)
                target.data = uri
                target.setPackage(packageName)
                targets.add(target)
            }
        }
        if (!targets.isEmpty()) {
            val chooser = Intent.createChooser(targets.removeAt(0), "Pay money with")
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(arrayOf<Parcelable>()))
            startActivityForResult(chooser, UPI_PAYMENT)
        }
    }


    override fun updateCount() {
        ConfigureBillSummeryRecycleView()
    }

    private fun upiPaymentOpration(datalist: java.util.ArrayList<String>) {
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
                val equlStr =
                    response[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                append.append(response[i] + "\n")

                if (equlStr.size >= 2) {
                    if (equlStr[0].toLowerCase() == "Status".toLowerCase()) {
                        status = equlStr[1].toLowerCase()
                        //  ((TextView)findViewById(R.id.result)).setText("status is "+status);
                        if (status == "success") {
                            //result.setText("status"+status);
                            Log.e("UPI", "" + status)
                            payment.TransactionStatus = status
                            Log.e("UPI SUCCESS 11111", "" + status)

                            callOrderUPIPayment()
                            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                        } else {
                            Utility.createCustomDialog(this,  "PAYMENT FAILED",true)
                        }
                    } else if (equlStr[0].toLowerCase() == "approval Ref".toLowerCase() || equlStr[0].toLowerCase() == "txnRef".toLowerCase()) {
                        approveRef = equlStr[1].toLowerCase()
                        Log.e("UPI", "" + approveRef)
                        payment.ApprovalReferenceNumberBeneficiary = equlStr[1]
                        Log.e("UPI SUCCESS 22222", "" + payment.TransactionReferenceId)
                    } else if (equlStr[0].toLowerCase() == "txnId".toLowerCase()) {
                        payment.TransactionId = equlStr[1]
                    } else if (equlStr[0].toLowerCase() == "responseCode".toLowerCase()) {
                        payment.ResponseCode = equlStr[1]
                    }
                }

            }
            // result.setText("" + append.toString() + "\n" + txt)
        }
    }

    private fun callOrderUPIPayment() {
        val placeOrderServiceUPIPayment: RetrofitService =
            RetrofitInstantBuilder.buildService(RetrofitService::class.java)!!
        placeOrderServiceUPIPayment.postCheckoutByUPI(
            "application/json",
            "application/json",
            createUPIPaymentOrderObject()
        ).also {
            it.enqueue(object : Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                    Utility.createCustomTwoButtonDialog(this@CheckoutActivity,"Please check your order history wheather order placeor not",
                        View.OnClickListener { startActivity(Intent(this@CheckoutActivity,
                            MyOrderActivity::class.java)) })
                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                    if(response.errorBody()!=null){
                        val gson=Gson()
                        var verifyDish = gson.fromJson(response.errorBody()?.string(), Array<VerifyDish>::class.java)
                        if(verifyDish!![0].StockValue.toIntOrNull()!=null) {
                            Utility.createCustomDialog(
                                this@CheckoutActivity,

                                "Only ${verifyDish!![0].StockValue} is/are avalable",true
                            )
                        }else {
                            Utility.createCustomDialog(
                                this@CheckoutActivity,

                                "avail upto   ${verifyDish!![0].StockValue}",true
                            )
                        }
                    }
                    else{
                        Utility.createCustomDialog(this@CheckoutActivity,"Placed the order", View.OnClickListener {
                            ShoppingCart.clearCart()
                            finish() })
                    }
                }

            })
        }

    }

    private fun createUPIPaymentOrderObject(): CheckoutByUPI {
        return CheckoutByUPI(creatCODOrderList(), payment)
    }


    private fun conntionIsAvailable(mainActivity: CheckoutActivity): Boolean {
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("UPI SUCCESS", "" + data.toString())

        when (requestCode) {
            UPI_PAYMENT -> {
                //(findViewById(R.id.result) as TextView).text = "request code"
                if (RESULT_OK === resultCode) {
                    // result.setText("request code$resultCode")
                    if (data != null) {
                        txt = data.getStringExtra("response")
                        //(findViewById(R.id.result) as TextView).setText(txt)
                        Log.e("UPI", "" + txt)
                        val datalist = java.util.ArrayList<String>()
                        datalist.add(txt)
                        //(findViewById(R.id.result) as TextView).text = "data not null"
                        upiPaymentOpration(datalist)
                    } else {

                        Log.e("UPI", "data is null")
                        val datalist = java.util.ArrayList<String>()
                        datalist.add("Nothing")
                        upiPaymentOpration(datalist)
                    }

                } else {

                    Log.e("UPI", "data is null")
                    val datalist = java.util.ArrayList<String>()
                    datalist.add("Nothing")
                    upiPaymentOpration(datalist)
                }
            }
        }
    }

}
