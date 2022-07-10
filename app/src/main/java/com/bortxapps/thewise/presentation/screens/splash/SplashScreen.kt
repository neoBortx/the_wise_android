package com.bortxapps.thewise.presentation.screens.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bortxapps.thewise.R
import com.bortxapps.thewise.navigation.Screen
import com.bortxapps.thewise.presentation.viewmodels.SplashViewModel
import com.bortxapps.thewise.ui.theme.Yellow300
import com.bortxapps.thewise.ui.theme.Yellow800

@Composable
fun SplashScreen(navHostController: NavHostController,
                 splashViewModel: SplashViewModel = hiltViewModel())
{
        Splash()
        //navHostController.popBackStack()
        navHostController.navigate(route = Screen.Home.getFullRoute()){
            popUpTo(route =
            Screen.Splash.getFullRoute()) {
                inclusive = true
            }
        }
}

@Composable
fun Splash()
{
    Box(modifier = Modifier
        .background(Brush.verticalGradient(listOf(Yellow800, Yellow300)))
        .fillMaxSize(),
        contentAlignment = Alignment.Center,)
    {
        Image(painter = painterResource(id = R.drawable.ic_the_wise_logo),
        contentDescription = stringResource(R.string.app_logo),
        contentScale = ContentScale.None)
    }
}

@Composable
@Preview
fun SplashScreenPreview()
{
    Splash()
}