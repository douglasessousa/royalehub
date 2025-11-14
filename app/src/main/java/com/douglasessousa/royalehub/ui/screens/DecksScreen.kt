package com.douglasessousa.royalehub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.douglasessousa.royalehub.ui.components.DeckCard
import com.douglasessousa.royalehub.data.local.*

@Composable
fun DecksScreen(navController: NavController) {
    val decks = LocalRepository.getDecks()
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Meus Decks", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        decks.forEach { deck ->
            DeckCard(deck)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}