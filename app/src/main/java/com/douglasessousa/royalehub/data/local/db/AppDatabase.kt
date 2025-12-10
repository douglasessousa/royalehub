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

/**
 * Configuração do banco de dados
 * Esta classe abstrata serve como o ponto de entrada principal para o Room.
 */
@Database(
    entities = [Deck::class, MatchResult::class, User::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun royaleDao(): RoyaleDao

    /**
     * Criar uma conexão com o banco de dados é uma operação "pesada" e gasta bateria.
     * Por isso, usei este padrão pra garantir que exista apenas uma instância
     * do banco de dados aberta durante toda a vida do aplicativo.
     */
    companion object {
        // @Volatile: se uma thread atualizar o banco, as outras saibam imediatamente.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Se já existe devolve o mesmo, se n cria um novo.
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