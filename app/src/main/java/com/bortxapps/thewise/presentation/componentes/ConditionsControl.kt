package com.bortxapps.thewise.presentation.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bortxapps.application.pokos.ConditionWeight
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.texfield.ConditionBadge
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModelPreview
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.IElectionFormViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@ExperimentalMaterialApi
@Composable
fun GetConditionsControl(electionFormViewModel: IElectionFormViewModel) {
    val maxCharacters = 20
    var conditionName by remember { mutableStateOf("") }
    var conditionWeight by remember { mutableStateOf(ConditionWeight.LOW) }
    var selectedIndex by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var buttonEnabled by remember { mutableStateOf(false) }

    val items = ConditionWeight.values()
    val conditions = electionFormViewModel.conditions
    val focusManager = LocalFocusManager.current

    @Composable
    fun GetConditionWeight(weight: ConditionWeight): String {
        return when (weight) {
            ConditionWeight.LOW -> stringResource(R.string.meh)
            ConditionWeight.MEDIUM -> stringResource(R.string.wish)
            ConditionWeight.MUST -> stringResource(R.string.must)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    focusedLabelColor = colorResource(id = R.color.yellow_800),
                    unfocusedLabelColor = colorResource(id = R.color.yellow_800),
                    focusedIndicatorColor = colorResource(id = R.color.yellow_800),
                    unfocusedIndicatorColor = colorResource(id = R.color.yellow_800),
                    backgroundColor = colorResource(id = R.color.white)
                ),
                label = { Text(text = stringResource(id = R.string.requisite_name)) },
                modifier = Modifier
                    .background(color = colorResource(id = R.color.white))
                    .weight(1f),
                value = conditionName,
                singleLine = true,
                onValueChange = {
                    if (it.length < maxCharacters) {
                        conditionName = it
                        buttonEnabled = conditionName.isNotBlank()
                    }
                },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .width(130.dp)
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.TopStart)
                    .padding(horizontal = 15.dp)
            ) {
                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        focusedLabelColor = colorResource(id = R.color.yellow_800),
                        unfocusedLabelColor = colorResource(id = R.color.yellow_800),
                        focusedIndicatorColor = colorResource(id = R.color.yellow_800),
                        unfocusedIndicatorColor = colorResource(id = R.color.yellow_800),
                        backgroundColor = colorResource(id = R.color.white)
                    ),
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.white))
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clickable(onClick = { expanded = true }),
                    value = GetConditionWeight(conditionWeight),
                    singleLine = true,
                    readOnly = true,
                    onValueChange = {},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    items.forEachIndexed { index, s ->
                        DropdownMenuItem(onClick = {
                            selectedIndex = index
                            expanded = false
                            conditionWeight = items[selectedIndex]
                        }) {
                            Text(text = GetConditionWeight(s))
                        }
                    }
                }
            }

            Button(modifier = Modifier.size(50.dp), enabled = buttonEnabled, onClick = {
                electionFormViewModel.addCondition(conditionName, conditionWeight)
                focusManager.clearFocus()
                conditionName = ""
            }) {
                Icon(
                    imageVector = Icons.Default.Add, "", modifier = Modifier.size(40.dp)
                )
            }
        }
        if (conditions.isEmpty()) {
            Text(
                text = "No conditions configured jet",
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
            )
        } else {
            FlowRow(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 10.dp),
                mainAxisAlignment = MainAxisAlignment.Start,
                mainAxisSize = SizeMode.Expand,
                crossAxisSpacing = 12.dp,
                mainAxisSpacing = 8.dp
            ) {
                conditions.forEach { condition ->
                    ConditionBadge(condition.name, condition.weight) {
                        electionFormViewModel.deleteCondition(conditionId = condition.id)
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun ShowControl() {
    GetConditionsControl(ElectionFormViewModelPreview())
}

