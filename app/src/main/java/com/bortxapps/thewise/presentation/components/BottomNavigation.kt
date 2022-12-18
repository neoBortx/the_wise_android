package com.bortxapps.thewise.presentation.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.R
import com.bortxapps.thewise.navigation.Screen

object BottomNavigation {
    @Composable
    fun GetBottomNavigation(election: Election, navController: NavHostController) {
        val items = listOf(
            Screen.InfoElection,
            Screen.OptionsList
        )

        BottomNavigation(backgroundColor = colorResource(id = R.color.yellow_800)) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(item.iconId),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            stringResource(id = item.label),
                            color = colorResource(id = R.color.dark_text)
                        )
                    },
                    selectedContentColor = colorResource(id = R.color.dark_text),
                    unselectedContentColor = colorResource(id = R.color.light_text),
                    alwaysShowLabel = true,
                    selected = currentRoute == item.getFullRoute(),
                    onClick = {
                        navController.navigate(item.getRouteWithId(election.id.toString())) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}