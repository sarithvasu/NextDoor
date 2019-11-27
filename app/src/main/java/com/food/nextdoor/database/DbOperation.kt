package com.food.nextdoor.database

import android.content.Context
import android.database.sqlite.SQLiteException
import android.os.AsyncTask
import com.food.nextdoor.model.*
import com.food.nextdoor.model.post.*
//import com.food.nextdoor.model.post.ProductView
import system.Utility
import java.lang.ref.WeakReference
import java.util.ArrayList

class DbOperation {
    class InsertBuyerInfo internal constructor(context: Context) :  AsyncTask<BuyerInfo, Any, Any>(){
        private val  weakContext: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg p0: BuyerInfo):Any? {

            try {
                return NextDoorDB.invoke(weakContext.get()!!).daoAccess.insertBuyerInfo(buyerInfo = p0[0])

                //return NextDoorDB.invoke(weakContext.get()!!).daoAccess.getBuyerInfo() as ArrayList<BuyerInfo>
            } catch (e: Exception) {
                return  e
            }
        }


    }

    class InsertPackingType internal constructor(context: Context) :  AsyncTask<PackingType, Any, Any>(){
        private val  weakContext: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg p0: PackingType):Any? {

            try {
                return NextDoorDB.invoke(weakContext.get()!!).daoAccess.insertPackingType(packingType = p0[0])

                //return NextDoorDB.invoke(weakContext.get()!!).daoAccess.getBuyerInfo() as ArrayList<BuyerInfo>
            } catch (e: Exception) {
                return e
            }
        }

        override fun onPostExecute(result: Any) {
            super.onPostExecute(result)
            if (result is SQLiteException) {
             //   Utility.createDialog(weakContext.get()!!, "insertPackingType Failed", result.toString())
            } else {
             //   Utility.createDialog(weakContext.get()!!, "insertPackingType Success", result.toString())
            }
        }
    }
    class InsertDeliveryType internal constructor(context: Context) :  AsyncTask<DeliveryType, Any, Any>(){
        private val  weakContext: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg p0: DeliveryType):Any? {

            try {
                return NextDoorDB.invoke(weakContext.get()!!).daoAccess.insertDeliveryType(deliveryType = p0[0])

                //return NextDoorDB.invoke(weakContext.get()!!).daoAccess.getBuyerInfo() as ArrayList<BuyerInfo>
            } catch (e: Exception) {
                return e
            }
        }

        override fun onPostExecute(result: Any) {
            super.onPostExecute(result)
            if (result is SQLiteException) {
               // Utility.createDialog(weakContext.get()!!, "insertDeliveryType Failed", result.toString())
            } else {
              //  Utility.createDialog(weakContext.get()!!, "insertDeliveryType Success", result.toString())
            }
        }
    }
    class InsertPaymentMode internal constructor(context: Context) :  AsyncTask<PaymentMode, Any, Any>(){
        private val  weakContext: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg p0: PaymentMode):Any? {

            try {
                return NextDoorDB.invoke(weakContext.get()!!).daoAccess.insertPaymentMode(paymentMode = p0[0])
            } catch (e: Exception) {
                return e
            }
        }

        override fun onPostExecute(result: Any) {
            super.onPostExecute(result)
            if (result is SQLiteException) {
               // Utility.createDialog(weakContext.get()!!, "insertPaymentMode Failed", result.toString())
            } else {
               // Utility.createDialog(weakContext.get()!!, "insertPaymentMode Success", result.toString())
            }
        }
    }
    class InsertReviewTag internal constructor(context: Context) :  AsyncTask<ReviewTag, Any, Any>(){
        private val  weakContext: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg p0: ReviewTag):Any? {

            try {
                return NextDoorDB.invoke(weakContext.get()!!).daoAccess.insertReviewTag(reviewTag = p0[0])
            } catch (e: Exception) {
                return e
            }
        }

        override fun onPostExecute(result: Any) {
            super.onPostExecute(result)
            if (result is SQLiteException) {
               // Utility.createDialog(weakContext.get()!!, "insertReviewTag Failed", result.toString())
            } else {
                //Utility.createDialog(weakContext.get()!!, "insertReviewTag Success", result.toString())
            }
        }
    }


    //**********************************************User Action*************************************************
    class InsertFollowUnfollow internal constructor(context: Context) :  AsyncTask<FollowUnfollow, Any, Any>(){
        private val  weakContext: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg p0: FollowUnfollow):Any? {
            try {
                return NextDoorDB.invoke(weakContext.get()!!).daoAccess.insertFollowUnfollow(followUnfollow = p0[0])
            } catch (e: Exception) {
                return e
            }
        }

        override fun onPostExecute(result: Any) {
            super.onPostExecute(result)
            if (result is SQLiteException) {
               // Utility.createDialog(weakContext.get()!!, "InsertFollowUnfollow Failed", result.toString())
            }
        }
    }
    class InsertShare internal constructor(context: Context) :  AsyncTask<Share, Any, Any>(){
        private val  weakContext: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg p0: Share):Any? {
            try {
                return NextDoorDB.invoke(weakContext.get()!!).daoAccess.insertShare(share = p0[0])
            } catch (e: Exception) {
                return e
            }
        }

        override fun onPostExecute(result: Any) {
            super.onPostExecute(result)
            if (result is SQLiteException) {
               // Utility.createDialog(weakContext.get()!!, "InsertShare Failed", result.toString())
            }
        }
    }
    class InsertProductView internal constructor(context: Context) :  AsyncTask<ProductView, Any, Any>(){
        private val  weakContext: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg p0: ProductView):Any? {
            try {
                return NextDoorDB.invoke(weakContext.get()!!).daoAccess.insertProductView(productView = p0[0])
            } catch (e: Exception) {
                return e
            }
        }

        override fun onPostExecute(result: Any) {
            super.onPostExecute(result)
            if (result is SQLiteException) {
               // Utility.createDialog(weakContext.get()!!, "InsertProductView Failed", result.toString())
            }
        }
    }
    class InsertLikeDislike internal constructor(context: Context) :  AsyncTask<LikeDislike, Any, Any>(){
        private val  weakContext: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg p0: LikeDislike):Any? {
            try {
                return NextDoorDB.invoke(weakContext.get()!!).daoAccess.insertLikeDislike(likeDislike = p0[0])
            } catch (e: Exception) {
                return e
            }
        }

        override fun onPostExecute(result: Any) {
            super.onPostExecute(result)
            if (result is SQLiteException) {
               // Utility.createDialog(weakContext.get()!!, "InsertLikeDislike Failed", result.toString())
            }
        }
    }


}



