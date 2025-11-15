package com.douglasessousa.royalehub.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.douglasessousa.royalehub.data.model.Carta
import com.douglasessousa.royalehub.ui.theme.RoyalehubTheme

@Composable
fun CartaCard(
    carta: Carta,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        AsyncImage(
            model = carta.imagemUrl,
            contentDescription = "Imagem da carta ${carta.nome}",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(95f / 140f),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
private fun CartaCardPreview() {
    RoyalehubTheme {
        CartaCard(
            carta = Carta(
                id = 1,
                nome = "Cavaleiro",
                elixir = 3,
                raridade = "Comum",
                imagemUrl = ""
            ),
            onClick = {}
        )
    }
}
