package com.harisha.hackerstories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.harisha.hackerstories.api.NetworkState
import com.harisha.hackerstories.api.RetrofitService
import com.harisha.hackerstories.model.Stories
import com.harisha.hackerstories.repository.MainRepository
import com.harisha.hackerstories.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainViewModelTest {


    private val testDispatcher = TestCoroutineDispatcher()
    lateinit var mainViewModel: MainViewModel

    lateinit var mainRepository: MainRepository

    @Mock
    lateinit var apiService: RetrofitService

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        mainRepository = mock(MainRepository::class.java)
        mainViewModel = MainViewModel(mainRepository)
    }

    @Test
    fun getAllMoviesTest() {
        runBlocking {
            Mockito.`when`(mainRepository.getAllStories())
                .thenReturn(NetworkState.Success(listOf<Stories>(Stories("Story", "", "new"))))
            mainViewModel.getAllStories()
            val result = mainViewModel.storiesList.getOrAwaitValue()
            assertEquals(listOf<Stories>(Stories("Story", "", "new")), result)
        }
    }


    @Test
    fun `empty movie list test`() {
        runBlocking {
            Mockito.`when`(mainRepository.getAllStories())
                .thenReturn(NetworkState.Success(listOf<Stories>()))
            mainViewModel.getAllStories()
            val result = mainViewModel.storiesList.getOrAwaitValue()
            assertEquals(listOf<Stories>(), result)
        }
    }

}