package com.example.tipwise.ui.user

import ReviewPagingSource
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.tipwise.models.Review
import com.example.tipwise.repository.ReviewRepository
import com.example.tipwise.utils.ProfileSetupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val profileSetupManager: ProfileSetupManager
) : ViewModel() {

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    val reviews: Flow<PagingData<Review>> = userId.filterNotNull().flatMapLatest { id ->
        Log.d("ReviewViewModel", "Fetching reviews for user: $id")
        reviewRepository.getReviewsPagingFlow(id)
    }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            val id = profileSetupManager.getUserId()
            Log.d("ReviewViewModel", "Initial user ID: $id")
            _userId.value = id
        }
    }
}