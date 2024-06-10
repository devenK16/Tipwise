package com.example.tipwise.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tipwise.api.ReviewAPI
import com.example.tipwise.models.Review
import com.example.tipwise.models.User
import com.example.tipwise.models.WorkerResponse
import com.example.tipwise.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class ReviewRepository @Inject constructor( private val reviewAPI: ReviewAPI ){
    private val _reviewLiveData = MutableLiveData<NetworkResult<List<Review>>>()
    val reviewLiveData : LiveData<NetworkResult<List<Review>>>
        get() = _reviewLiveData


    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getReviews(userId: String) {
        _reviewLiveData.postValue(NetworkResult.Loading())
        val response = reviewAPI.getReviews(userId)
        handleReviewResponse(response)
    }

//    private fun handleResponse(response: Response<Unit>, message: String) {
//        if (response.isSuccessful) {
//            _statusLiveData.postValue(NetworkResult.Success(message))
//        } else {
//            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
//        }
//    }

    private fun handleReviewResponse(response: Response<List<Review>>) {
        if (response.isSuccessful && response.body() != null) {
            _reviewLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _reviewLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _reviewLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}