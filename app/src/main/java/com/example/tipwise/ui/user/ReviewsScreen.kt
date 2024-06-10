package com.example.tipwise.ui.user

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tipwise.models.Review

@Composable
fun ReviewsScreen(
    viewModel: ReviewViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val reviewResponse by viewModel.reviewLiveData.observeAsState()
    val reviews = reviewResponse?.data ?: emptyList()
    val userIdState = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val userId = viewModel.getUserId()
        if (userId != null) {
            userIdState.value = userId
            Log.d("ReviewUserIDTestValue", userId)
        } else {
            Log.e("ReviewsScreen", "User ID is null, cannot fetch reviews")
        }
    }

    val currentUserId by rememberUpdatedState(userIdState.value)

    LaunchedEffect(currentUserId) {
        currentUserId?.let {
            viewModel.getReviews(it)
        }
    }

    if (userIdState.value != null) {
        LazyColumn {
            items(reviews) { review ->
                ReviewCard(review = review)
            }
        }
    } else {
        // Handle the case where userId is null (e.g., show an error message)
        Text(text = "Unable to load reviews. Log out and Log in again")
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card {
        Column {
            Text(text = review.reviewText)
            Text(text = review.date)
        }
    }
}