package com.food.nextdoor.webservices

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.sql.Time
import java.util.concurrent.TimeUnit
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



object RetrofitInstantBuilder {
    private const val BASE_URL="http://192.168.2.186:8080/api/"

    var client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30,TimeUnit.SECONDS)
        .build()
    private val builder:Retrofit.Builder=Retrofit.Builder().baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
    private val retrofit:Retrofit= builder.build();
    fun <T> buildService(serviceType:Class<T>):T?{
       return retrofit.create(serviceType)
    }
}