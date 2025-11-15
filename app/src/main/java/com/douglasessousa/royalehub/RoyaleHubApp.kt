package com.douglasessousa.royalehub

import android.app.Application
import androidx.room.Room
import com.douglasessousa.royalehub.data.local.db.AppDatabase

class RoyaleHubApp : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "royale-hub-db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
}