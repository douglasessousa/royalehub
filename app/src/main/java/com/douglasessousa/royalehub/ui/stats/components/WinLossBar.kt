package com.douglasessousa.royalehub.ui.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.douglasessousa.royalehub.ui.theme.LossRed
import com.douglasessousa.royalehub.ui.theme.WinGreen

// Barra horizontal
@Composable
fun WinLossBar(wins: Int, losses: Int) {
    val total = wins + losses
    if (total == 0) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(CircleShape)
            .background(Color.LightGray.copy(alpha = 0.3f))
    ) {
        if (wins > 0) {
            Box(
                modifier = Modifier
                    .weight(wins.toFloat())
                    .fillMaxHeight()
                    .background(WinGreen)
            )
        }
        if (losses > 0) {
            Box(
                modifier = Modifier
                    .weight(losses.toFloat())
                    .fillMaxHeight()
                    .background(LossRed)
            )
        }
    }
}