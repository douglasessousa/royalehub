package com.douglasessousa.royalehub.api

import com.douglasessousa.royalehub.data.model.Carta
import com.douglasessousa.royalehub.data.model.TropaDeTorre
import retrofit2.http.GET

interface ApiService {
    @GET("cards")
    suspend fun getCards(): List<Carta>

    @GET("towers")
    suspend fun getTowers(): List<TropaDeTorre>
}