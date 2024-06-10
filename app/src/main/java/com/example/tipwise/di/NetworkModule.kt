package com.example.tipwise.di


import com.example.tipwise.api.UsersAPI
import com.example.tipwise.api.AuthInterceptor
import com.example.tipwise.api.ReviewAPI
import com.example.tipwise.api.WorkersAPI
import com.example.tipwise.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofitBuilder() : Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient( authInterceptor: AuthInterceptor) : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun providesUsersAPI( retrofitBuilder : Retrofit.Builder) : UsersAPI {
        return retrofitBuilder.build().create(UsersAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesWorkersAPI( retrofitBuilder : Retrofit.Builder , okHttpClient: OkHttpClient) : WorkersAPI{
        return retrofitBuilder.client(okHttpClient).build().create(WorkersAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesReviewAPI( retrofitBuilder : Retrofit.Builder) : ReviewAPI {
        return retrofitBuilder.build().create(ReviewAPI::class.java)
    }

}