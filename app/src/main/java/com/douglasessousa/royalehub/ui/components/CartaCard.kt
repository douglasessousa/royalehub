package com.douglasessousa.royalehub.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.douglasessousa.royalehub.data.model.Carta

@Composable
fun CartaCard(
    carta: Carta?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        if (carta != null) {
            AsyncImage(
                model = carta.imagemUrl,
                contentDescription = "Imagem da carta ${carta.nome}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(95f / 140f),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(95f/140f)
                    .border(2.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
            )
        }
    }
}