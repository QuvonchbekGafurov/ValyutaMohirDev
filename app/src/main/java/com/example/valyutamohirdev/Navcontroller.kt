package com.example.valyutamohirdev

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "currencyList") {
        composable("currencyList") {
            CurrencyScreen(navController = navController)
        }
        composable("currencyDetail/{currencyCode}") { backStackEntry ->
            val currencyCode = backStackEntry.arguments?.getString("currencyCode")
            CurrencyDetailScreen(currencyCode, navController = navController)
        }
    }
}
