package com.douglasessousa.royalehub.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.douglasessousa.royalehub.ui.screens.*


sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Decks : Routes("decks")
    object History : Routes("history")
    object Profile : Routes("profile")
}

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route) { HomeScreen(navController) }
        composable(Routes.Decks.route) { DecksScreen(navController) }
//        composable(Routes.History.route) { MatchHistoryScreen(navController) }
//        composable(Routes.Profile.route) { ProfileScreen(navController) }
    }
}
