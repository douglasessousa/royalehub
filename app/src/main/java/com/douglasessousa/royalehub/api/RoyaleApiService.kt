package com.douglasessousa.royalehub.api

import com.douglasessousa.royalehub.data.model.Card
import com.douglasessousa.royalehub.data.model.Tower

import retrofit2.http.GET

interface RoyaleApiService {

    @GET("cards")
    suspend fun getAllCards(): List<Card>

    @GET("towers")
    suspend fun getAllTowers(): List<Tower>
}