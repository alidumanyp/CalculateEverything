package com.aliduman.calculateeverything

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aliduman.calculateeverything.screens.Home
import com.aliduman.calculateeverything.screens.IdealWeight
import com.aliduman.calculateeverything.screens.MainScreen

@Composable
fun DrawerNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(route = "home") {
            Home()
        }
        composable(route = "weight") {
            IdealWeight()
        }

    }
}