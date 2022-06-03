package com.example.nexleinterview.di

import com.example.nexleinterview.data.network.auth.AuthDataSource
import com.example.nexleinterview.data.network.auth.AuthRemoteDataSource
import com.example.nexleinterview.data.network.auth.AuthRepository
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
    fun providerAuthRepository(authRemoteDataSource: AuthRemoteDataSource): AuthDataSource{
        return AuthRepository(authRemoteDataSource)
    }
}
