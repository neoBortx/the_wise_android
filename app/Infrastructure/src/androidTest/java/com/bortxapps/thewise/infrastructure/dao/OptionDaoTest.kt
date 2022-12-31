package com.bortxapps.thewise.infrastructure.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.bortxapps.thewise.infrastructure.database.TheWiseDatabase
import com.bortxapps.thewise.infrastructure.model.ConditionInOptionCrossRef
import com.bortxapps.thewise.infrastructure.model.ElectionEntity
import com.bortxapps.thewise.infrastructure.utils.TestDispatcherRule
import com.bortxapps.thewise.infrastructure.utils.createTestConditions
import com.bortxapps.thewise.infrastructure.utils.createTestOptions
import com.bortxapps.thewise.infrastructure.utils.createTestQuestions
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration test for Room data base
 */
@RunWith(AndroidJUnit4::class)
class OptionDaoTest {


    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var dataBase: TheWiseDatabase

    @Before
    fun setDatabase() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TheWiseDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() {
        dataBase.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insert_a_bunch_of_option_expect_all_items() = runTest(UnconfinedTestDispatcher()) {
        val newElection = ElectionEntity(1, "Election", "Description")
        val newOptions = createTestOptions(1, "testOption", 25)

        dataBase.electionDao().addElection(newElection)

        newOptions.forEach {
            dataBase.optionDao().addOption(it)
        }


        val flow = dataBase.optionDao().getOptions()
        flow.test {
            val list = awaitItem()
            assertEquals(list.count(), newOptions.count())
            assertEquals(list.map { it.option }.toSet(), newOptions.toSet())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insert_a_bunch_of_option_with_no_election_expect_exception() =
        runTest(UnconfinedTestDispatcher()) {

            val newOptions = createTestOptions(1, "testOption", 1)
            var exceptionHandled = false

            try {
                newOptions.forEach {
                    dataBase.optionDao().addOption(it)
                }
            } catch (ex: Exception) {
                exceptionHandled = true
            }

            assertTrue(exceptionHandled)

            val flow = dataBase.optionDao().getOptions()
            flow.test {
                val list = awaitItem()
                assertTrue(list.isEmpty())
            }
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insert_a_repeated_option_expect_aborted() = runTest(UnconfinedTestDispatcher()) {

        val newElection = ElectionEntity(1, "Election", "Description")
        val newOption = createTestOptions(1, "testOption", 1)

        dataBase.electionDao().addElection(newElection)

        val firstRes = dataBase.optionDao().addOption(newOption.first())
        //insert again
        val secondRes = dataBase.optionDao().addOption(newOption.first())

        assertEquals(newOption.first().optId, firstRes)
        assertEquals(-1, secondRes)


        val flow = dataBase.optionDao().getOptions()
        flow.test {
            val list = awaitItem()
            assertEquals(1, list.count())
            assertEquals(list.map { it.option }.first(), newOption.first())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun remove_delete_first_question_from_two_expect_a_list_with_one_item() =
        runTest(UnconfinedTestDispatcher()) {

            val newElection = ElectionEntity(1, "Election", "Description")
            val newOptions = createTestOptions(1, "testOption", 2)

            dataBase.electionDao().addElection(newElection)


            newOptions.forEach {
                dataBase.optionDao().addOption(it)
            }

            dataBase.optionDao().deleteOption(newOptions.first())

            dataBase.optionDao().getOptions().test {
                val list = awaitItem()
                assertEquals(1, list.count())
                assertEquals(list.map { it.option }.first(), newOptions[1])
            }

            dataBase.optionDao().getOption(newOptions[0].optId).test {
                assertNull(awaitItem())
            }

            dataBase.optionDao().getOption(newOptions[1].optId).test {
                assertEquals(newOptions[1], awaitItem()?.option)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun remove_delete_the_only_one_question_expect_a_empty_list() =
        runTest(UnconfinedTestDispatcher()) {
            val newElection = ElectionEntity(1, "Election", "Description")
            val newOptions = createTestOptions(1, "testOption", 1)

            dataBase.electionDao().addElection(newElection)

            newOptions.forEach {
                dataBase.optionDao().addOption(it)
            }

            newOptions.forEach {
                dataBase.optionDao().deleteOption(it)
            }

            dataBase.optionDao().getOptions().test {
                val list = awaitItem()
                assertTrue(list.isEmpty())
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun update_element_expected_new_element_added() = runTest(UnconfinedTestDispatcher()) {
        val newElections = createTestQuestions("test!uestion", 1)
        newElections.forEach {
            dataBase.electionDao().addElection(it)
        }

        val newOption = createTestOptions(newElections.first().electId, "testOption", 1)
        newOption.forEach {
            dataBase.optionDao().addOption(it)
        }

        val updatedOption = newOption.first().copy(name = "updated question")
        dataBase.optionDao().updateOption(updatedOption)

        dataBase.optionDao().getOptions().test {
            val list = awaitItem()
            assertEquals(1, list.count())
            assertEquals(updatedOption, list.first().option)
        }

        dataBase.optionDao().getOption(newOption[0].optId).test {
            val item = awaitItem()
            assertEquals(updatedOption, item?.option)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun update_not_existent_element_expect_fail() = runTest(UnconfinedTestDispatcher()) {
        val newElections = createTestQuestions("test!uestion", 1)
        newElections.forEach {
            dataBase.electionDao().addElection(it)
        }

        val newOption = createTestOptions(newElections.first().electId, "testOption", 1)
        newOption.forEach {
            dataBase.optionDao().addOption(it)
        }

        val updatedOption =
            newOption.first().copy(optId = 678687298L, name = "updated option")
        dataBase.optionDao().updateOption(updatedOption)

        dataBase.optionDao().getOptions().test {
            val list = awaitItem()
            assertEquals(1, list.count())
            assertEquals(newOption.first(), list.first().option)
        }

        dataBase.optionDao().getOption(newOption[0].optId).test {
            val item = awaitItem()
            assertEquals(newOption.first(), item?.option)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun remove_election_expect_all_its_options_removed() = runTest(UnconfinedTestDispatcher()) {
        val newElections = createTestQuestions("testQuestion", 2)
        newElections.forEach {
            dataBase.electionDao().addElection(it)
        }

        val newOption = createTestOptions(newElections.first().electId, "testOption", 5)
        newOption.forEach {
            dataBase.optionDao().addOption(it)
        }

        val newOption2 = createTestOptions(newElections[1].electId, "testOption", 7)
        newOption2.forEach {
            dataBase.optionDao().addOption(it)
        }

        dataBase.electionDao().deleteElection(newElections.first())

        dataBase.optionDao().getOptionsFromElection(newElections.first().electId).test {
            val list = awaitItem()
            assertTrue(list.isEmpty())
        }

        dataBase.optionDao().getOptionsFromElection(newElections[1].electId).test {
            val list = awaitItem()
            assertEquals(newOption2.toSet(), list.map { it.option }.toSet())

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insert_condition_in_option_expect_condition_added() = runTest(UnconfinedTestDispatcher()) {
        val newElections = createTestQuestions("testQuestion", 1)
        newElections.forEach {
            dataBase.electionDao().addElection(it)
        }

        val newOption = createTestOptions(newElections.first().electId, "testOption", 5)
        newOption.forEach {
            dataBase.optionDao().addOption(it)
        }

        val newConditions = createTestConditions(newElections.first().electId, "testCondition", 2)
        val optionForTest = newOption.first()

        newConditions.forEach {
            dataBase.conditionDao().addCondition(it)
            dataBase.optionDao().insertConditionInOption(
                ConditionInOptionCrossRef(
                    optionForTest.optId,
                    it.condId
                )
            )
        }

        dataBase.optionDao().getOptionsFromElection(newElections.first().electId).test {
            val list = awaitItem()
            assertEquals(
                newConditions.toSet(),
                list.first { it.option.optId == optionForTest.optId }.conditions.toSet()
            )
            assertTrue(list[1].conditions.isEmpty())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun remove_condition_from_option_expect_condition_removed() =
        runTest(UnconfinedTestDispatcher()) {
            val newElections = createTestQuestions("testQuestion", 1)
            newElections.forEach {
                dataBase.electionDao().addElection(it)
            }

            val newOption = createTestOptions(newElections.first().electId, "testOption", 5)
            newOption.forEach {
                dataBase.optionDao().addOption(it)
            }

            val newConditions =
                createTestConditions(newElections.first().electId, "testCondition", 5)

            val optionForTest = newOption.first()

            newConditions.forEach {
                dataBase.conditionDao().addCondition(it)
                dataBase.optionDao().insertConditionInOption(
                    ConditionInOptionCrossRef(
                        optionForTest.optId,
                        it.condId
                    )
                )
            }

            val conditionToRemove = newConditions.first()

            dataBase.optionDao().removeConditionFromOption(
                ConditionInOptionCrossRef(
                    optionForTest.optId,
                    conditionToRemove.condId
                )
            )

            dataBase.optionDao().getOption(optionForTest.optId).test {
                val opt = awaitItem()
                assertEquals(4, opt?.conditions?.count())
                opt?.conditions?.none { it.condId == conditionToRemove.condId }
                    ?.let { assertTrue(it) }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun clear_conditions_from_option_expect_option_without_conditions() =
        runTest(UnconfinedTestDispatcher()) {
            val newElections = createTestQuestions("testQuestion", 1)
            newElections.forEach {
                dataBase.electionDao().addElection(it)
            }

            val newOption = createTestOptions(newElections.first().electId, "testOption", 5)
            newOption.forEach {
                dataBase.optionDao().addOption(it)
            }

            val newConditions =
                createTestConditions(newElections.first().electId, "testCondition", 5)

            val optionForTest = newOption.first()

            newConditions.forEach {
                dataBase.conditionDao().addCondition(it)
                dataBase.optionDao().insertConditionInOption(
                    ConditionInOptionCrossRef(
                        optionForTest.optId,
                        it.condId
                    )
                )
            }

            dataBase.optionDao().clearConditionsOfOption(optionForTest.optId)

            dataBase.optionDao().getOption(optionForTest.optId).test {
                val opt = awaitItem()
                opt?.conditions?.isEmpty()?.let { assertTrue(it) }
            }
        }
}