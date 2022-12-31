package com.bortxapps.thewise.infrastructure.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.bortxapps.thewise.infrastructure.database.TheWiseDatabase
import com.bortxapps.thewise.infrastructure.model.ElectionEntity
import com.bortxapps.thewise.infrastructure.utils.TestDispatcherRule
import com.bortxapps.thewise.infrastructure.utils.createTestConditions
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
class ConditionDaoTest {


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
    fun insert_a_bunch_of_conditions_expect_all_items() = runTest(UnconfinedTestDispatcher()) {
        val newElection = ElectionEntity(1, "Election", "Description")
        val newConditions = createTestConditions(1, "testCondition", 25)

        dataBase.electionDao().addElection(newElection)

        newConditions.forEach {
            dataBase.conditionDao().addCondition(it)
        }


        val flow = dataBase.conditionDao().getAllConditions()
        flow.test {
            val list = awaitItem()
            assertEquals(list.count(), newConditions.count())
            assertEquals(list.map { it }.toSet(), newConditions.toSet())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insert_a_bunch_of_option_with_no_election_expect_exception() =
        runTest(UnconfinedTestDispatcher()) {

            val newConditions = createTestConditions(1, "testCondition", 1)
            var exceptionHandled = false

            try {
                newConditions.forEach {
                    dataBase.conditionDao().addCondition(it)
                }
            } catch (ex: Exception) {
                exceptionHandled = true
            }

            assertTrue(exceptionHandled)

            val flow = dataBase.conditionDao().getAllConditions()
            flow.test {
                val list = awaitItem()
                assertTrue(list.isEmpty())
            }
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insert_a_repeated_option_expect_aborted() = runTest(UnconfinedTestDispatcher()) {

        val newElection = ElectionEntity(1, "Election", "Description")
        val newConditions = createTestConditions(1, "testCondition", 1)

        dataBase.electionDao().addElection(newElection)

        val firstRes = dataBase.conditionDao().addCondition(newConditions.first())
        //insert again
        val secondRes = dataBase.conditionDao().addCondition(newConditions.first())

        assertEquals(newConditions.first().condId, firstRes)
        assertEquals(-1, secondRes)


        val flow = dataBase.conditionDao().getAllConditions()
        flow.test {
            val list = awaitItem()
            assertEquals(1, list.count())
            assertEquals(list.map { it }.first(), newConditions.first())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun remove_delete_first_question_from_two_expect_a_list_with_one_item() =
        runTest(UnconfinedTestDispatcher()) {

            val newElection = ElectionEntity(1, "Election", "Description")
            val newConditions = createTestConditions(1, "testCondition", 2)

            dataBase.electionDao().addElection(newElection)


            newConditions.forEach {
                dataBase.conditionDao().addCondition(it)
            }

            dataBase.conditionDao().deleteCondition(newConditions.first())

            dataBase.conditionDao().getAllConditions().test {
                val list = awaitItem()
                assertEquals(1, list.count())
                assertEquals(list.map { it }.first(), newConditions[1])
            }

            dataBase.conditionDao().getCondition(newConditions[0].condId).test {
                assertNull(awaitItem())
            }

            dataBase.conditionDao().getCondition(newConditions[1].condId).test {
                assertEquals(newConditions[1], awaitItem())
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun remove_delete_the_only_one_question_expect_a_empty_list() =
        runTest(UnconfinedTestDispatcher()) {
            val newElection = ElectionEntity(1, "Election", "Description")
            val newConditions = createTestConditions(1, "testCondition", 1)

            dataBase.electionDao().addElection(newElection)

            newConditions.forEach {
                dataBase.conditionDao().addCondition(it)
            }

            newConditions.forEach {
                dataBase.conditionDao().deleteCondition(it)
            }

            dataBase.conditionDao().getAllConditions().test {
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

        val newConditions = createTestConditions(newElections.first().electId, "testCondition", 1)
        newConditions.forEach {
            dataBase.conditionDao().addCondition(it)
        }

        val updatedOption = newConditions.first().copy(name = "updated question")
        dataBase.conditionDao().updateCondition(updatedOption)

        dataBase.conditionDao().getAllConditions().test {
            val list = awaitItem()
            assertEquals(1, list.count())
            assertEquals(updatedOption, list.first())
        }

        dataBase.conditionDao().getCondition(newConditions[0].condId).test {
            val item = awaitItem()
            assertEquals(updatedOption, item)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun update_not_existent_element_expect_fail() = runTest(UnconfinedTestDispatcher()) {
        val newElections = createTestQuestions("test!uestion", 1)
        newElections.forEach {
            dataBase.electionDao().addElection(it)
        }

        val newConditions = createTestConditions(newElections.first().electId, "testCondition", 1)
        newConditions.forEach {
            dataBase.conditionDao().addCondition(it)
        }

        val updatedOption =
            newConditions.first().copy(condId = 678687298L, name = "updated option")
        dataBase.conditionDao().updateCondition(updatedOption)

        dataBase.conditionDao().getAllConditions().test {
            val list = awaitItem()
            assertEquals(1, list.count())
            assertEquals(newConditions.first(), list.first())
        }

        dataBase.conditionDao().getCondition(newConditions[0].condId).test {
            val item = awaitItem()
            assertEquals(newConditions.first(), item)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun remove_election_expect_all_its_options_removed() = runTest(UnconfinedTestDispatcher()) {
        val newElections = createTestQuestions("testQuestion", 2)
        newElections.forEach {
            dataBase.electionDao().addElection(it)
        }

        val newConditions = createTestConditions(newElections.first().electId, "testCondition", 5)
        newConditions.forEach {
            dataBase.conditionDao().addCondition(it)
        }

        val newConditions2 = createTestConditions(newElections[1].electId, "testCondition2", 7)
        newConditions2.forEach {
            dataBase.conditionDao().addCondition(it)
        }

        dataBase.electionDao().deleteElection(newElections.first())

        dataBase.conditionDao().getConditionsFromElection(newElections.first().electId).test {
            val list = awaitItem()
            assertTrue(list.isEmpty())
        }

        dataBase.conditionDao().getConditionsFromElection(newElections[1].electId).test {
            val list = awaitItem()
            assertEquals(newConditions2.toSet(), list.map { it }.toSet())

        }
    }
}