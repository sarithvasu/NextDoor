package com.food.nextdoor.webservices


import com.food.nextdoor.model.*
import com.food.nextdoor.model.post.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    @GET("Feed/GetHomeFeedByApartmentId")
    fun getBuyerHomeData(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("ApartmentId") appartmentId:  Int): Call<HomeFeed>

    @POST("Registration/RegisterBuyer")
    fun postUserDeatails(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body buyerInfo: Buyer): Call<ResponseCode>

    @GET("Registration/GetApartmentList")
    fun getLaucherFeedData(): Call<LauncherFeed>

    @POST("Registration/ManageChef")
    fun postSellerrDeatails(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body postSeller: PostSeller): Call<ResponseCode>

    @POST("Seller/CreateDish")
    fun postNewDishInfo(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body newDish: NewDish): Call<ResponseCode>

    @POST("Registration/AddNewApartmentRequest")
    fun postAddNewApartment(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body newAppartment: NewAppartment): Call<ResponseCode>

    @GET("Checkout/GetOrderHistoryByBuyerId")
    fun getOrderHistoryByBuyerId(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("buyerId") buyerId:  Int): Call<OrderHistory>

    @PUT("Registration/UpdateUserProfile")
    fun postUpdateUser(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body updateUser: UpdateUser): Call<ResponseCode>

    @POST("Checkout/CheckoutByCOD")
    fun postCheckoutByCOD(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body checkoutByCOD: CheckoutByCOD): Call<ResponseCode>

    @POST("Checkout/VerifyStock")
    fun postVerifyStock(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body verifyCheckOutlist: ArrayList<VerifyCheckOut>): Call<ArrayList<VerifyDish>>

    @POST("Checkout/CheckoutByUPI")
    fun postCheckoutByUPI(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body checkoutByUPI: CheckoutByUPI): Call<ResponseCode>

    @PUT("Registration/UpdateBuyerAddress")
    fun postUpdateBuyerAddress(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("userId") userId: Int, @Query("newApartmentId") newApartmentId: Int,@Query("newFlatNumber" ) newFlatNumber:String): Call<ResponseCode>

    @GET("Registration/GetUserDetailByMobileNumber")
    fun getUserDetailByMobileNumber(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("mobilenumber") mobileNumber:  String): Call<UserInfo>


    @POST("Analytic/AddNewReview")
    fun postWriteReview(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body reviews: Array<WriteReviewPost?>): Call<ResponseCode>


    @GET("Analytic/ReviewFeedByDishId")
    fun getReviewByDishId(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("dishId") dishId:  Int): Call<ArrayList<ReadReviewNetwork>>

    @POST("Analytic/AddNewViewed")
    fun postAddNewViewed(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body reviews: ArrayList<PostViewed?>): Call<ResponseCode>


    @GET("Analytic/ViewedFeedByDishId")
    fun getViewedFeedByDishId (@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("dishId") dishId:  Int): Call<ArrayList<ViewedShared>>

    @GET("Analytic/SharedFeedByDishId")
    fun getSharedFeedByDishIdFull (@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("dishId") dishId:  Int): Call<ArrayList<ViewedShared>>

    @POST("Analytic/AddNewShared")
    fun postAddNewShared(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body sharedDish: ArrayList<SharedDish?>): Call<ResponseCode>

    @POST("Analytic/AddNewFollowing")
    fun postAddNewFollowing(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body following: ArrayList<Following>): Call<ResponseCode>

    @PUT("Analytic/UnFollowChef")
    fun putUnFollowChef (@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("userId") userId:  Int,@Query("chefId") chefId:  Int): Call<ResponseCode>

    @POST("Product/ManageDish")
    fun postAddNewDish(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body postAddNewDish: PostAddNewDish): Call<ResponseCode>

    @POST("Product/ManegeActivateDish")
    fun postManegeActivateDish(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body postActiveDishForFirst: PostActiveDishForFirst): Call<ResponseCode>

    @GET("Registration/GetUserStatusById")
    fun getCheckApprovalstatusByUserId(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("userId") userId:  Int): Call<UserStatus>

    @GET("Feed/GetChefHomeFeedByChefId")
    fun getChefDishFeedByChefId(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("chefId") chefId:  Int) : Call<ChefDishFeed>

    @PUT("Product/ActivateTheDishByDishId")
    fun putActivateTheDishByDishId (@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("dishId") dishId:  Int): Call<ResponseCode>

    @PUT("Product/InActivateTheDishByDishId")
    fun putInActivateTheDishByDishId (@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Query("dishId") dishId:  Int): Call<ResponseCode>

    @POST("Product/EditInactiveDish")
    fun postEditInactiveDish(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body  postEditInactiveDish :PostEditInactiveDish): Call<ResponseCode>

    @POST("Product/EditActivatedDish")
    fun postEditActivatedDish(@Header("Accept") accept:String, @Header("Content-Type") content_type:String, @Body  postEditActiveDish :PostEditActiveDish): Call<ResponseCode>
}