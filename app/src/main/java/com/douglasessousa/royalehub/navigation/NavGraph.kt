package com.douglasessousa.royalehub.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.douglasessousa.royalehub.ui.screens.CartasScreen
import com.douglasessousa.royalehub.ui.screens.DecksScreen
import com.douglasessousa.royalehub.ui.screens.HomeScreen
import com.douglasessousa.royalehub.ui.screens.TropasDeTorreScreen
import com.douglasessousa.royalehub.viewmodel.AppViewModel

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Decks : Routes("decks")
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
        composable(Routes.Home.route) { HomeScreen(navController) }
        composable(Routes.Decks.route) { DecksScreen(navController) }
        composable(Routes.Cards.route) { CartasScreen(appViewModel) }
        composable(Routes.TropasDeTorre.route) { TropasDeTorreScreen(appViewModel) }
        composable(Routes.History.route) { /* TODO */ }
        composable(Routes.Profile.route) { /* TODO */ }
    }
}
