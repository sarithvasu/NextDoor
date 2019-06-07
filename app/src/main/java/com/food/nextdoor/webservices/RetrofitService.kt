package com.food.nextdoor.webservices


import com.food.nextdoor.model.HomeFeed
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {
    @GET("home")
    fun getBuyerHomeData(): Call<HomeFeed>
}