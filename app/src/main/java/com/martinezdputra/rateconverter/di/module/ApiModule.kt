package com.martinezdputra.rateconverter.di.module

import com.martinezdputra.rateconverter.repository.ApiService
import com.martinezdputra.rateconverter.repository.datastore.HomepageLocalDataStore
import com.martinezdputra.rateconverter.repository.datastore.HomepageRemoteDataStore
import com.martinezdputra.rateconverter.repository.repository.HomepageRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named


@Module
class ApiModule {

    @Provides
    fun providesRetrofit(@Named("baseUrl") baseUrl: String) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    @Provides
    fun providesApiService() : ApiService {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return Retrofit.Builder()
            .baseUrl("http://api.currencylayer.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideHomepageLocalDataStore() = HomepageLocalDataStore()

    @Provides
    fun provideHomepageRemoteDataStore(apiService: ApiService) = HomepageRemoteDataStore(apiService)

    @Provides
    fun provideHomepageRepository(localDataStore: HomepageLocalDataStore, remoteDataStore: HomepageRemoteDataStore) = HomepageRepository(localDataStore, remoteDataStore)
}