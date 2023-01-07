package com.bortxapps.thewise.home

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bortxapps.application.contracts.service.IConditionsAppService
import com.bortxapps.application.contracts.service.IElectionsAppService
import com.bortxapps.thewise.MainActivity
import com.bortxapps.thewise.R
import com.bortxapps.thewise.presentation.screens.elections.viewmodel.ElectionFormViewModel
import com.bortxapps.thewise.presentation.screens.home.HomeViewModel
import com.bortxapps.thewise.utils.createFilledElection
import com.bortxapps.thewise.utils.getElectionList
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
class HomeTest {


    @BindValue
    @JvmField
    val homeViewModel = mockk<HomeViewModel>(relaxed = true)

    private var electionService = mockk<IElectionsAppService>()
    private var conditionService = mockk<IConditionsAppService>()

    @BindValue
    @JvmField
    var electionFormViewModel = ElectionFormViewModel(electionService, conditionService)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @OptIn(ExperimentalMaterialApi::class)
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

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
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun home_empty_data_expect_no_questions() {

        every { homeViewModel.questions } returns flow {
            emit(listOf())
        }

        composeTestRule.onNodeWithTag("home_col_no_questions").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun home_with_questions_expect_list() {
        val numberCards = 7
        every { homeViewModel.questions } returns getElectionList(numberCards, 1, 1)
        composeTestRule.onNodeWithTag("home_col_questions").assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("home_col_question_card").assertCountEquals(numberCards)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun home_fill_and_hide_election_then_open_again_form_expect_empty() {
        //open form
        composeTestRule.onNodeWithTag("home_floating_button").performClick()
        //fill form
        composeTestRule.onNodeWithTag("election_form_name_text").performTextInput("new election")
        composeTestRule.onNodeWithTag("election_form_description_text")
            .performTextInput("new description")
        composeTestRule.onNodeWithTag("conditions_control_name_text_field")
            .performTextInput("new cond")
        composeTestRule.onNodeWithTag("conditions_control_add_button").performClick()
        composeTestRule.onNodeWithTag("conditions_control_name_text_field")
            .performTextInput("new cond 2")
        composeTestRule.onNodeWithTag("conditions_control_add_button").performClick()
        //hide form
        composeTestRule.onNodeWithTag("election_form_col").performTouchInput {
            swipeDown()
        }

        //open again
        composeTestRule.onNodeWithTag("home_floating_button").performClick()

        //tests form empty
        composeTestRule.onNodeWithTag("election_form_name_text", useUnmergedTree = true)
            .assertTextEquals("")
        composeTestRule.onNodeWithTag("election_form_description_text", useUnmergedTree = true)
            .assertTextEquals("")
        composeTestRule.onNodeWithTag("conditions_control_name_text_field", useUnmergedTree = true)
            .assertTextEquals("")
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun home_fill_and_add_election_then_open_again_form_expect_empty() {
        //open form
        composeTestRule.onNodeWithTag("home_floating_button").performClick()
        //fill election
        composeTestRule.onNodeWithTag("election_form_name_text").performTextInput("new election")
        composeTestRule.onNodeWithTag("election_form_description_text")
            .performTextInput("new description")
        composeTestRule.onNodeWithTag("conditions_control_name_text_field")
            .performTextInput("new cond")
        composeTestRule.onNodeWithTag("conditions_control_add_button").performClick()
        composeTestRule.onNodeWithTag("conditions_control_name_text_field")
            .performTextInput("new cond 2")
        composeTestRule.onNodeWithTag("conditions_control_add_button").performClick()
        //add election
        composeTestRule.onNodeWithTag("bottom_button").performClick()
        //open again
        composeTestRule.onNodeWithTag("home_floating_button").performClick()

        //test empty form
        composeTestRule.onNodeWithTag("election_form_name_text", useUnmergedTree = true)
            .assertTextEquals("")
        composeTestRule.onNodeWithTag("election_form_description_text", useUnmergedTree = true)
            .assertTextEquals("")
        composeTestRule.onNodeWithTag("conditions_control_name_text_field", useUnmergedTree = true)
            .assertTextEquals("")

    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_edit_form_validations_when_start_with_empty_data() {
        //open form
        composeTestRule.onNodeWithTag("home_floating_button").performClick()
        composeTestRule.onNodeWithTag("bottom_button").assertIsNotEnabled()

        //fill election Name
        composeTestRule.onNodeWithTag("election_form_name_text").performTextInput("new election")
        composeTestRule.onNodeWithTag("bottom_button").assertIsNotEnabled()

        //fill election description
        composeTestRule.onNodeWithTag("election_form_description_text")
            .performTextInput("new description")
        composeTestRule.onNodeWithTag("bottom_button").assertIsNotEnabled()

        //fill conditions and check that becomes enabled
        composeTestRule.onNodeWithTag("conditions_control_name_text_field")
            .performTextInput("new cond")
        composeTestRule.onNodeWithTag("conditions_control_add_button").performClick()
        composeTestRule.onNodeWithTag("bottom_button").assertIsNotEnabled()

        composeTestRule.onNodeWithTag("conditions_control_name_text_field")
            .performTextInput("new cond 2")
        composeTestRule.onNodeWithTag("conditions_control_add_button").performClick()
        composeTestRule.onNodeWithTag("bottom_button").assertIsEnabled()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun click_election_card_expect_election_info_screen() {
        every { homeViewModel.questions } returns getElectionList(1, 1, 1)

        val name = "new home"
        val description = "with balcony"
        val numOptions = 3
        val numConditions = 3
        val testElection = createFilledElection(
            1L,
            name,
            description,
            numOptions,
            numConditions
        )

        //prepare a custom election for the test
        every { electionService.getElection(1L) } returns flow { emit(testElection) }
        composeTestRule.onAllNodesWithTag("home_col_question_card").onFirst().performClick()
        composeTestRule.onNodeWithTag("election_info_main_column").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun click_election_card_expand_icon_expect_card_size_modified() {
        val numberCards = 1
        val numberConditions = 5
        every { homeViewModel.questions } returns getElectionList(numberCards, 1, numberConditions)

        composeTestRule.onNodeWithTag("home_col_questions").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_col_question_card_size_button").performClick()
        composeTestRule.onNodeWithTag("home_question_card_detail_col", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("simple_badge_condition", useUnmergedTree = true)
            .assertCountEquals(numberConditions)

        composeTestRule.onAllNodesWithTag("home_col_question_card_size_button").onFirst()
            .performClick()
        composeTestRule.onNodeWithTag("home_question_card_detail_col", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_election_card_without_option_expect_no_option_message() {
        every { homeViewModel.questions } returns getElectionList(1, 0, 1)

        composeTestRule.onNodeWithTag("home_col_question_card_winning_icon", useUnmergedTree = true)
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag(
            "home_col_question_card_winning_option_name",
            useUnmergedTree = true
        )
            .assertTextEquals(composeTestRule.activity.getString(R.string.no_options_configured))
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun test_election_card_with_option_expect_winning_info() {
        every { homeViewModel.questions } returns getElectionList(1, 1, 1)

        composeTestRule.onNodeWithTag("home_col_question_card_winning_icon", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag(
            "home_col_question_card_winning_option_name",
            useUnmergedTree = true
        )
            .assertTextEquals("option name-1")
    }
}