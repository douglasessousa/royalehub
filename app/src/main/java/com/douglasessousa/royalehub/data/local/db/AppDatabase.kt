package com.douglasessousa.royalehub.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.douglasessousa.royalehub.data.local.Converters
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.model.MatchResult
import com.douglasessousa.royalehub.data.model.User

@Database(entities = [Deck::class, MatchResult::class, User::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun royaleDao(): RoyaleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "royale_hub_database"
                )
                    .fallbackToDestructiveMigration() // Apaga os dados ao mudar a versão do banco
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
