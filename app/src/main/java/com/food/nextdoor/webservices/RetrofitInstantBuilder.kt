package com.food.nextdoor.webservices

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstantBuilder {
    private const val BASE_URL="https://60ced595-ba5a-4f3c-859d-447cda1bc7a6.mock.pstmn.io"
    private val okHTTp:OkHttpClient.Builder=OkHttpClient.Builder()
    private val builder:Retrofit.Builder=Retrofit.Builder().baseUrl(BASE_URL)
        .client(okHTTp.build())
        .addConverterFactory(GsonConverterFactory.create())
    private val retrofit:Retrofit= builder.build();
    fun <T> buildService(serviceType:Class<T>):T?{
       return retrofit.create(serviceType)
    }
}