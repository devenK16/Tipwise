package com.example.tipwise.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tipwise.models.UserResponse
import com.example.tipwise.ui.theme.PacificBridge
import com.example.tipwise.utils.ProfileSetupManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    authToken: String?,
    viewModel: AuthViewModel = hiltViewModel(),
    profileSetupManager: ProfileSetupManager
) {
    val userResponse by viewModel.userResponseLiveData.observeAsState()

    // Call getUser when the screen is initialized
    LaunchedEffect(key1 = true) {
        viewModel.getUser(userId = "your_user_id")
    }

    // Bind user details to variables
    var email = userResponse?.data?.user?.email ?: ""
    var restaurantName = userResponse?.data?.user?.name ?: ""
    var address = userResponse?.data?.user?.address ?: ""
    var contactNumber = userResponse?.data?.user?.contactNo ?: ""

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
                // Save profile data
                profileSetupManager.setProfileSetupCompleted(true)
                navController.navigate("home")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Continue")
        }
    }
}