package com.bortxapps.thewise.presentation.componentes.texfield

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Badge
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
        ConditionWeight.HIGH -> colorResource(id = R.color.badge_high)
        ConditionWeight.MUST -> colorResource(id = R.color.badge_must)
    }
}

@Composable
fun ConditionBadge(label: String, weight: ConditionWeight) {

    Badge(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth(),
        backgroundColor = getColor(weight)
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = label, maxLines = 1,
            color = colorResource(id = R.color.black),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
        )
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
fun PrintBadgePreviewHigh() {
    ConditionBadge("Piscina climatizada", ConditionWeight.HIGH)
}

@Preview
@Composable
fun PrintBadgePreviewMust() {
    ConditionBadge("Piscina climatizada", ConditionWeight.MUST)
}