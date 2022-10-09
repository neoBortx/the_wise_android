package com.bortxapps.thewise.presentation.screens.options

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bortxapps.application.pokos.Condition
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.texfield.SimpleConditionBadge
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode


@Composable
fun OptionCardBody(expanded: Boolean, deleteCallBack: (() -> Unit)?, conditions: List<Condition>) {

    AnimatedVisibility(visible = expanded) {
        Column {
            Divider(modifier = Modifier.padding(vertical = 0.dp, horizontal = 5.dp))
            Requisites(
                showBottomMargin = deleteCallBack != null,
                conditions = conditions
            )
            DeleteButton(deleteCallBack)
        }
    }

}

@Composable
private fun Requisites(showBottomMargin: Boolean, conditions: List<Condition>) {
    Text(
        text = stringResource(id = R.string.matching_conditions_label),
        style = MaterialTheme.typography.subtitle1,
        textAlign = TextAlign.Left,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(top = 5.dp)
            .wrapContentWidth()
            .wrapContentHeight(),
        color = colorResource(id = R.color.dark_text),
        overflow = TextOverflow.Ellipsis
    )
    FlowRow(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                top = 8.dp,
                start = 10.dp,
                end = 6.dp,
                bottom = if (showBottomMargin) 0.dp else 10.dp
            ),
        mainAxisAlignment = MainAxisAlignment.Start,
        mainAxisSize = SizeMode.Expand,
        crossAxisSpacing = 5.dp,
        mainAxisSpacing = 5.dp,
    ) {
        conditions.sortedByDescending { condition -> condition.weight }
            .forEach { condition ->
                SimpleConditionBadge(condition.name, condition.weight)
            }
    }
}

@Composable
private fun DeleteButton(deleteCallBack: (() -> Unit)?) {

    val colTitle = colorResource(id = R.color.yellow_800)

    deleteCallBack?.let {
        Button(
            onClick = { deleteCallBack() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            Text(
                text = stringResource(id = R.string.delete),
                textDecoration = TextDecoration.Underline,
                color = colTitle,
                textAlign = TextAlign.End
            )
        }
    }
}