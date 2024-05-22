package com.example.tipwise.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tipwise.api.UsersAPI
import com.example.tipwise.models.User
import com.example.tipwise.models.UserLoginRequest
import com.example.tipwise.models.UserRequest
import com.example.tipwise.models.UserResponse
import com.example.tipwise.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val usersAPI: UsersAPI) {
    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    private val _userLiveData = MutableLiveData<NetworkResult<User>>()
    val userLivedata : LiveData<NetworkResult<User>>
        get() = _userLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
//        val response = usersAPI.signUp(userRequest)
//        handleResponse(response)
        try {
            val response = usersAPI.signUp(userRequest)
            handleResponse(response)
        } catch (e: Exception) {
            e.printStackTrace()
            _userResponseLiveData.postValue(NetworkResult.Error("Exception occurred: ${e.message}"))
        }
    }

    suspend fun loginUser(userLoginRequest: UserLoginRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = usersAPI.signIn(userLoginRequest)
        handleResponse(response)
    }

    suspend fun getUser(userId: String) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = usersAPI.getUser(userId)
        Log.d("UserRepository", "getUser response: $response")
        handleResponseUser(response)
    }

    suspend fun updateUser(userId: String, userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = usersAPI.updateUser(userId, userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            if (errorBody != null) {
                try {
                    // Check if the response is an HTML error page
                    if (errorBody.trim().startsWith("<!DOCTYPE")) {
                        _userResponseLiveData.postValue(NetworkResult.Error("Server returned an HTML error page. Possible server error or incorrect endpoint."))
                    } else {
                        // Try to parse the error body as JSON
                        val errorObj = JSONObject(errorBody)
                        val errorMessage = errorObj.optString("message", "Unknown error")
                        _userResponseLiveData.postValue(NetworkResult.Error(errorMessage))
                    }
                } catch (e: Exception) {
                    // If parsing fails, log the error and provide a generic message
                    e.printStackTrace()
                    _userResponseLiveData.postValue(NetworkResult.Error("Error parsing response: ${e.message}"))
                }
            } else {
                _userResponseLiveData.postValue(NetworkResult.Error("No error body provided"))
            }
        }
    }

    private fun handleResponseUser(response: Response<User>) {
        if (response.isSuccessful && response.body() != null) {
            _userLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string()
            if (errorBody != null) {
                try {
                    // Check if the response is an HTML error page
                    if (errorBody.trim().startsWith("<!DOCTYPE")) {
                        _userLiveData.postValue(NetworkResult.Error("Server returned an HTML error page. Possible server error or incorrect endpoint."))
                    } else {
                        // Try to parse the error body as JSON
                        val errorObj = JSONObject(errorBody)
                        val errorMessage = errorObj.optString("message", "Unknown error")
                        _userLiveData.postValue(NetworkResult.Error(errorMessage))
                    }
                } catch (e: Exception) {
                    // If parsing fails, log the error and provide a generic message
                    e.printStackTrace()
                    _userLiveData.postValue(NetworkResult.Error("Error parsing response: ${e.message}"))
                }
            } else {
                _userLiveData.postValue(NetworkResult.Error("No error body provided"))
            }
        }
    }
}