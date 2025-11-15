package com.douglasessousa.royalehub.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.douglasessousa.royalehub.ui.screens.CartasScreen
import com.douglasessousa.royalehub.ui.screens.DeckDetailScreen
import com.douglasessousa.royalehub.ui.screens.DecksScreen
import com.douglasessousa.royalehub.ui.screens.HomeScreen
import com.douglasessousa.royalehub.ui.screens.TropasDeTorreScreen
import com.douglasessousa.royalehub.viewmodel.AppViewModel

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Decks : Routes("decks")
    object DeckDetail : Routes("deck/{deckId}") { // Rota com argumento
        fun createRoute(deckId: Int) = "deck/$deckId"
    }
    object Cards : Routes("cards")
    object TropasDeTorre : Routes("towers")
    object History : Routes("history")
    object Profile : Routes("profile")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = viewModel()
) {
    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route) { HomeScreen(navController) } // <<< CORREÇÃO AQUI
        composable(Routes.Decks.route) { DecksScreen(appViewModel, navController) }
        composable(
            route = Routes.DeckDetail.route,
            arguments = listOf(navArgument("deckId") { type = NavType.IntType })
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getInt("deckId") ?: -1
            DeckDetailScreen(appViewModel, deckId)
        }
        composable(Routes.Cards.route) { CartasScreen(appViewModel) }
        composable(Routes.TropasDeTorre.route) { TropasDeTorreScreen(appViewModel) }
        composable(Routes.History.route) { /* TODO */ }
        composable(Routes.Profile.route) { /* TODO */ }
    }
}
