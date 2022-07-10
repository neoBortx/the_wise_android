package com.bortxapps.thewise.presentation.screens.options

import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.bortxapps.application.pokos.Option
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.componentes.BottomButton.GetBottomButton
import com.bortxapps.thewise.presentation.componentes.MainColumn.GetMainColumn
import com.bortxapps.thewise.presentation.componentes.TextHeader.GetTextHeader
import com.bortxapps.thewise.presentation.componentes.texfield.ImagePickerTextField
import com.bortxapps.thewise.presentation.componentes.texfield.NoEmptyTextField
import com.bortxapps.thewise.presentation.componentes.texfield.RegularTextField
import com.bortxapps.thewise.presentation.screens.options.viewmodel.OptionFormViewModel
import com.bortxapps.thewise.presentation.viewmodels.ConditionViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun OptionFormScreen(
    formViewModel: OptionFormViewModel = hiltViewModel(),
    conditionalViewModel: ConditionViewModel = hiltViewModel(),
    option: Option,
    electionId: Long,
    isEditingExistingOption: Boolean,
    isLinkingOptionsAndConnection: Boolean,
    formCompletedCallback: () -> Job
) {
    conditionalViewModel.configure(electionId)

    val conditions by conditionalViewModel.conditions.collectAsState(initial = listOf())
    val nameLabel = stringResource(id = R.string.name_option)
    val descLabel = stringResource(id = R.string.description)
    val imageLabel = stringResource(id = R.string.image)
    val urlLabel = stringResource(id = R.string.url)

    val scope = rememberCoroutineScope()

    fun onButtonFormClick() {
        formViewModel.createNewOption()
        formViewModel.clearOption()
        formCompletedCallback.invoke()
    }

    Scaffold(backgroundColor = colorResource(id = R.color.white)) {
        GetMainColumn {
            if (!isLinkingOptionsAndConnection) {
                if (isEditingExistingOption) {
                    GetTextHeader(stringResource(R.string.edit_option))
                } else {
                    GetTextHeader(stringResource(R.string.create_option))
                }
                NoEmptyTextField(nameLabel, formViewModel.optionName) { formViewModel.setName(it) }
                NoEmptyTextField(
                    descLabel,
                    formViewModel.optionDescription
                ) { formViewModel.setDescription(it) }
                RegularTextField(urlLabel, formViewModel.optionUrl) { formViewModel.setUrl(it) }
                ImagePickerTextField(
                    imageLabel,
                    formViewModel.optionImageUrl
                ) { formViewModel.setImage(it) }
                GetBottomButton(
                    {
                        scope.launch {
                            onButtonFormClick()
                        }
                    },
                    R.string.save_option,
                    formViewModel.isButtonEnabled
                )
            } else {
                GetTextHeader(stringResource(R.string.assign_conditions))
                conditions.forEach { _ ->
                    Card {
                    }
                }
            }
        }
    }

}