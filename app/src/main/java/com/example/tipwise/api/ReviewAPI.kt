package com.example.tipwise.api

import com.example.tipwise.models.Review
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewAPI {
    @GET("api/reviews/{userId}")
    suspend fun getReviews(@Path("userId") userId: String) : Response<List<Review>>
}