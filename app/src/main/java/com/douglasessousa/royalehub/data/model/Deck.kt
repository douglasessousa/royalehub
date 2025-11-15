package com.douglasessousa.royalehub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val cartas: List<Carta>,
    val tropaDeTorre: TropaDeTorre? = null
)