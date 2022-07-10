package com.bortxapps.thewise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bortxapps.thewise.navigation.SetSplashNavGraph
import com.bortxapps.thewise.ui.theme.TheWiseTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity()
{
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheWiseTheme {
                navController = rememberNavController()
                SetSplashNavGraph(navController = navController)
            }
        }
    }
}