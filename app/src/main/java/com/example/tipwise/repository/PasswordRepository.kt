package com.example.tipwise.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tipwise.api.PasswordAPI
import com.example.tipwise.api.WorkersAPI
import com.example.tipwise.models.NewPassword
import com.example.tipwise.models.NewPasswordResponse
import com.example.tipwise.models.WorkerResponse
import com.example.tipwise.utils.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class PasswordRepository @Inject constructor(private val passwordAPI: PasswordAPI) {
    private val _passwordLiveData = MutableLiveData<NetworkResult<String>>()
    val passwordLiveData : LiveData<NetworkResult<String>>
        get() = _passwordLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun changePassword( newPassword: NewPassword ){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = passwordAPI.changePassword(newPassword)
        handleResponse(response , "Password Changed")
    }

        private fun handleResponse(response: Response<NewPasswordResponse>, message: String) {
        if (response.isSuccessful) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}