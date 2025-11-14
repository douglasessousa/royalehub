package com.douglasessousa.royalehub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.douglasessousa.royalehub.ui.components.DeckCard
import com.douglasessousa.royalehub.data.local.*
import com.douglasessousa.royalehub.navigation.Routes

@Composable
fun DecksScreen(navController: NavController) {
    val decks = LocalRepository.getDecks()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Meus Decks",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            decks.forEach { deck ->
                DeckCard(deck)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

}