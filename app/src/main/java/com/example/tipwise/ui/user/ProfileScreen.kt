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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tipwise.models.UserResponse
import com.example.tipwise.ui.theme.PacificBridge
import com.example.tipwise.utils.NetworkResult
import com.example.tipwise.utils.ProfileSetupManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel(),
    profileSetupManager: ProfileSetupManager
) {
    val userResponseLiveData by viewModel.userResponseLiveData.observeAsState()
    val userLiveData by viewModel.userLiveData.observeAsState()

    // Get the user ID from the AuthViewModel
    val userId by produceState<String?>(initialValue = null) {
        value = viewModel.getUserId()
        value?.let { Log.d("ProfileUserIDTestValue" , it) }
    }

    // Call getUser with the userId when the screen is initialized
    LaunchedEffect(key1 = userId) {
        userId?.let { Log.d("ProfileUserIDTestuserId" , it) }
        if (userId != null) {
            viewModel.getUser(userId!!)
        }
    }


    userId?.let { Log.d("ProfileUserIDTest" , it) }

    var email by remember { mutableStateOf("") }
    var restaurantName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PacificBridge,
                unfocusedBorderColor =  Color.Black,
                focusedLabelColor = PacificBridge,
                unfocusedLabelColor =  Color.Black,
                focusedTrailingIconColor = PacificBridge,
                unfocusedTrailingIconColor =  Color.Black,
                focusedTextColor = PacificBridge,
                unfocusedTextColor = Color.Black,
                cursorColor = PacificBridge
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = restaurantName,
            onValueChange = { restaurantName = it },
            label = { Text(text = "Restaurant Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PacificBridge,
                unfocusedBorderColor =  Color.Black,
                focusedLabelColor = PacificBridge,
                unfocusedLabelColor =  Color.Black,
                focusedTrailingIconColor = PacificBridge,
                unfocusedTrailingIconColor =  Color.Black,
                focusedTextColor = PacificBridge,
                unfocusedTextColor = Color.Black,
                cursorColor = PacificBridge
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(text = "Address") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PacificBridge,
                unfocusedBorderColor =  Color.Black,
                focusedLabelColor = PacificBridge,
                unfocusedLabelColor =  Color.Black,
                focusedTrailingIconColor = PacificBridge,
                unfocusedTrailingIconColor =  Color.Black,
                focusedTextColor = PacificBridge,
                unfocusedTextColor = Color.Black,
                cursorColor = PacificBridge
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text(text = "Contact Number") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = PacificBridge,
                unfocusedBorderColor =  Color.Black,
                focusedLabelColor = PacificBridge,
                unfocusedLabelColor =  Color.Black,
                focusedTrailingIconColor = PacificBridge,
                unfocusedTrailingIconColor =  Color.Black,
                focusedTextColor = PacificBridge,
                unfocusedTextColor = Color.Black,
                cursorColor = PacificBridge
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("home")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Continue")
        }
    }

    LaunchedEffect(userLiveData) {
        when (val userResponse = userLiveData) {
            is NetworkResult.Success -> {
                val userData = userResponse.data
                if (userData != null) {
                    email = userData.email ?: ""
                    restaurantName = userData.name ?: ""
                    address = userData.address ?: ""
                    contactNumber = userData.contactNo ?: ""
                }

                Log.d("ProfileUserIDTestEmail" , userResponse.data.toString() )
            }

            is NetworkResult.Error -> {
//            errorMessage = userResponse.message ?: "An unknown error occurred"
            }

            is NetworkResult.Loading -> {

            }

            else -> {
                // Do nothing
            }
        }
    }

}