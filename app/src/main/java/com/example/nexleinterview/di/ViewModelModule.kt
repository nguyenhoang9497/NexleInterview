package com.example.nexleinterview.di

import com.example.nexleinterview.data.network.ApiService
import com.example.nexleinterview.data.network.auth.DataSource
import com.example.nexleinterview.data.network.auth.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @Singleton
    fun providerRemoteDataSource(apiService: ApiService): DataSource {
        return RemoteDataSource(apiService)
    }
}
