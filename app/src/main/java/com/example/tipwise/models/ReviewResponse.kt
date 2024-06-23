package com.example.tipwise.models

data class ReviewResponse(
    val currentPage: Int,
    val totalPages: Int,
    val reviews: List<Review>
)

