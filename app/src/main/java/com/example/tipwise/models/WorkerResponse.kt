package com.example.tipwise.models

data class WorkerResponse(
    val __v: Int,
    val _id: String,
    val bankAccountName: String,
    val bankAccountNumber: String,
    val ifscCode: String,
    val name: String,
    val photo: String,
    val profession: String,
    val upiId: String,
    val userId: String,
    val contactNo: String
)