package com.example.quoteapp.Interface

import android.media.session.MediaSession.Token
import com.example.quoteapp.ModelClass.MOdelClassFactResponse
import com.example.quoteapp.ModelClass.MOdelClassQuotesResponse
import com.example.quoteapp.ModelClass.ModelCLassSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterFace {
    @GET("quotes")
    fun Quotes(@Header("X-API-TOKEN") token: String,@Query("date") date:String):retrofit2.Call<MOdelClassQuotesResponse>

    @GET("facts")
    fun Fact(@Header("X-API-TOKEN") token: String):retrofit2.Call<MOdelClassFactResponse>

    @GET("search")
    fun Search(@Header("X-API-TOKEN") token: String,@Query("search-text") text:String,@Query("page") page:Int):retrofit2.Call<ModelCLassSearchResponse>
}