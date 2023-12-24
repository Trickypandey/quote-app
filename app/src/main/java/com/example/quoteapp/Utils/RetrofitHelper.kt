package com.example.quoteapp.Utils

import com.example.quoteapp.Interface.ApiInterFace
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class
RetrofitHelper() {

    fun retrofitobj(): ApiInterFace {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY


        val client = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).addInterceptor(logging).build()

        val retro = Retrofit.Builder().baseUrl("https://shopblack366.com/daily_quotes_mobile_app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return  retro.create(ApiInterFace::class.java)

    }
}