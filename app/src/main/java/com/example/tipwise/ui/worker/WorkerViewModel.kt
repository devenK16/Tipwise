package com.example.tipwise.ui.worker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tipwise.models.WorkerRequest
import com.example.tipwise.repository.WorkerRepository
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

}