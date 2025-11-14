package com.douglasessousa.royalehub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.douglasessousa.royalehub.navigation.Routes

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "RoyaleHub", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate(Routes.Decks.route) }) {
            Text("Meus Decks")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { navController.navigate(Routes.History.route) }) {
            Text("Histórico de Partidas")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { navController.navigate(Routes.Profile.route) }) {
            Text("Perfil")
        }
    }
}