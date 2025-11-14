package com.douglasessousa.royalehub.data.model
enum class MatchResult { WIN, LOSS, DRAW }

data class Match(
    val id: Int,
    val deckId: Int,
    val result: MatchResult
)