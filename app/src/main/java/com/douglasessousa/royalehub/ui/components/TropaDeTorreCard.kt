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
import com.douglasessousa.royalehub.data.model.TropaDeTorre

@Composable
fun TropaDeTorreCard(
    tropaDeTorre: TropaDeTorre?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier // A largura agora é controlada pelo chamador
            .aspectRatio(3f / 4f)
            .clickable(onClick = onClick)
    ) {
        if (tropaDeTorre != null) {
            AsyncImage(
                model = tropaDeTorre.imagemUrl,
                contentDescription = "Imagem da tropa de torre ${tropaDeTorre.nome}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
            )
        }
    }
}