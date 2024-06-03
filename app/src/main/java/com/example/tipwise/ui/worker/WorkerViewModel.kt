package com.example.tipwise.ui.worker

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipwise.models.WorkerRequest
import com.example.tipwise.repository.WorkerRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class WorkerViewModel @Inject constructor( private val workerRepository: WorkerRepository) : ViewModel(){
    val workerLiveData get() = workerRepository.workerLiveData
    val statusLiveData get() = workerRepository.statusLiveData

    fun getWorkers(){
        viewModelScope.launch {
            workerRepository.getWorkers()
        }
    }

    fun createWorkers(workerRequest: WorkerRequest ){
        viewModelScope.launch {
            workerRepository.createWorker(workerRequest)
        }
    }

    fun deleteWorker(workerId: String , imageUrl: String? = null){
        viewModelScope.launch {
            workerRepository.deleteWorker(workerId)
            imageUrl?.let {
                deleteImageFromFirebase(it)
            }
        }
    }

    fun getWorkerById(workerId: String) {
        viewModelScope.launch {
            workerRepository.getWorkerById(workerId)
        }
    }

    fun updateWorker(workerRequest: WorkerRequest , workerId: String , oldImageUrl: String? = null){
        viewModelScope.launch {
            workerRepository.updateWorker(workerRequest, workerId)
            oldImageUrl?.takeIf { workerRequest.photo != it }?.let {
                deleteImageFromFirebase(it)
            }
        }
    }

//    fun uploadImage(uri: Uri) {
//        viewModelScope.launch {
//            workerRepository.uploadImageToFirebase(uri)
//        }
//    }

//    fun uploadImageToFirebase(uri: Uri?, onComplete: (String) -> Unit){
//        if( uri == null ){
//            onComplete("https://placehold.co/600x400?text=.&font=Raleway")
//            return
//        }
//        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
//        val imageRef: StorageReference = storageRef.child("images/${uri.lastPathSegment}")
//        val uploadTask = imageRef.putFile(uri)
//
//        uploadTask.addOnSuccessListener {
//            imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
//                onComplete(downloadUri.toString())
//            }
//        }.addOnFailureListener {
//            // Handle unsuccessful uploads
//            it.printStackTrace()
//            onComplete("https://placehold.co/600x400?text=.&font=Raleway") // Return default URL on failure
//        }
//
//    }

    suspend fun uploadImageToFirebase(uri: Uri?): String? {
        if (uri == null) return null

        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val imageRef: StorageReference = storageRef.child("images/${uri.lastPathSegment}")
        val uploadTask = imageRef.putFile(uri)

        return try {
            val result = uploadTask.await()
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun deleteImageFromFirebase(imageUrl: String) {
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
        storageRef.delete().addOnFailureListener {
            // Log error if needed
            println("Error deleting image: $it")
        }
    }

    fun validateCredentials(
        name: String,
        upiId: String,
        contactNumber: String
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        if ( name.isEmpty() ) {
            result = Pair(false, "Please provide the Name")
        } else if ( upiId.isEmpty()) {
            result = Pair(false, "Please enter a valid UPI ID")
        } else if ( contactNumber.length != 10) {
            result = Pair(false, "Please enter a valid contact number")
        }
        return result
    }

}