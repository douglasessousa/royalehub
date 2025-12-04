package com.douglasessousa.royalehub.api

import com.douglasessousa.royalehub.data.model.Card
import retrofit2.http.GET

interface RoyaleApiService {

    @GET("cards")
    suspend fun getAllCards(): List<Card>

    @GET("towers")
    suspend fun getAllTowers(): List<Card>
}