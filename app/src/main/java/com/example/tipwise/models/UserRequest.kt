package com.example.tipwise.models

data class UserRequest(
    val email: String,
    val name: String,
    val password: String,
    val contactNumber: String,
    val address: String
)