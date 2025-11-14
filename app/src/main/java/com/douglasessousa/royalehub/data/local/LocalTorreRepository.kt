package com.douglasessousa.royalehub.data.local
import com.douglasessousa.royalehub.data.model.TorreTroop

object LocalTorreRepository {

    private val torres = listOf(
        TorreTroop(
            id = 159000000,
            nome = "Tower Princess",
            raridade = "common",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/Nzo5Gjbh7NG6O3Hyu7ev54Pu5zK7vDMR2fbpGdVsS64.png"
        ),
        TorreTroop(
            id = 159000001,
            nome = "Cannoneer",
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/cUfU4UowRdbIiRvxv0ns4ezQUNndJTy7D2q4I_K_fzg.png"
        ),
        TorreTroop(
            id = 159000002,
            nome = "Dagger Duchess",
            raridade = "legendary",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/MVj028nMLCmBuP3HlV503uxVAxFg7jyliJVZ5JYJ1h8.png"
        ),
        TorreTroop(
            id = 159000004,
            nome = "Royal Chef",
            raridade = "legendary",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/CLxP2o0iX7q9peMkekI8Ki4DgWixra8L5aUbiAeBU9U.png"
        )
    )

    fun getAll(): List<TorreTroop> = torres

    fun getById(id: Int): TorreTroop? = torres.find { it.id == id }
}
