package com.douglasessousa.royalehub.data.local

import com.douglasessousa.royalehub.data.model.Carta

object LocalCartaRepository {

    private val cartas = listOf(
        Carta(
            id = 27000012,
            nome = "Wall Breakers",
            elixir = 3,
            raridade = "rare",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/vD24bBgK4rSq7wx5QEbuqChtPMRFviL_ep76GwQw1yA.png"
        ),
        Carta(
            id = 27000013,
            nome = "Goblin Drill",
            elixir = 4,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/eN2TKUYbih-26yBi0xy5LVFOA0zDftgDqxxnVfdIg1o.png"
        ),
        Carta(
            id = 28000000,
            nome = "Fireball",
            elixir = 4,
            raridade = "rare",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/lZD9MILQv7O-P3XBr_xOLS5idwuz3_7Ws9G60U36yhc.png"
        ),
        Carta(
            id = 28000001,
            nome = "Arrows",
            elixir = 3,
            raridade = "common",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/Flsoci-Y6y8ZFVi5uRFTmgkPnCmMyMVrU7YmmuPvSBo.png"
        ),
        Carta(
            id = 28000002,
            nome = "Rage",
            elixir = 2,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/bGP21OOmcpHMJ5ZA79bHVV2D-NzPtDkvBskCNJb7pg0.png"
        ),
        Carta(
            id = 28000003,
            nome = "Rocket",
            elixir = 6,
            raridade = "rare",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/Ie07nQNK9CjhKOa4-arFAewi4EroqaA-86Xo7r5tx94.png"
        ),
        Carta(
            id = 28000004,
            nome = "Goblin Barrel",
            elixir = 3,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/CoZdp5PpsTH858l212lAMeJxVJ0zxv9V-f5xC8Bvj5g.png"
        ),
        Carta(
            id = 28000005,
            nome = "Freeze",
            elixir = 4,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/I1M20_Zs_p_BS1NaNIVQjuMJkYI_1-ePtwYZahn0JXQ.png"
        ),
        Carta(
            id = 28000006,
            nome = "Mirror",
            raridade = "epic",
            elixir = 0,
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/wC6Cm9rKLEOk72zTsukVwxewKIoO4ZcMJun54zCPWvA.png"
        ),
        Carta(
            id = 28000007,
            nome = "Lightning",
            elixir = 6,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/fpnESbYqe5GyZmaVVYe-SEu7tE0Kxh_HZyVigzvLjks.png"
        ),
        Carta(
            id = 28000008,
            nome = "Zap",
            elixir = 2,
            raridade = "common",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/7dxh2-yCBy1x44GrBaL29vjqnEEeJXHEAlsi5g6D1eY.png"
        ),
        Carta(
            id = 28000009,
            nome = "Poison",
            elixir = 4,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/98HDkG2189yOULcVG9jz2QbJKtfuhH21DIrIjkOjxI8.png"
        ),
        Carta(
            id = 28000010,
            nome = "Graveyard",
            elixir = 5,
            raridade = "legendary",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/Icp8BIyyfBTj1ncCJS7mb82SY7TPV-MAE-J2L2R48DI.png"
        ),
        Carta(
            id = 28000011,
            nome = "The Log",
            elixir = 2,
            raridade = "legendary",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/_iDwuDLexHPFZ_x4_a0eP-rxCS6vwWgTs6DLauwwoaY.png"
        ),
        Carta(
            id = 28000012,
            nome = "Tornado",
            elixir = 3,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/QJB-QK1QJHdw4hjpAwVSyZBozc2ZWAR9pQ-SMUyKaT0.png"
        ),
        Carta(
            id = 28000013,
            nome = "Clone",
            elixir = 3,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/mHVCet-1TkwWq-pxVIU2ZWY9_2z7Z7wtP25ArEUsP_g.png"
        ),
        Carta(
            id = 28000014,
            nome = "Earthquake",
            elixir = 3,
            raridade = "rare",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/XeQXcrUu59C52DslyZVwCnbi4yamID-WxfVZLShgZmE.png"
        ),
        Carta(
            id = 28000015,
            nome = "Barbarian Barrel",
            elixir = 2,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/Gb0G1yNy0i5cIGUHin8uoFWxqntNtRPhY_jeMXg7HnA.png"
        ),
        Carta(
            id = 28000016,
            nome = "Heal Spirit",
            elixir = 1,
            raridade = "rare",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/GITl06sa2nGRLPvboyXbGEv5E3I-wAwn1Eqa5esggbc.png"
        ),
        Carta(
            id = 28000017,
            nome = "Giant Snowball",
            elixir = 2,
            raridade = "common",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/7MaJLa6hK9WN2_VIshuh5DIDfGwm0wEv98gXtAxLDPs.png"
        ),
        Carta(
            id = 28000018,
            nome = "Royal Delivery",
            elixir = 3,
            raridade = "common",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/LPg7AGjGI3_xmi7gLLgGC50yKM1jJ2teWkZfoHJcIZo.png"
        ),
        Carta(
            id = 28000023,
            nome = "Void",
            elixir = 3,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/BykyeWDqzn4PlHSszu3NbrXT1mHxW2EA8vHbQGR5LDE.png"
        ),
        Carta(
            id = 28000024,
            nome = "Goblin Curse",
            elixir = 2,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/OQPfGgT5mHEUvPuKKt0plZT0PNtIjCqUgQ3Rm86dQ2k.png"
        ),
        Carta(
            id = 28000025,
            nome = "Spirit Empress",
            elixir = 6,
            raridade = "legendary",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/MWWBZqiQeRBXSqEITVMj46nO_uCIUsqBidvUw9K2twY.png"
        ),
        Carta(
            id = 28000026,
            nome = "Vines",
            elixir = 3,
            raridade = "epic",
            imagemUrl = "https://api-assets.clashroyale.com/cards/300/V5sJlPuo2WBRtirJ1lGEPJA4NvQ0KPwFeMrpxLX5z_8.png"
        )
    )

    fun getAll(): List<Carta> = cartas

    fun getById(id: Int): Carta? = cartas.find { it.id == id }
}
