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
import com.douglasessousa.royalehub.data.model.TropaDeTorre
import com.douglasessousa.royalehub.ui.theme.RoyalehubTheme

@Composable
fun TropaDeTorreCard(
    tropaDeTorre: TropaDeTorre,
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
            model = tropaDeTorre.imagemUrl,
            contentDescription = "Imagem da tropa de torre ${tropaDeTorre.nome}",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f), // Proporção retangular
            contentScale = ContentScale.Fit // Alterado para Fit
        )
    }
}

@Preview
@Composable
private fun TropaDeTorreCardPreview() {
    RoyalehubTheme {
        TropaDeTorreCard(
            tropaDeTorre = TropaDeTorre(
                id = 1,
                nome = "Princesa da Torre",
                raridade = "Comum",
                imagemUrl = ""
            ),
            onClick = {}
        )
    }
}
