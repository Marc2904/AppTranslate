package com.example.apptranslate

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @GET("/0.2/languages")
    suspend fun getLanguage(): Response<List<Language>>

    @Headers("Authorization: Bearer 123f8b758ed15d7c2e6d9cc68d1c681e")
    @FormUrlEncoded
    @POST("/0.2/detect")
    suspend fun getTextLanguage(@Field("q")text:String) :Response<DetectionResponse>
}