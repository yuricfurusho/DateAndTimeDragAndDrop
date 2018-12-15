package com.yuricfurusho.dateandtimedraganddrop.repository

import android.util.Log
import com.yuricfurusho.dateandtimedraganddrop.model.DateAndTimeJSON
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by Yuri Furusho on 15/12/18.
 */
interface DateAndTimeApi {

    @GET
    fun getDateAndTime(@Url s: String): Flowable<DateAndTimeJSON>

    companion object {
        private const val BASE_URL = "https://dateandtimeasjson.appspot.com/"
        private var client: OkHttpClient? = null
        private var instance: DateAndTimeApi? = null
        private var mLogger: HttpLoggingInterceptor? = null

        fun getInstance(): DateAndTimeApi {
            val logger: HttpLoggingInterceptor = mLogger ?: HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BODY

            return instance ?: Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(
                    client ?: OkHttpClient.Builder()
                        .addInterceptor(logger)
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DateAndTimeApi::class.java)
        }
    }
}