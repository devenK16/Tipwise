package com.example.tipwise.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipwise.repository.ReviewRepository
import com.example.tipwise.utils.ProfileSetupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val profileSetupManager: ProfileSetupManager
) : ViewModel() {

    val reviewLiveData get() = reviewRepository.reviewLiveData
    val statusLiveData get() = reviewRepository.statusLiveData

    suspend fun getUserId(): String? {
        return profileSetupManager.getUserId()
    }
    suspend fun getReviews(userId: String) {
        viewModelScope.launch {
            reviewRepository.getReviews(userId)
        }
    }

}