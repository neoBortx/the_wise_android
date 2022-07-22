package com.bortxapps.thewise.presentation.componentes.texfield

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.thewise.R

@Composable
private fun getColor(weight: ConditionWeight): Color {
    return when (weight) {
        ConditionWeight.LOW -> colorResource(id = R.color.badge_low)
        ConditionWeight.MEDIUM -> colorResource(id = R.color.badge_medium)
        ConditionWeight.MUST -> colorResource(id = R.color.badge_must)
    }
}

@Composable
fun ConditionBadge(label: String, weight: ConditionWeight, deleteCallback: (() -> Unit)? = null) {

    Badge(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth(),
        backgroundColor = getColor(weight)
    ) {

        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = label, maxLines = 1,
                color = colorResource(id = R.color.black),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )

            if (deleteCallback != null) {
                Button(
                    onClick = { deleteCallback.invoke() },
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 5.dp),
                    border = null,
                    elevation = null,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        disabledBackgroundColor = Color.Transparent,
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close, "", modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PrintBadgePreviewLow() {
    ConditionBadge("Piscina climatizada", ConditionWeight.LOW)
}

@Preview
@Composable
fun PrintBadgePreviewMedium() {
    ConditionBadge("Piscina climatizada", ConditionWeight.MEDIUM)
}

@Preview
@Composable
fun PrintBadgePreviewMust() {
    ConditionBadge("Piscina climatizada", ConditionWeight.MUST)
}