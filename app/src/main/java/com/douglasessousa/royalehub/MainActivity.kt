package com.douglasessousa.royalehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.douglasessousa.royalehub.ui.details.DeckDetailsScreen
import com.douglasessousa.royalehub.ui.details.DeckDetailsViewModel
import com.douglasessousa.royalehub.ui.home.HomeScreen
import com.douglasessousa.royalehub.ui.home.HomeViewModel
import com.douglasessousa.royalehub.ui.settings.SettingsScreen
import com.douglasessousa.royalehub.ui.settings.SettingsViewModel
import com.douglasessousa.royalehub.ui.stats.StatsScreen
import com.douglasessousa.royalehub.ui.stats.StatsViewModel
import com.douglasessousa.royalehub.ui.theme.RoyalehubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as RoyaleHubApp

        // Setup manual do repositório para a Factory
        val repository = com.douglasessousa.royalehub.data.repository.RoyaleRepository(
            app.database.royaleDao(),
            app.apiService
        )
        val viewModelFactory = ViewModelFactory(repository)

        setContent {
            RoyalehubTheme {
                val navController = rememberNavController()

                // observa a rota atual
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // onde a barra de navegação deve ser exibida
                val showBottomBar = currentRoute in listOf("home", "statistics", "settings")

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            RoyaleBottomBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
                            HomeScreen(
                                viewModel = homeViewModel,
                                onNavigateToCreateDeck = { navController.navigate("create_deck") },
                                onNavigateToDeckDetails = { id -> navController.navigate("deck_details/$id") },
                            )
                        }

                        composable("statistics") {
                            val statsViewModel: StatsViewModel =
                                viewModel(factory = viewModelFactory)
                            StatsScreen(viewModel = statsViewModel)
                        }

                        composable("settings") {
                            val settingsViewModel: SettingsViewModel =
                                viewModel(factory = viewModelFactory)
                            SettingsScreen(viewModel = settingsViewModel)
                        }

                        composable("create_deck") {
                            val deckViewModel: DeckViewModel = viewModel(factory = viewModelFactory)
                            CreateDeckScreen(
                                viewModel = deckViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = "deck_details/{deckId}",
                            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
                        ) { backStackEntry ->
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