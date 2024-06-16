package com.example.tipwise.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipwise.api.PasswordAPI
import com.example.tipwise.models.NewPassword
import com.example.tipwise.models.User
import com.example.tipwise.models.UserResponse
import com.example.tipwise.repository.PasswordRepository
import com.example.tipwise.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor( private val passwordRepository: PasswordRepository) : ViewModel() {
    val userResponseLiveData: LiveData<NetworkResult<String>>
        get() = passwordRepository.passwordLiveData
    val userLiveData: LiveData<NetworkResult<String>>
        get() = passwordRepository.statusLiveData

    fun changePassword( newPassword: NewPassword ){
        viewModelScope.launch {
            passwordRepository.changePassword(newPassword)
        }
    }

}