package com.douglasessousa.royalehub.data.local

import androidx.room.TypeConverter
import com.douglasessousa.royalehub.data.model.Card
import com.douglasessousa.royalehub.data.model.TropaDeTorre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromCardList(cards: List<Card>?): String? {
        if (cards == null) {
            return null
        }
        return gson.toJson(cards)
    }

    @TypeConverter
    fun toCardList(cardsString: String?): List<Card>? {
        if (cardsString == null) {
            return null
        }
        val type = object : TypeToken<List<Card>>() {}.type
        return gson.fromJson(cardsString, type)
    }

    @TypeConverter
    fun fromTropaDeTorre(tropaDeTorre: TropaDeTorre?): String? {
        if (tropaDeTorre == null) {
            return null
        }
        return gson.toJson(tropaDeTorre)
    }

    @TypeConverter
    fun toTropaDeTorre(tropaDeTorreString: String?): TropaDeTorre? {
        if (tropaDeTorreString == null) {
            return null
        }
        return gson.fromJson(tropaDeTorreString, TropaDeTorre::class.java)
    }
}