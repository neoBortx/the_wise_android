package com.bortxapps.thewise.presentation.screens.options

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.BottomButton.GetBottomButton
import com.bortxapps.thewise.presentation.componentes.MainColumn.GetMainColumn
import com.bortxapps.thewise.presentation.componentes.OptionFilePicker.ImagePickerField
import com.bortxapps.thewise.presentation.componentes.TextError.GetTextError
import com.bortxapps.thewise.presentation.componentes.TextHeader.GetTextHeader
import com.bortxapps.thewise.presentation.componentes.texfield.NoEmptyTextField
import com.bortxapps.thewise.presentation.componentes.texfield.SelectableConditionBadge
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionFormViewModel
import com.bortxapps.thewise.presentation.viewmodels.ConditionViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun OptionFormScreen(
    formViewModel: OptionFormViewModel = hiltViewModel(),
    conditionalViewModel: ConditionViewModel = hiltViewModel(),
    electionId: Long,
    formCompletedCallback: () -> Job
) {
    conditionalViewModel.configure(electionId)

    val nameLabel = stringResource(id = R.string.name_option)
    val imageLabel = stringResource(id = R.string.image)

    val scope = rememberCoroutineScope()

    fun onButtonFormClick() {
        formViewModel.createNewOption()
        formViewModel.clearOption()
        formCompletedCallback.invoke()
    }

    Scaffold(backgroundColor = colorResource(id = R.color.white)) {
        GetMainColumn {
            Divider(
                color = colorResource(R.color.dark_text),
                thickness = 1.dp,
                modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 0.dp)
            )
            GetTextHeader(stringResource(R.string.create_option))
            NoEmptyTextField(nameLabel, formViewModel.optionName) { formViewModel.setName(it) }
            ImagePickerField(
                imageLabel,
                formViewModel.optionImageUrl
            )
            GetTextHeader(stringResource(R.string.assign_requisites))
            if (formViewModel.configuredConditions.isEmpty()) {
                GetTextError(stringResource(R.string.tooltip_select_requisites))
            }
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
                formViewModel.allConditions.forEach { condition ->
                    SelectableConditionBadge(
                        label = condition.name,
                        weight = condition.weight,
                        isSelected = formViewModel.configuredConditions.any { it == condition }
                    ) {
                        formViewModel.selectCondition(it, condition)
                    }
                }
            }

            GetBottomButton(
                {
                    scope.launch {
                        onButtonFormClick()
                    }
                },
                R.string.save_option,
                formViewModel.isButtonEnabled
            )
        }
    }

}