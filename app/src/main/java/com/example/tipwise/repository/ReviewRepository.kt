package com.example.tipwise.repository

import ReviewPagingSource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tipwise.api.ReviewAPI
import com.example.tipwise.models.Review
import com.example.tipwise.models.ReviewResponse
import com.example.tipwise.models.User
import com.example.tipwise.models.WorkerResponse
import com.example.tipwise.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewAPI: ReviewAPI
) {
    fun getReviewsPagingFlow(userId: String): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 20  // This should be the same as pageSize
            ),
            pagingSourceFactory = { ReviewPagingSource(reviewAPI, userId) }
        ).flow
    }
}