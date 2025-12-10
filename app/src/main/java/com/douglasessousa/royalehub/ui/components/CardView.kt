package com.douglasessousa.royalehub.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.douglasessousa.royalehub.data.model.Card

@Composable
fun CardView(
    card: Card?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(95f / 140f)
            .clickable(onClick = onClick)
    ) {
        //SE A CARTA EXISTIR MOSTRA A CARTA
        if (card != null) {
            AsyncImage(
                model = card.imageUrl,
                contentDescription = "Imagem da carta ${card.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else { //SE NÃO MOSTRA SÓ O CONTORNO
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
            )
        }
    }
}