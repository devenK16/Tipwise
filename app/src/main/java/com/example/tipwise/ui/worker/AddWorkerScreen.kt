package com.example.tipwise.ui.worker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.tipwise.models.WorkerRequest
import com.example.tipwise.models.WorkerResponse
import com.example.tipwise.ui.theme.PacificBridge
import com.example.tipwise.utils.NetworkResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkerScreen(
    viewModel: WorkerViewModel = hiltViewModel(),
    navController: NavHostController,
    workerId: String? = null
) {
    val workerResult by viewModel.workerLiveData.observeAsState()

    // Initialize state variables
    var name by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }
    var upiId by remember { mutableStateOf("") }
    var bankAccountName by remember { mutableStateOf("") }
    var bankAccountNumber by remember { mutableStateOf("") }
    var ifscCode by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Fetch worker data if workerId is provided
    LaunchedEffect(workerId) {
        if (workerId != null) {
            viewModel.getWorkerById(workerId)
        }
    }

    // Handle workerResult and update state variables accordingly
    LaunchedEffect(workerResult) {
        if (workerResult is NetworkResult.Success && workerId != null) {
            (workerResult as NetworkResult.Success<List<WorkerResponse>>).data?.firstOrNull { it._id == workerId }?.let { worker ->
                name = worker.name
                profession = worker.profession
                upiId = worker.upiId
                bankAccountName = worker.bankAccountName
                bankAccountNumber = worker.bankAccountNumber
                ifscCode = worker.ifscCode
                imageUri = worker.photo?.let { Uri.parse(it) }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (workerId != null) "Update Worker" else "Add Worker") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageUri != null) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable { imageUri = null }
                ) {
                    Image(
                        painter = rememberImagePainter(imageUri),
                        contentDescription = "Selected Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            } else {
                ImageUploadButton(
                    onImageSelect = { imageUri = it },
                    modifier = Modifier
                        .size(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
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
                value = profession,
                onValueChange = { profession = it },
                label = { Text("Profession") },
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
                value = upiId,
                onValueChange = { upiId = it },
                label = { Text("UPI ID") },
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
                value = bankAccountName,
                onValueChange = { bankAccountName = it },
                label = { Text("Bank Account Name") },
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
                value = bankAccountNumber,
                onValueChange = { bankAccountNumber = it },
                label = { Text("Bank Account Number") },
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
                value = ifscCode,
                onValueChange = { ifscCode = it },
                label = { Text("IFSC Code") },
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
            Spacer(modifier = Modifier.height(44.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        val workerRequest = WorkerRequest(
                            name = name,
                            profession = profession,
                            bankAccountName = bankAccountName,
                            bankAccountNumber = bankAccountNumber,
                            ifscCode = ifscCode,
                            photo = imageUri?.toString() ?: "",
                            upiId = upiId
                        )
                        if (workerId != null) {
                            viewModel.updateWorker(workerRequest, workerId)
                        } else {
                            viewModel.createWorkers(workerRequest)
                        }
                        navController.navigateUp()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PacificBridge
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp)
                ) {
                    Text(
                        text = if (workerId != null) "Update" else "Add",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                if (workerId != null) {
                    Button(
                        onClick = {
                            viewModel.deleteWorker(workerId)
                            navController.navigateUp()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp)
                    ) {
                        Text(
                            text = "Delete",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageUploadButton(
    onImageSelect: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                onImageSelect(it)
            }
        }
    )

    CircleWithText(
        text = "Upload",
        modifier = modifier,
        onClick = {
            launcher.launch("image/*")
        }
    )
}

@Composable
fun CircleWithText(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .background(color = Color.LightGray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }
}
