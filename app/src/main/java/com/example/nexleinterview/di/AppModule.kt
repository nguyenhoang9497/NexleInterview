package com.example.nexleinterview.di

import com.example.nexleinterview.BuildConfig
import com.example.nexleinterview.BuildConfig.BASE_URL
import com.example.nexleinterview.data.network.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val HEADER_CONTENT_TYPE = "Content-Type"
    private const val HEADER_CONTENT_TYPE_VALUE = "application/json"
    private const val HEADER_PLATFORM = "x-device-platform"
    private const val PLATFORM = "ANDROID"
    private const val CONNECT_TIMEOUT_S: Long = 40
    private const val READ_TIMEOUT_S: Long = 40
    private const val WRITE_TIMEOUT_S: Long = 40

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.header(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE)
            requestBuilder.header(HEADER_PLATFORM, PLATFORM)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            if (BuildConfig.DEBUG) {
                addInterceptor(httpLoggingInterceptor)
            }
            connectTimeout(CONNECT_TIMEOUT_S, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT_S, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT_S, TimeUnit.SECONDS)
        }.build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideNullOnEmptyConverterFactory(): Converter.Factory {
        return object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter =
                    retrofit.nextResponseBodyConverter<Any?>(
                        converterFactory(),
                        type,
                        annotations
                    )

                override fun convert(value: ResponseBody) =
                    if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }
    }

    @Provides
    @Singleton
    fun provideJsonParser(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        jsonParser: Json,
        nullOnEmptyConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                jsonParser.asConverterFactory(HEADER_CONTENT_TYPE_VALUE.toMediaType())
            )
            .addConverterFactory(nullOnEmptyConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}
