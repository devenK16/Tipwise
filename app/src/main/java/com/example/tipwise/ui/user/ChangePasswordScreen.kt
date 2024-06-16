package com.example.tipwise.ui.user

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tipwise.models.NewPassword
import com.example.tipwise.ui.theme.TipzonnBlack
import com.example.tipwise.ui.theme.TipzonnLight
import com.example.tipwise.utils.NetworkResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavHostController,
    viewModel: PasswordViewModel = hiltViewModel()
) {
    val statusLiveData by viewModel.userLiveData.observeAsState()
    val context = LocalContext.current

    val newPassword = rememberSaveable { mutableStateOf("") }
    val confirmPassword = rememberSaveable { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Change Password",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TipzonnBlack
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(36.dp))

        OutlinedTextField(
            value = newPassword.value,
            onValueChange = { newPassword.value = it },
            label = { Text("Enter New Password") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = TipzonnLight,
                unfocusedBorderColor = TipzonnBlack,
                focusedLabelColor = TipzonnLight,
                unfocusedLabelColor = TipzonnBlack,
                focusedTrailingIconColor = TipzonnLight,
                unfocusedTrailingIconColor = TipzonnBlack,
                focusedTextColor = TipzonnLight,
                unfocusedTextColor = TipzonnBlack,
                cursorColor = TipzonnLight
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = { Text("Confirm New Password") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = TipzonnLight,
                unfocusedBorderColor = TipzonnBlack,
                focusedLabelColor = TipzonnLight,
                unfocusedLabelColor = TipzonnBlack,
                focusedTrailingIconColor = TipzonnLight,
                unfocusedTrailingIconColor = TipzonnBlack,
                focusedTextColor = TipzonnLight,
                unfocusedTextColor = TipzonnBlack,
                cursorColor = TipzonnLight
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 10.dp)
        )

        Text(
            text = errorMessage,
            modifier = Modifier
                .padding(10.dp),
            color = Color.Red,
            fontSize = 10.sp
        )

        Button(
            onClick = {
                if (newPassword.value.trim() == confirmPassword.value.trim()) {
                    viewModel.changePassword(NewPassword(newPassword = newPassword.value.trim()))
                } else {
                    errorMessage = "Passwords do not match"
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = TipzonnLight
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 10.dp)
        ) {
            Text(
                text = "Submit",
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = TipzonnLight)
        }
    }

    LaunchedEffect(statusLiveData) {
        when (val status = statusLiveData) {
            is NetworkResult.Success -> {
                isLoading = false
                Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
                navController.navigate("home")
            }
            is NetworkResult.Error -> {
                isLoading = false
                errorMessage = status.message ?: "An unknown error occurred"
            }
            is NetworkResult.Loading -> {
                isLoading = true
            }
            else -> {
                isLoading = false
            }
        }
    }
}