package com.example.tipwise.api

import com.example.tipwise.models.WorkerRequest
import com.example.tipwise.models.WorkerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WorkersAPI {
    @GET("/workers")
    suspend fun getWorkers() : Response<List<WorkerResponse>>

    @POST("/workers")
    suspend fun addWorker(@Body workerRequest: WorkerRequest ) : Response<WorkerResponse>

    @DELETE("/workers/{workerId}")
    suspend fun deleteWorker( @Path("workerId") workerId: String ) : Response<WorkerResponse>

    @PUT("/workers/{workerId}")
    suspend fun updateWorker( @Path("workerId") workerId: String , @Body workerRequest: WorkerRequest ) : Response<WorkerResponse>
}