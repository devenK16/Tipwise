package com.example.tipwise.ui.user

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tipwise.models.UserLoginRequest
import com.example.tipwise.models.UserRequest
import com.example.tipwise.ui.theme.PacificBridge
import com.example.tipwise.utils.NetworkResult
import com.example.tipwise.utils.ProfileSetupManager
import com.example.tipwise.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController,
    tokenManager: TokenManager,
    profileSetupManager : ProfileSetupManager
) {

    val userResponseLiveData by viewModel.userResponseLiveData.observeAsState()

    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val icon = if (passwordVisible)
        Icons.Filled.Visibility
    else
        Icons.Filled.VisibilityOff
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = "Welcome Back!",
            fontSize = 30.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = "Login to access your account",
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(48.dp))
        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text("Email or Contact Number") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PacificBridge,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = PacificBridge,
                unfocusedLabelColor = Color.Black,
                focusedTrailingIconColor = PacificBridge,
                unfocusedTrailingIconColor = Color.Black,
                focusedTextColor = PacificBridge,
                unfocusedTextColor = Color.Black,
                cursorColor = PacificBridge
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PacificBridge,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = PacificBridge,
                unfocusedLabelColor = Color.Black,
                focusedTrailingIconColor = PacificBridge,
                unfocusedTrailingIconColor = Color.Black,
                focusedTextColor = PacificBridge,
                unfocusedTextColor = Color.Black,
                cursorColor = PacificBridge
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )
        Text(
            text = errorMessage,
            modifier = Modifier
                .padding(10.dp),
            color = Color.Red,
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.height(44.dp))
        Button(
            onClick = {
                val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(identifier).matches()
                val isContactNumber = identifier.length == 10 && identifier.all { it.isDigit() }
                if (!isEmail && !isContactNumber) {
                    errorMessage = "Please enter a valid email or mobile number"
                } else {
                    val email = if (isEmail) identifier else ""
                    val contactNumber = if (isContactNumber) identifier else ""
                    Log.d("LoginEmail" , email)
                    Log.d("LoginNumber" , contactNumber)
                    Log.d("LoginPassword" , password)
                    val (isValid, message) = viewModel.validateLoginCredentials(identifier, password)
                    if (isValid) {
                        viewModel.loginUser(UserLoginRequest(identifier,password))
                    } else {
                        errorMessage = message
                    }
                }

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PacificBridge
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = "Login",
                fontSize = 20.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = {
                navController.popBackStack()
                navController.navigate("signup")
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Don't have an account? Register Now",
                color = PacificBridge,
                textAlign = TextAlign.Center
            )
        }
    }

    LaunchedEffect(userResponseLiveData) {
        when (val userResponse = userResponseLiveData) {
            is NetworkResult.Success -> {
                viewModel.saveToken(userResponse.data!!.token)
                viewModel.saveUserId(userResponse.data.user._id)
                Log.d("LoginUserID" , userResponse.data.user._id)
                navController.navigate("home")
            }
            is NetworkResult.Error -> {
                errorMessage = userResponse.message ?: "An unknown error occurred"
            }
            is NetworkResult.Loading -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
            }
            else -> {
                // Do nothing
            }
        }
    }

}