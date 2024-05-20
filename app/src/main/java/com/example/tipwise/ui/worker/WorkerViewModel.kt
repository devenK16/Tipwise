package com.example.tipwise.ui.worker

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipwise.models.WorkerRequest
import com.example.tipwise.repository.WorkerRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    fun deleteWorker(workerId: String){
        viewModelScope.launch {
            workerRepository.deleteWorker(workerId)
        }
    }

    fun getWorkerById(workerId: String) {
        viewModelScope.launch {
            workerRepository.getWorkerById(workerId)
        }
    }

    fun updateWorker(workerRequest: WorkerRequest , workerId: String){
        viewModelScope.launch {
            workerRepository.updateWorker(workerRequest, workerId)
        }
    }

//    fun uploadImage(uri: Uri) {
//        viewModelScope.launch {
//            workerRepository.uploadImageToFirebase(uri)
//        }
//    }

    fun uploadImageToFirebase(uri: Uri?, onComplete: (String) -> Unit){
        if( uri == null ){
            onComplete("https://placehold.co/600x400?text=.&font=Raleway")
            return
        }
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val imageRef: StorageReference = storageRef.child("images/${uri.lastPathSegment}")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                onComplete(downloadUri.toString())
            }
        }.addOnFailureListener {
            // Handle unsuccessful uploads
            it.printStackTrace()
            onComplete("https://placehold.co/600x400?text=.&font=Raleway") // Return default URL on failure
        }

    }

}