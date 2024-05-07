package com.example.tipwise.api

import com.example.tipwise.models.UserRequest
import com.example.tipwise.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersAPI {
    @POST("/auth/signup")
    suspend fun signUp(userRequest: UserRequest): Response<UserResponse>

    @POST("/auth/signin")
    suspend fun signIn(@Body userRequest: UserRequest): Response<UserResponse>
}