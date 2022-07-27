package com.bortxapps.thewise.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bortxapps.thewise.R
import com.bortxapps.thewise.navigation.Screen
import com.bortxapps.thewise.ui.theme.Yellow300
import com.bortxapps.thewise.ui.theme.Yellow800

@Composable
fun SplashScreen(navHostController: NavHostController) {
    Splash()
    navHostController.navigate(route = Screen.Home.getFullRoute()) {
        popUpTo(
            route =
            Screen.Splash.getFullRoute()
        ) {
            inclusive = true
        }
    }
}

@Composable
fun Splash() {
    Box(
        modifier = Modifier
            .background(Brush.verticalGradient(listOf(Yellow800, Yellow300)))
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    )
    {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_the_wise_logo),
                tint = colorResource(id = R.color.dark_text),
                contentDescription = stringResource(R.string.app_logo)
            )
            Text(
                stringResource(id = R.string.app_name),
                color = colorResource(id = R.color.dark_text),
                fontSize = 40.sp,
                fontFamily = FontFamily(Font(R.font.washington_text))
            )
        }
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    Splash()
}