package com.example.tipwise.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tipwise.api.WorkersAPI
import com.example.tipwise.models.WorkerRequest
import com.example.tipwise.models.WorkerResponse
import com.example.tipwise.utils.NetworkResult
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class WorkerRepository @Inject constructor( private val workersAPI: WorkersAPI ) {
    private val _workerLiveData = MutableLiveData<NetworkResult<List<WorkerResponse>>>()
    val workerLiveData : LiveData<NetworkResult<List<WorkerResponse>>>
        get() = _workerLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData : LiveData<NetworkResult<String>>
        get() = _statusLiveData
    private val storageReference = Firebase.storage.reference

    suspend fun getWorkers() {
        _workerLiveData.postValue(NetworkResult.Loading())
        val response = workersAPI.getWorkers()
        if (response.isSuccessful && response.body() != null) {
            _workerLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _workerLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _workerLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun createWorker(workerRequest: WorkerRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = workersAPI.addWorker(workerRequest)
        handleResponse(response , "Worker Created")
    }


    suspend fun deleteWorker(workerId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = workersAPI.deleteWorker(workerId)
        handleResponse(response , "Worker Deleted")
    }

    suspend fun updateWorker(workerRequest: WorkerRequest, workerId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = workersAPI.updateWorker(workerId , workerRequest)
        handleResponse(response , "Worker Updated")
    }

    suspend fun getWorkerById(workerId: String) {
        _workerLiveData.postValue(NetworkResult.Loading())
        val response = workersAPI.getWorkerById(workerId)
        handleWorkerResponse(response)
    }

//    suspend fun uploadImageToFirebase(uri: Uri?, onComplete: (String) -> Unit){
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

    private fun handleResponse(response: Response<WorkerResponse> , message: String ) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    private fun handleWorkerResponse(response: Response<WorkerResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _workerLiveData.postValue(NetworkResult.Success(listOf(response.body()!!)))
        } else {
            _workerLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}