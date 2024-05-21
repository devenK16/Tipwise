package com.example.tipwise.api

import com.example.tipwise.models.User
import com.example.tipwise.models.UserRequest
import com.example.tipwise.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersAPI {
    @POST("api/auth/signup")
    suspend fun signUp(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("api/auth/signin")
    suspend fun signIn(@Body userRequest: UserRequest): Response<UserResponse>
    @GET("api/users/{userId}")
    suspend fun getUser( @Path("userId") userId : String) : Response<User>

    @PUT("api/users/{userId}")
    suspend fun updateUser( @Path("userId") userId: String , @Body userRequest: UserRequest ) : Response<UserResponse>
}