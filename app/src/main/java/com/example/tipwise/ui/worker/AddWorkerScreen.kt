package com.example.tipwise.ui.worker

import android.content.Context
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.tipwise.models.WorkerRequest
import com.example.tipwise.models.WorkerResponse
import com.example.tipwise.ui.theme.PacificBridge
import com.example.tipwise.ui.theme.TipzonnBlack
import com.example.tipwise.ui.theme.TipzonnLight
import com.example.tipwise.utils.NetworkResult
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

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
    var imageUrl1 by remember { mutableStateOf("") }
    var contactNo by remember { mutableStateOf("") }
    var oldImageUrl by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Initialize error state variables
    var nameError by remember { mutableStateOf(false) }
    var professionError by remember { mutableStateOf(false) }
    var upiIdError by remember { mutableStateOf(false) }
    var contactNoError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Fetch worker data if workerId is provided
    LaunchedEffect(workerId) {
        if (workerId != null) {
            viewModel.getWorkerById(workerId)
        }
    }

    // Handle workerResult and update state variables accordingly
    LaunchedEffect(workerResult) {
        if (workerResult is NetworkResult.Success && workerId != null) {
            (workerResult as NetworkResult.Success<List<WorkerResponse>>).data?.firstOrNull { it._id == workerId }
                ?.let { worker ->
                    name = worker.name
                    profession = worker.profession
                    upiId = worker.upiId
                    bankAccountName = worker.bankAccountName
                    bankAccountNumber = worker.bankAccountNumber
                    ifscCode = worker.ifscCode
                    imageUrl1 = worker.photo
                    contactNo = worker.contactNo
                }
        }
    }

    fun copyUriToFile(context: Context, uri: Uri): File {
        val destinationFile = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(destinationFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return destinationFile
    }

    suspend fun compressImage(context: Context, uri: Uri): Uri {
        return withContext(Dispatchers.IO) {
            val actualImageFile = copyUriToFile(context, uri)
            val compressedImageFile = Compressor.compress(context, actualImageFile) {
                default()
            }
            Uri.fromFile(compressedImageFile)
        }
    }


    fun saveWorker(imageUrl: String) {
        val workerRequest = WorkerRequest(
            name = name,
            profession = profession,
            bankAccountName = bankAccountName,
            bankAccountNumber = bankAccountNumber,
            ifscCode = ifscCode,
            photo = imageUrl,
            upiId = upiId,
            contactNo = contactNo
        )
        if (workerId != null) {
            viewModel.updateWorker(workerRequest, workerId)
        } else {
            viewModel.createWorkers(workerRequest)
        }
        navController.navigateUp()
    }

    // Handle form submission
    fun handleFormSubmission() {
        // Reset error states
        nameError = name.isEmpty()
        professionError = profession.isEmpty()
        upiIdError = upiId.isEmpty()
        contactNoError = contactNo.isEmpty()

        if (nameError || professionError || upiIdError || contactNoError) {
            return
        }

        viewModel.viewModelScope.launch {
            val newImageUrl: String? = if (imageUri != null) {
                val compressedUri = compressImage(context, imageUri!!)
                viewModel.uploadImageToFirebase(compressedUri)
            } else {
                null
            }

            if (workerId != null) {
                if (newImageUrl != null && newImageUrl != imageUrl1) {
                    viewModel.updateWorker(
                        WorkerRequest(
                            name = name,
                            profession = profession,
                            bankAccountName = bankAccountName,
                            bankAccountNumber = bankAccountNumber,
                            ifscCode = ifscCode,
                            photo = newImageUrl,
                            upiId = upiId,
                            contactNo = contactNo
                        ),
                        workerId,
                        oldImageUrl = imageUrl1
                    )

                    navController.navigateUp()
                } else {
                    viewModel.updateWorker(
                        WorkerRequest(
                            name = name,
                            profession = profession,
                            bankAccountName = bankAccountName,
                            bankAccountNumber = bankAccountNumber,
                            ifscCode = ifscCode,
                            photo = imageUrl1,
                            upiId = upiId,
                            contactNo = contactNo
                        ),
                        workerId
                    )
                    navController.navigateUp()
                }
            } else {
                saveWorker(newImageUrl ?: imageUrl1)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (workerId != null) "Update Worker" else "Add Worker",
                        color = TipzonnBlack
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = TipzonnBlack
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
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
                    viewModel = viewModel,
                    onImageSelect = { imageUri = it },
                    imageUrl = imageUrl1,
                    modifier = Modifier
                        .size(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                isError = nameError,
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

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = profession,
                onValueChange = { profession = it },
                label = { Text("Profession") },
                isError = professionError,
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

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = upiId,
                onValueChange = { upiId = it },
                label = { Text("UPI ID") },
                isError = upiIdError,
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

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = contactNo,
                onValueChange = { contactNo = it },
                label = { Text("Contact No.") },
                isError = contactNoError,
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
            Text(
                text = errorMessage,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                color = Color.Red,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        val (isValid, message) = viewModel.validateCredentials(
                            name,
                            upiId,
                            contactNo
                        )
                        if (isValid) {
                            handleFormSubmission()
                        } else {
                            errorMessage = message
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TipzonnLight
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
                            viewModel.deleteWorker(workerId, imageUrl1)
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
    viewModel: WorkerViewModel,
    onImageSelect: (Uri) -> Unit,
    imageUrl: String,
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

    if (imageUrl.isEmpty()) { // Check if imageUrl is empty
        CircleWithText(
            text = "Upload Image",
            modifier = modifier,
            onClick = {
                launcher.launch("image/*")
            }
        )
    } else {
        Box(
            modifier = modifier
                .size(200.dp)
                .clip(CircleShape)
                .clickable {
                    launcher.launch("image/*")
                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Selected Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }
    }
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
                color = TipzonnBlack,
                fontSize = 20.sp
            )
        }
    }
}
