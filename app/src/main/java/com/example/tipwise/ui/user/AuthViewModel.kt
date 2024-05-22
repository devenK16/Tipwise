package com.example.tipwise.ui.user

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipwise.models.User
import com.example.tipwise.models.UserLoginRequest
import com.example.tipwise.models.UserRequest
import com.example.tipwise.models.UserResponse
import com.example.tipwise.repository.UserRepository
import com.example.tipwise.utils.NetworkResult
import com.example.tipwise.utils.ProfileSetupManager
import com.example.tipwise.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val profileSetupManager: ProfileSetupManager
) : ViewModel() {
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData
    val userLiveData: LiveData<NetworkResult<User>>
        get() = userRepository.userLivedata

    @Inject
    lateinit var tokenManager: TokenManager

    private val _isProfileSetupCompleted = MutableStateFlow(false)

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userLoginRequest: UserLoginRequest) {
        viewModelScope.launch {
            userRepository.loginUser(userLoginRequest)
        }
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            userRepository.getUser(userId)
        }
    }

    fun updateUser(userId: String, userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.updateUser(userId, userRequest)
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch(IO) {
            tokenManager.saveToken(token)
        }
    }


    fun saveUserId(userId: String) {

        Log.d("viewmodelUserID" , userId)
        viewModelScope.launch(IO) {
            profileSetupManager.saveUserId(userId)
        }
    }

    suspend fun getUserId(): String? {
        return profileSetupManager.getUserId()
    }

    fun validateCredentials(
        username: String,
        emailAddress: String,
        password: String,
        isLogin: Boolean,
        contactNumber: String = ""
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        if ((!isLogin && (username.isEmpty() || contactNumber.isEmpty())) || emailAddress.isEmpty() || password.isEmpty()) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Please provide a valid email")
        } else if (password.length <= 5) {
            result = Pair(false, "Password length should be greater than 5")
        } else if (!isLogin && contactNumber.length != 10) {
            result = Pair(false, "Please enter a valid contact number")
        }
        return result
    }

    fun validateLoginCredentials(identifier: String, password: String): Pair<Boolean, String> {
        if (identifier.isBlank() || password.isBlank()) {
            return Pair(false, "Please provide the credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(identifier).matches() && !Patterns.PHONE.matcher(identifier).matches()) {
            return Pair(false, "Please provide a valid email or phone number")
        } else if (password.length <= 5) {
            return Pair(false, "Password length should be greater than 5")
        }
        return Pair(true, "")
    }
}