package com.example.a7minutesworkout.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@DisableInstallInCheck
@Module
open class OkHttpModule {
    @Provides
    @Singleton
    open fun provideOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(HTTP_CLIENT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()
    }

    companion object {
        const val HTTP_CLIENT_CONNECTION_TIMEOUT = 10 * 1000L
    }
}