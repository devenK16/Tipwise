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

    fun getNotes(){
        viewModelScope.launch {
            workerRepository.getWorkers()
        }
    }

    fun createNote(workerRequest: WorkerRequest ){
        viewModelScope.launch {
            workerRepository.createWorker(workerRequest)
        }
    }

    fun deleteNote(workerId: String){
        viewModelScope.launch {
            workerRepository.deleteWorker(workerId)
        }
    }

    fun updateNote(workerRequest: WorkerRequest , workerId: String){
        viewModelScope.launch {
            workerRepository.updateWorker(workerRequest, workerId)
        }
    }

}