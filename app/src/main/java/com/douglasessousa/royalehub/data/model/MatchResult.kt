package com.douglasessousa.royalehub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val deckId: Int,

    val isWin: Boolean, // true = Vitória, false = Derrota

    val timestamp: Long = System.currentTimeMillis() // Data da partida
)