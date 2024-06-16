package com.example.tipwise.api

import com.example.tipwise.models.NewPassword
import com.example.tipwise.models.NewPasswordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface PasswordAPI {
    @PUT("api/auth/changePassword")
    suspend fun changePassword( @Body newPassword: NewPassword) : Response<NewPasswordResponse>
}