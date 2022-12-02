package com.harisha.hackerstories

import com.harisha.hackerstories.api.NetworkState
import com.harisha.hackerstories.api.RetrofitService
import com.harisha.hackerstories.model.Stories
import com.harisha.hackerstories.repository.MainRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class MainRepositoryTest {

    lateinit var mainRepository: MainRepository

    @Mock
    lateinit var apiService: RetrofitService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mainRepository = MainRepository(apiService)
    }

    @Test
    fun `get all movie test`() {
        runBlocking {
            Mockito.`when`(apiService.getAllStories()).thenReturn(Response.success(listOf<Stories>()))
            val response = mainRepository.getAllStories()
            assertEquals(listOf<Stories>(),  NetworkState.Success(response).data)
        }

    }

}