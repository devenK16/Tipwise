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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tipwise.models.UserRequest
import com.example.tipwise.ui.theme.PacificBridge
import com.example.tipwise.utils.NetworkResult
import com.example.tipwise.utils.ProfileSetupManager
import com.example.tipwise.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController,
    tokenManager: TokenManager,
    profileSetupManager: ProfileSetupManager
) {
    val userResponse by viewModel.userResponseLiveData.observeAsState()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(userResponse) {
        when (val userResponse = userResponse) {
            is NetworkResult.Success -> {
                isLoading = false
                viewModel.saveToken(userResponse.data!!.token)
                val user = userResponse.data.user
                viewModel.saveUserId(user._id)
                Log.d("SignUpUserToken" , user._id)
                navController.navigate("home")

            }
            is NetworkResult.Error -> {
                isLoading = false
                errorMessage = userResponse.message ?: "An unknown error occurred"
            }
            is NetworkResult.Loading -> {
                isLoading = true
            }
            else -> {
                // Do nothing
            }
        }
    }

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
            text = "Create New Account",
            fontSize = 30.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = "Please provide all the required information",
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(48.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username ") },
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
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
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
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text("Contact Number") },
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
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
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
            label = { Text(text = "Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
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
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val (isValid, message) = viewModel.validateCredentials(
                    username,
                    email,
                    password,
                    isLogin = false,
                    contactNumber
                )
                if (isValid) {
                    viewModel.registerUser(UserRequest(email, username, password , contactNumber , address))
                } else {
                    errorMessage = message
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
                text = "Sign Up",
                fontSize = 20.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Already have an account? Login",
                color = PacificBridge,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}