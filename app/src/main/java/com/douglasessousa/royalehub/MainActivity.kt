package com.douglasessousa.royalehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.douglasessousa.royalehub.ui.components.RoyaleBottomBar
import com.douglasessousa.royalehub.ui.ViewModelFactory
import com.douglasessousa.royalehub.ui.deck.CreateDeckScreen
import com.douglasessousa.royalehub.ui.deck.DeckViewModel
import com.douglasessousa.royalehub.ui.deck_details.DeckDetailsScreen
import com.douglasessousa.royalehub.ui.deck_details.DeckDetailsViewModel
import com.douglasessousa.royalehub.ui.home.HomeScreen
import com.douglasessousa.royalehub.ui.home.HomeViewModel
import com.douglasessousa.royalehub.ui.settings.SettingsScreen
import com.douglasessousa.royalehub.ui.settings.SettingsViewModel
import com.douglasessousa.royalehub.ui.stats.StatsScreen
import com.douglasessousa.royalehub.ui.stats.StatsViewModel
import com.douglasessousa.royalehub.ui.theme.RoyalehubTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as RoyaleHubApp

        // Criamos o repositório e a fábrica de ViewModels aqui.
        // Passaremos essa 'viewModelFactory' para todas as telas, para que elas
        // possam criar seus ViewModels com acesso aos dados.
        val repository = com.douglasessousa.royalehub.data.repository.RoyaleRepository(
            app.database.royaleDao(),
            app.apiService
        )
        val viewModelFactory = ViewModelFactory(repository)

        setContent {
            // Detecta o tema do sistema
            val systemTheme = isSystemInDarkTheme()
            // Variável de estado pra permitir que o usuário mude o tema manualmente.
            var isDarkTheme by remember { mutableStateOf(systemTheme) }

            RoyalehubTheme(darkTheme = isDarkTheme) {
                // Sabe em que tela estamos e como ir para outra
                val navController = rememberNavController()

                // Observa a rota atual para decidir se mostra a barra inferior
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Lista de telas onde a Barra de Navegação deve aparecer
                val showBottomBar = currentRoute in listOf("home", "statistics", "settings")

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            RoyaleBottomBar(navController)
                        }
                    }
                ) { innerPadding ->

                    Box(modifier = Modifier.fillMaxSize()) {

                        Image(
                            painter = painterResource(id = R.drawable.royalehub_background),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            // --- Rota: HOME ---
                            composable("home") {
                                val homeViewModel: HomeViewModel =
                                    viewModel(factory = viewModelFactory)
                                HomeScreen(
                                    viewModel = homeViewModel,
                                    onNavigateToCreateDeck = { navController.navigate("create_deck") },
                                    onNavigateToDeckDetails = { id -> navController.navigate("deck_details/$id") },
                                )
                            }

                            // --- Rota: ESTATÍSTICAS ---
                            composable("statistics") {
                                val statsViewModel: StatsViewModel =
                                    viewModel(factory = viewModelFactory)
                                StatsScreen(viewModel = statsViewModel)
                            }

                            // --- Rota: CONFIGURAÇÕES ---
                            composable("settings") {
                                val settingsViewModel: SettingsViewModel = viewModel(factory = viewModelFactory)
                                SettingsScreen(
                                    viewModel = settingsViewModel,
                                    isDarkTheme = isDarkTheme,
                                    onThemeChange = { isDarkTheme = it },
                                    onSave = { navController.navigate("home") }
                                )
                            }

                            // --- Rota: CRIAR DECK ---
                            composable("create_deck") {
                                val deckViewModel: DeckViewModel =
                                    viewModel(factory = viewModelFactory)
                                CreateDeckScreen(
                                    viewModel = deckViewModel,
                                    onBack = { navController.popBackStack() }
                                )
                            }

                            // --- Rota: DETALHES DO DECK ---
                            composable(
                                route = "deck_details/{deckId}",
                                arguments = listOf(navArgument("deckId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                // Pega o ID passado na URL da navegação
                                val deckId = backStackEntry.arguments?.getInt("deckId") ?: 0
                                val detailsViewModel: DeckDetailsViewModel =
                                    viewModel(factory = viewModelFactory)
                                DeckDetailsScreen(
                                    deckId = deckId,
                                    viewModel = detailsViewModel,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}