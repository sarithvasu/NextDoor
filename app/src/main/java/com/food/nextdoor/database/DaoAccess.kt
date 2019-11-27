package com.food.nextdoor.database

import androidx.room.*

import com.food.nextdoor.model.*
import com.food.nextdoor.model.post.*
import androidx.room.Delete




@Dao
interface DaoAccess {



    //--------------------------------------- Buyer---------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBuyerInfo(buyerInfo: BuyerInfo): Long

    @Query(value = "select * from buyer_info WHERE userId = :userId LIMIT 1")
    fun getBuyerInfo(userId: Int): BuyerInfo
    //-----------------------------------------END Buyer-----------------------------------------------


    //--------------------------------------- PackingType ------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPackingType(packingType: PackingType): Long


    @Query(value = "select * from packing_type")
    fun getPackingTypeList():List<PackingType>


    @Query(value = "select packingTypeId from packing_type WHERE packingDescription = :strDescription LIMIT 1")
    fun getPackingTypeIdByDescription(strDescription: String): Int


    @Query(value = "select packingDescription  from packing_type WHERE packingTypeId = :packingId LIMIT 1")
    fun getPackingDescriptionById(packingId: Int): String
    //---------------------------------------END PackingType ----------------------------------------------


    //--------------------------------------- DeliveryType ------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDeliveryType(deliveryType: DeliveryType):Long


    @Query(value = "select * from delivery_type")
    fun getDeliveryTypeList():List<DeliveryType>


    @Query(value = "select deliveryTypeId from delivery_type WHERE deliveryDescription = :strDescription LIMIT 1")
    fun getDeliveryTypeIdByDescription(strDescription: String): Int


    @Query(value = "select deliveryDescription  from delivery_type WHERE deliveryTypeId = :deliveryId LIMIT 1")
    fun getDeliveryDescriptionById(deliveryId: Int): String
    //---------------------------------------END DeliveryType ----------------------------------------------


    //--------------------------------------- PaymentMode------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPaymentMode(paymentMode: PaymentMode): Long

    @Query(value = "select paymentModeId from payment_mode WHERE paymentModeDescription = :strPaymentMode LIMIT 1")
    fun getPaymentModeIdByDescription(strPaymentMode: String): Int
    //---------------------------------------END PaymentMode----------------------------------------------



    //--------------------------------------- ReviewTag------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReviewTag(reviewTag: ReviewTag): Long

    @Query(value = "select * from review_tag")
    fun getReviewTagList():List<ReviewTag>
    //---------------------------------------END ReviewTag----------------------------------------------


//**************************************************User Action***********************************************

    //--------------------------------------- FollowUnfollow------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFollowUnfollow(followUnfollow: FollowUnfollow): Long

    @Query(value = "select * from follow_unfollow")
    fun getFollowUnfollowList(): FollowUnfollow
    //---------------------------------------END FollowUnfollow----------------------------------------------


    //--------------------------------------- Share----------------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertShare(share: Share): Long

    @Query(value = "select * from share")
    fun getShareList():List<Share>
    //---------------------------------------END Share-------------------------------------------------------


    //--------------------------------------- ProductView----------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductView(productView: ProductView): Long

    @Query(value = "select * from product_view")
    fun getProductViewList():List<ProductView>
    //---------------------------------------END ProductView-------------------------------------------------


    //--------------------------------------- LikeDislike----------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLikeDislike(likeDislike: LikeDislike): Long

    @Query(value = "select * from like_dislike")
    fun getLikeDislikeList():List<LikeDislike>
    //---------------------------------------END LikeDislikeReview-------------------------------------------

    @Update
    fun updateBuyerInfo(buyerInfo: BuyerInfo)


    @Query("UPDATE buyer_info SET flatNumber = :flatNumber,fullName =:fullName,email=:email,gender=:gender, profileImageUrl=:profileImageUrl WHERE userId = :user_id")
    fun updateBuyerProfile(user_id: Int, flatNumber: String,fullName:String, email: String,gender: String,profileImageUrl:String): Int



    @Query("UPDATE buyer_info SET flatNumber = :flatNumber,apartmentId =:apparmentId,apparmentName=:apparmentName,pinCode=:pinCode  WHERE userId = :user_id")
    fun updateBuyerAddress(user_id: Int, flatNumber: String,apparmentId:Int, apparmentName: String, pinCode: String): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDishView(View: PostViewed): Long

    @Query(value = "select * from dish_views")
    fun getDishViewList():List<PostViewed>

    @Query("DELETE FROM dish_views")
    fun deleteViewed()

    @Query("DELETE FROM shared_dish")
    fun deleteSharedDish()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSharedDish(sharedDish: SharedDish): Long

    @Query(value = "select * from shared_dish")
    fun getSharedDishList():List<SharedDish>

    @Query("UPDATE buyer_info SET userTypeId = :userTypeId WHERE userId = :user_id")
    fun updateBuyerUserType(user_id: Int,userTypeId:Int): Int




}