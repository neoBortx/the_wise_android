package com.bortxapps.thewise.infrastructure.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.bortxapps.thewise.infrastructure.database.TheWiseDatabase
import com.bortxapps.thewise.infrastructure.model.ElectionEntity
import com.bortxapps.thewise.infrastructure.utils.TestDispatcherRule
import com.bortxapps.thewise.infrastructure.utils.createTestQuestions
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class ElectionDaoTest {


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
    fun insert_a_bunch_of_items_exepct_all_items() = runTest(UnconfinedTestDispatcher()) {
        val newElections = createTestQuestions("test!uestion", 25)

        newElections.forEach {
            dataBase.electionDao().addElection(it)
        }

        newElections.forEach {
            assertEquals(
                dataBase.electionDao().getElection(it.electId).first()?.election, it
            )
        }


        val flow = dataBase.electionDao().getElections()
        flow.test {
            val list = awaitItem()
            assertEquals(list.count(), newElections.count())
            assertEquals(list.map { it.election }.toSet(), newElections.toSet())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insert_a_repeated_item_expect_aborted() = runTest(UnconfinedTestDispatcher()) {
        val newElection = ElectionEntity(32L, "Name", "description of")

        val firstRes = dataBase.electionDao().addElection(newElection)

        //insert again
        val secondRes = dataBase.electionDao().addElection(newElection)

        assertEquals(32L, firstRes)
        assertEquals(-1, secondRes)


        val flow = dataBase.electionDao().getElections()
        flow.test {
            val list = awaitItem()
            assertEquals(1, list.count())
            assertEquals(list.map { it.election }.first(), newElection)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun remove_delete_first_question_from_two_expect_a_list_with_one_item() =
        runTest(UnconfinedTestDispatcher()) {
            val newElections = createTestQuestions("test!uestion", 2)

            newElections.forEach {
                dataBase.electionDao().addElection(it)
            }

            dataBase.electionDao().deleteElection(newElections.first())

            dataBase.electionDao().getElections().test {
                val list = awaitItem()
                assertEquals(1, list.count())
                assertEquals(list.map { it.election }.first(), newElections[1])
            }

            dataBase.electionDao().getElection(newElections[0].electId).test {
                assertNull(awaitItem())
            }

            dataBase.electionDao().getElection(newElections[1].electId).test {
                assertEquals(newElections[1], awaitItem()?.election)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun remove_delete_the_only_one_question_expect_a_list_with_one_item() =
        runTest(UnconfinedTestDispatcher()) {
            val newElections = createTestQuestions("test!uestion", 1)

            newElections.forEach {
                dataBase.electionDao().addElection(it)
            }

            dataBase.electionDao().deleteElection(newElections.first())

            dataBase.electionDao().getElections().test {
                val list = awaitItem()
                assertTrue(list.isEmpty())
            }

            dataBase.electionDao().getElection(newElections[0].electId).test {
                assertNull(awaitItem())
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun update_element_expected_new_element_added() = runTest(UnconfinedTestDispatcher()) {
        val newElections = createTestQuestions("test!uestion", 1)

        newElections.forEach {
            dataBase.electionDao().addElection(it)
        }

        val updatedElection = newElections.first().copy(name = "updated question")
        dataBase.electionDao().updateElection(updatedElection)

        dataBase.electionDao().getElections().test {
            val list = awaitItem()
            assertEquals(1, list.count())
            assertEquals(updatedElection, list.first().election)
        }

        dataBase.electionDao().getElection(newElections[0].electId).test {
            val item = awaitItem()
            assertEquals(updatedElection, item?.election)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun update_not_existent_element_expect_fail() = runTest(UnconfinedTestDispatcher()) {
        val newElections = createTestQuestions("test!uestion", 1)

        newElections.forEach {
            dataBase.electionDao().addElection(it)
        }

        val updatedElection =
            newElections.first().copy(electId = 678687298L, name = "updated question")
        dataBase.electionDao().updateElection(updatedElection)

        dataBase.electionDao().getElections().test {
            val list = awaitItem()
            assertEquals(1, list.count())
            assertEquals(newElections.first(), list.first().election)
        }

        dataBase.electionDao().getElection(newElections[0].electId).test {
            val item = awaitItem()
            assertEquals(newElections.first(), item?.election)
        }
    }
}