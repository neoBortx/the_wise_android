package com.bortxapps.thewise.questionInfo

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.application.contracts.service.IOptionsAppService
import com.bortxapps.thewise.presentation.screens.elections.ElectionInfoScreen
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionInfoViewModel
import com.bortxapps.thewise.presentation.screens.utils.HiltActivity
import com.bortxapps.thewise.ui.theme.TheWiseTheme
import com.bortxapps.thewise.utils.createFilledElection
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ElectionInfoTest {

    private var electionService = mockk<IElectionsAppService>()
    private var conditionService = mockk<IConditionsAppService>()
    private var optionService = mockk<IOptionsAppService>()
    private var navHostController =
        TestNavHostController(ApplicationProvider.getApplicationContext())

    @BindValue
    @JvmField
    var electionFormViewModel = ElectionFormViewModel(electionService, conditionService)

    @BindValue
    @JvmField
    var electionInfoViewModel = ElectionInfoViewModel(electionService)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltActivity>()

    @Composable
    fun getElectionFormViewModel() = spyk(hiltViewModel<ElectionFormViewModel>())

    @Before
    fun setup() {
        hiltRule.inject()

        coEvery { electionService.addElection(any()) } returns 5
        coEvery { electionService.deleteElection(any()) } returns Unit
        coEvery { electionService.updateElection(any()) } returns Unit


        coEvery { conditionService.addCondition(any()) } returns Unit
        coEvery { conditionService.deleteCondition(any()) } returns Unit
        coEvery { conditionService.updateCondition(any()) } returns Unit

        coEvery { optionService.addOption(any()) } returns Unit
        coEvery { optionService.deleteOption(any()) } returns Unit
        coEvery { optionService.updateOption(any()) } returns Unit
    }


    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_election_info_with_no_options_expect_no_option_message() {

        val numConditions = 6
        val election =
            createFilledElection(5, "Name election", "description election", 0, numConditions)

        every { electionService.getElection(electionId = election.id) } returns flow { emit(election) }

        composeTestRule.setContent {
            TheWiseTheme {
                ElectionInfoScreen(
                    electionInfoViewModel,
                    electionFormViewModel,
                    election.id,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }

        composeTestRule.onAllNodesWithTag("simple_badge_condition").assertCountEquals(numConditions)
        composeTestRule.onNodeWithTag("election_info_description_label")
            .assertTextEquals(election.description)
        composeTestRule.onNodeWithTag("top_title_test").assertTextEquals(election.name)
        composeTestRule.onNodeWithTag("no_option_col").assertIsDisplayed()
        composeTestRule.onNodeWithTag("no_option_col_text").assertIsDisplayed()
        composeTestRule.onNodeWithTag("no_option_col_image").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_election_info_with_winning_option_expect_display_option_control() {
        val numConditions = 3
        val election =
            createFilledElection(5, "Name election", "Description election", 1, numConditions)

        every { electionService.getElection(5) } returns flow { emit(election) }

        composeTestRule.setContent {
            TheWiseTheme {
                ElectionInfoScreen(
                    electionInfoViewModel,
                    electionFormViewModel,
                    election.id,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }

        composeTestRule.onAllNodesWithTag("simple_badge_condition").assertCountEquals(numConditions)
        composeTestRule.onNodeWithTag("election_info_description_label")
            .assertTextEquals(election.description)
        composeTestRule.onNodeWithTag("top_title_test").assertTextEquals(election.name)
        composeTestRule.onNodeWithTag("election_info_winning_option_col").assertIsDisplayed()
        composeTestRule.onNodeWithTag(
            "election_info_winning_option_instructions_label",
            useUnmergedTree = true
        )
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag(
            "election_info_winning_option_instruction_icon",
            useUnmergedTree = true
        )
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_election_info_show_winning_option_expect_display_option_control() {
        val numConditions = 3
        val election =
            createFilledElection(5, "Name election", "Description election", 1, numConditions)

        every { electionService.getElection(5) } returns flow { emit(election) }

        composeTestRule.setContent {
            TheWiseTheme {
                ElectionInfoScreen(
                    electionInfoViewModel,
                    electionFormViewModel,
                    election.id,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }

        composeTestRule.onNodeWithTag("election_info_winning_option_col").performClick()

        composeTestRule.onNodeWithTag("option_card_name_text", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("option_card_winning_icon", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("option_card_expand_button", useUnmergedTree = true)
            .performClick()
        composeTestRule.onNodeWithTag("option_card_matching_conditions_row", useUnmergedTree = true)
            .onChildren().assertCountEquals(numConditions)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_election_info_edit_current_election() {
        val numConditions = 6
        val election =
            createFilledElection(5, "Name election", "Description election", 1, numConditions)

        every { electionService.getElection(5) } returns flow { emit(election) }

        composeTestRule.setContent {
            TheWiseTheme {
                ElectionInfoScreen(
                    electionInfoViewModel,
                    electionFormViewModel,
                    election.id,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }

        composeTestRule.onNodeWithTag("menu_button_edit").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("election_form_name_text", useUnmergedTree = true)
            .assertTextEquals(election.name)
        composeTestRule.onNodeWithTag("election_form_description_text", useUnmergedTree = true)
            .assertTextEquals(election.description)
        composeTestRule.onAllNodesWithTag("removable_badge_condition", useUnmergedTree = true)
            .assertCountEquals(numConditions)

        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsNotEnabled()
        composeTestRule.onNodeWithTag("election_form_name_text", useUnmergedTree = true)
            .performTextInput("ajdhgfksjfsk")
        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsEnabled()
        composeTestRule.onNodeWithTag("election_form_name_text", useUnmergedTree = true)
            .performTextReplacement("")
        composeTestRule.onNodeWithTag("bottom_button", useUnmergedTree = true).assertIsNotEnabled()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_election_info_edit_current_election_expect_form_filled() {
        val numConditions = 12
        val election =
            createFilledElection(5, "Name election", "Description election", 1, numConditions)

        every { electionService.getElection(5) } returns flow { emit(election) }

        composeTestRule.setContent {
            TheWiseTheme {
                ElectionInfoScreen(
                    electionInfoViewModel,
                    electionFormViewModel,
                    election.id,
                    {},
                    {},
                    navController = navHostController
                )
            }
        }

        composeTestRule.onNodeWithTag("menu_button_delete").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("delete_dialog", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("delete_dialog_forget", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithTag("delete_dialog", useUnmergedTree = true).assertDoesNotExist()

        composeTestRule.onNodeWithTag("menu_button_delete").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("delete_dialog", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("delete_dialog_keep", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithTag("delete_dialog", useUnmergedTree = true).assertDoesNotExist()
    }
}