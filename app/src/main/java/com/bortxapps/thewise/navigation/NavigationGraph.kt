package com.bortxapps.thewise.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bortxapps.thewise.navigation.NavigationConstants.ELECTION_SCREEN_ARGUMENT_ID
import com.bortxapps.thewise.presentation.screens.conditions.ConditionsListScreen
import com.bortxapps.thewise.presentation.screens.elections.ElectionInfoScreen
import com.bortxapps.thewise.presentation.screens.home.HomeScreen
import com.bortxapps.thewise.presentation.screens.options.OptionsListScreen
import com.bortxapps.thewise.presentation.screens.splash.SplashScreen


@ExperimentalMaterialApi
@Composable
fun SetSplashNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.getFullRoute())
    {
        composable(route = Screen.Splash.getFullRoute()) {
            SplashScreen(navHostController = navController)
        }
        composable(route = Screen.Home.getFullRoute()) {
            HomeScreen(navHostController = navController)
        }
        composable(
            route = Screen.InfoElection.getFullRoute(),
            arguments = listOf(navArgument(ELECTION_SCREEN_ARGUMENT_ID) { type = NavType.LongType })
        )
        {
            val id: Long = it.arguments?.getLong(ELECTION_SCREEN_ARGUMENT_ID) ?: 0
            ElectionInfoScreen(navHostController = navController, electionId = id)
        }
        composable(route = Screen.OptionsList.getFullRoute(),
            arguments = listOf(navArgument(ELECTION_SCREEN_ARGUMENT_ID) { type = NavType.LongType })
        )
        {
            val id: Long = it.arguments?.getLong(ELECTION_SCREEN_ARGUMENT_ID) ?: 0
            OptionsListScreen(navHostController = navController, electionId = id)
        }
        composable(route = Screen.ConditionsList.getFullRoute(),
            arguments = listOf(navArgument(ELECTION_SCREEN_ARGUMENT_ID) { type = NavType.LongType }))
        {
            val id: Long = it.arguments?.getLong(ELECTION_SCREEN_ARGUMENT_ID) ?: 0
            ConditionsListScreen(navHostController = navController, electionId = id)
        }

    }
}