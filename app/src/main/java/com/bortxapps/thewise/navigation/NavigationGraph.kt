package com.bortxapps.thewise.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bortxapps.thewise.navigation.NavigationConstants.ELECTION_SCREEN_ARGUMENT_ID
import com.bortxapps.thewise.presentation.screens.elections.ElectionInfoScreen
import com.bortxapps.thewise.presentation.screens.home.HomeScreen
import com.bortxapps.thewise.presentation.screens.options.OptionsListScreen
import com.bortxapps.thewise.presentation.screens.splash.SplashScreen


@ExperimentalMaterialApi
@Composable
fun MyAppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Splash.getFullRoute())
    {
        composable(route = Screen.Splash.getFullRoute()) {
            SplashScreen {
                navController.navigate(route = Screen.Home.getFullRoute()) {
                    popUpTo(
                        route =
                        Screen.Splash.getFullRoute()
                    ) {
                        inclusive = true
                    }
                }
            }
        }

        composable(route = Screen.Home.getFullRoute()) {
            HomeScreen(onBackNavigation = { navController.navigateUp() },
                onNavigationToDetail = {
                    navController.navigate(Screen.InfoElection.getRouteWithId(it.toString()))
                })
        }


        composable(
            route = Screen.InfoElection.getFullRoute(),
            arguments = listOf(navArgument(ELECTION_SCREEN_ARGUMENT_ID) { type = NavType.LongType })
        )
        {
            val id: Long = it.arguments?.getLong(ELECTION_SCREEN_ARGUMENT_ID) ?: 0
            ElectionInfoScreen(
                electionId = id,
                onBackNavigation = { navController.navigateUp() },
                onBackToHome = { navController.navigate(Screen.Home.getFullRoute()) },
                navController = navController
            )
        }

        composable(
            route = Screen.OptionsList.getFullRoute(),
            arguments = listOf(navArgument(ELECTION_SCREEN_ARGUMENT_ID) { type = NavType.LongType })
        )
        {
            val id: Long = it.arguments?.getLong(ELECTION_SCREEN_ARGUMENT_ID) ?: 0
            OptionsListScreen(
                electionId = id,
                onBackNavigation = { navController.navigateUp() },
                onBackToHome = { navController.navigate(Screen.Home.getFullRoute()) },
                navController = navController
            )
        }
    }
}