package com.bortxapps.thewise.presentation.componentes.texfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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

private fun setAlfa(isSelected: Boolean) = if (isSelected) {
    1f
} else {
    0.2f
}

@Composable
fun SimpleConditionBadge(label: String, weight: ConditionWeight) {

    Badge(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth(),
        backgroundColor = getColor(weight)
    ) {

        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = label, maxLines = 1,
                color = colorResource(id = R.color.black),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun RemovableConditionBadge(
    label: String,
    weight: ConditionWeight,
    deleteCallback: (() -> Unit)
) {

    Badge(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth(),
        backgroundColor = getColor(weight)
    ) {

        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = label, maxLines = 1,
                color = colorResource(id = R.color.black),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = 5.dp)
            )

            Button(
                onClick = { deleteCallback.invoke() },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(15.dp)
                    .width(15.dp),
                border = null,
                elevation = null,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    disabledBackgroundColor = Color.Transparent,
                )
            ) {
                Icon(imageVector = Icons.Default.Close, "", Modifier.size(40.dp))
            }
        }
    }
}

@Composable
fun SelectableConditionBadge(
    label: String,
    weight: ConditionWeight,
    isSelected: Boolean,
    selectedCallBack: ((Boolean) -> Unit)
) {

    Badge(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth(),
        backgroundColor = getColor(weight).copy(alpha = setAlfa(isSelected))
    ) {

        Row(
            modifier = Modifier
                .padding(5.dp)
                .clickable {
                    selectedCallBack.invoke(!isSelected)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .width(15.dp)
                        .height(15.dp)
                )
            }
            Text(
                text = label, maxLines = 1,
                color = colorResource(id = R.color.black),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
fun PrintBadgePreviewLow() {
    RemovableConditionBadge("Piscina climatizada", ConditionWeight.LOW) {

    }
}

@Preview
@Composable
fun PrintBadgePreviewMedium() {
    SelectableConditionBadge("Piscina climatizada", ConditionWeight.MEDIUM, false) {

    }
}

@Preview
@Composable
fun PrintBadgePreviewMust() {
    SelectableConditionBadge("Piscina climatizada", ConditionWeight.MUST, true) {

    }
}