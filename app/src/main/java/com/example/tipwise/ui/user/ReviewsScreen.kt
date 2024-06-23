package com.example.tipwise.ui.user

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.tipwise.models.Review
import com.example.tipwise.ui.theme.TipzonnBlack
import com.example.tipwise.ui.theme.TipzonnLightBackground
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewsScreen(
    viewModel: ReviewViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val reviews = viewModel.reviews.collectAsLazyPagingItems()

    LaunchedEffect(reviews.loadState) {
        val refresh = reviews.loadState.refresh
        val append = reviews.loadState.append

        Log.d("ReviewsPagination", "LoadState changed - Refresh: $refresh, Append: $append")
        Log.d("ReviewsPagination", "Total items loaded: ${reviews.itemCount}")
    }

    LazyColumn {
        items(
            count = reviews.itemCount,
            key = { index -> index }
        ) { index ->
            val review = reviews[index]
            if (review != null) {
                Log.d("ReviewsPagination", "Displaying review at index $index: ${review.reviewText}")
                ReviewCard(review = review)
            } else {
                Log.d("ReviewsPagination", "Null review at index $index")
            }
        }

        reviews.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Log.d("ReviewsPagination", "Refresh loading")
                        LoadingItem()
                    }
                }
                loadState.append is LoadState.Loading -> {
                    item {
                        Log.d("ReviewsPagination", "Append loading")
                        LoadingItem()
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = reviews.loadState.refresh as LoadState.Error
                    item {
                        Log.e("ReviewsPagination", "Refresh error: ${e.error.message}")
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            onRetryClick = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = reviews.loadState.append as LoadState.Error
                    item {
                        Log.e("ReviewsPagination", "Append error: ${e.error.message}")
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            onRetryClick = { retry() }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun LoadingItem() {
    // Implement a loading indicator
}

@Composable
fun ErrorItem(message: String, onRetryClick: () -> Unit) {
    // Implement an error message with retry button
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewCard(review: Review) {
    val formattedDate = remember(review.date) {
        formatDateString(review.date)
    }
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = TipzonnLightBackground
        )
    ) {
        Column (
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = review.reviewText,
                color = TipzonnBlack,
                fontSize = 18.sp
            )
            // Rating bar based on rating
            RatingBar(
                modifier = Modifier.size(20.dp),
                rating = review.rating.toDouble(),
                starsColor = Color.Yellow
            )
            Text(
                text = formattedDate,
                color = TipzonnBlack,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    onRatingChanged: (Double) -> Unit = {},
    starsColor: Color = Color.Yellow
) {
    var isHalfStar = (rating % 1) != 0.0

    Row {
        for (index in 1..rating.toInt()) {
            Icon(
                imageVector =
                if (index <= rating) {
                    Icons.Rounded.Star
                } else {
                    if (isHalfStar) {
                        isHalfStar = false
                        Icons.Rounded.StarHalf
                    } else {
                        Icons.Rounded.StarOutline
                    }
                },
                contentDescription = null,
                tint = starsColor,
                modifier = modifier
                    .clickable { onRatingChanged(index.toDouble()) }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateString(dateString: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a")
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        Log.e("ReviewCard", "Error parsing date: $dateString", e)
        dateString // fallback to original date string if parsing fails
    }
}