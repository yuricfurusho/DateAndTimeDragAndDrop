package com.yuricfurusho.dateandtimedraganddrop.repository

import android.util.Log
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by Yuri Furusho on 15/12/18.
 */
interface DateAndTimeApi {

    @GET("")
    fun getDateAndTime(): Observable<Any>

    companion object {
        private const val BASE_URL = "https://dateandtimeasjson.appspot.com/"

        fun create(): DateAndTimeApi {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DateAndTimeApi::class.java)
        }
    }
}