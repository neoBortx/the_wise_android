package com.bortxapps.thewise.presentation.componentes

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.TopAppBar.GetTopAppBar

object TopAppBar {
    @Composable
    fun GetTopAppBar(showBackButton: Boolean, title: String, backCallback: () -> Unit) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = {
                        backCallback()
                    }) {
                        Icon(Icons.Rounded.ArrowBack, "")
                    }
                }
            },
            backgroundColor = colorResource(id = R.color.yellow_800)

        )
    }
}

@Composable
@Preview
fun ShowPreview() {
    GetTopAppBar(true, "The wise") { println("Back pressed") }
}