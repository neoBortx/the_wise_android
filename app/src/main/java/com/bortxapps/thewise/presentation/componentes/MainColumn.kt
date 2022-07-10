package com.bortxapps.thewise.presentation.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object MainColumn {

    @Composable
    fun GetMainColumn(content: @Composable ColumnScope.() -> Unit){
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            content = content
        )
    }
}