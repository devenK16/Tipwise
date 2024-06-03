package com.example.tipwise.models

data class WorkerRequest(
    val bankAccountName: String,
    val bankAccountNumber: String,
    val ifscCode: String,
    val name: String,
    val photo: String,
    val profession: String,
    val upiId: String,
    val contactNo: String
)