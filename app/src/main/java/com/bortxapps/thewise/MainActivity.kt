package com.bortxapps.thewise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import com.bortxapps.thewise.navigation.MyAppNavHost
import com.bortxapps.thewise.ui.theme.TheWiseTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheWiseTheme {
                MyAppNavHost()
            }
        }
    }
}