package com.bortxapps.thewise.home

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.MainActivity
import com.bortxapps.thewise.presentation.screens.home.HomeViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
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

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @OptIn(ExperimentalMaterialApi::class)
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {

        hiltRule.inject()
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
        every { homeViewModel.questions } returns flow {
            emit(
                listOf(
                    Election(
                        id = 45454,
                        name = "Election 1",
                        description = "desc 5",
                        options = listOf(),
                        conditions = listOf()
                    )
                )
            )
        }

        composeTestRule.onNodeWithTag("home_col_questions").assertIsDisplayed()
    }
}