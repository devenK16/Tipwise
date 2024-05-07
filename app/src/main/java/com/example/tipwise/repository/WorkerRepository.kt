package com.example.tipwise.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tipwise.api.WorkersAPI
import com.example.tipwise.models.WorkerRequest
import com.example.tipwise.models.WorkerResponse
import com.example.tipwise.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class WorkerRepository @Inject constructor( private val workersAPI: WorkersAPI ) {
    private val _workerLiveData = MutableLiveData<NetworkResult<List<WorkerResponse>>>()
    val workerLiveData : LiveData<NetworkResult<List<WorkerResponse>>>
        get() = _workerLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData : LiveData<NetworkResult<String>>
        get() = _statusLiveData

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

    private fun handleResponse(response: Response<WorkerResponse> , message: String ) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}