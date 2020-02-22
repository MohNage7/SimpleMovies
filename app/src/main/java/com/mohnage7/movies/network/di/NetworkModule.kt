package com.mohnage7.movies.network.di

import com.mohnage7.movies.BuildConfig.API_KEY
import com.mohnage7.movies.BuildConfig.BASE_URL
import com.mohnage7.movies.network.RestApiService
import com.mohnage7.movies.network.adapter.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val CONNECTION_TIMEOUT = 10 // 10 seconds
private const val READ_TIMEOUT = 2 // 2 seconds
private const val WRITE_TIMEOUT = 2 // 2 seconds

val networkModule = module {
    single { providesHttpLogging() }
    single { providesHttpClient(get()) }
    single { providesRetrofit(get()) }
    single { provideRestService(get()) }
}

internal fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            //converts Retrofit response into Observable
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
}


internal fun providesHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    val builder = OkHttpClient.Builder()
    // add request time out
    builder.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
    builder.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
    builder.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
    // Add logging into
    builder.interceptors().add(httpLoggingInterceptor)
    builder.addInterceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        requestBuilder.addHeader("Authorization", API_KEY)
        val request = requestBuilder.build()
        chain.proceed(request)
    }
    return builder.build()
}

internal fun providesHttpLogging(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}


internal fun provideRestService(retrofit: Retrofit): RestApiService {
    return retrofit.create(RestApiService::class.java)
}

