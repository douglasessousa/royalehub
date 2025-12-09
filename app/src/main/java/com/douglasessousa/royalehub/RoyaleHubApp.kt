package com.douglasessousa.royalehub

import android.app.Application
import com.douglasessousa.royalehub.api.RoyaleApiService
import com.douglasessousa.royalehub.data.local.db.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RoyaleHubApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.0.10:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: RoyaleApiService by lazy {
        retrofit.create(RoyaleApiService::class.java)
    }
}