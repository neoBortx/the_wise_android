package com.bortxapps.thewise.presentation.components.conditions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bortxapps.application.pokos.Condition
import com.bortxapps.thewise.presentation.components.badges.SelectableConditionBadge
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun ConditionsSelectionControl(
    allConditions: List<Condition>,
    conditionsConfigured: List<Condition>,
    onConditionSelected: (Boolean, Condition) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 10.dp)
            .padding(horizontal = 20.dp),
        mainAxisAlignment = MainAxisAlignment.Start,
        mainAxisSize = SizeMode.Expand,
        crossAxisSpacing = 12.dp,
        mainAxisSpacing = 8.dp
    ) {
        allConditions.forEach { condition ->
            SelectableConditionBadge(
                label = condition.name,
                weight = condition.weight,
                isSelected = conditionsConfigured.any { it == condition }
            ) {
                onConditionSelected(it, condition)
            }
        }
    }
}