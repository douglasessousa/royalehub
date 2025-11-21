package com.douglasessousa.royalehub.data.local.db

import androidx.room.TypeConverter
import com.douglasessousa.royalehub.data.model.Carta
import com.douglasessousa.royalehub.data.model.TropaDeTorre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromCartaList(cartas: List<Carta>?): String? {
        if (cartas == null) {
            return null
        }
        val type = object : TypeToken<List<Carta>>() {}.type
        return gson.toJson(cartas, type)
    }

    @TypeConverter
    fun toCartaList(cartasString: String?): List<Carta>? {
        if (cartasString == null) {
            return null
        }
        val type = object : TypeToken<List<Carta>>() {}.type
        return gson.fromJson(cartasString, type)
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