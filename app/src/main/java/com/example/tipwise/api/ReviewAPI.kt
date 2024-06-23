package com.example.tipwise.api

import com.example.tipwise.models.Review
import com.example.tipwise.models.ReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewAPI {
    @GET("api/reviews/{userId}")
    suspend fun getReviews(
        @Path("userId") userId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ReviewResponse>
}