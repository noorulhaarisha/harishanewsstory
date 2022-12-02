package com.harisha.hackerstories.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harisha.hackerstories.repository.MainRepository
import com.harisha.hackerstories.api.NetworkState
import com.harisha.hackerstories.data.StoryDatabase
import com.harisha.hackerstories.model.Stories
import com.harisha.hackerstories.model.StoryEntity
import com.harisha.hackerstories.repository.StoryRepository
import kotlinx.coroutines.*

class MainViewModel constructor(private val mainRepository: MainRepository,
                                private val application: Application) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val storiesID = arrayOf( 33764935, 33762743, 33761567, 33763132, 33768153, 33768081,
        33755016, 33763398, 33747554, 33718124, 33759732, 33754133, 33765124, 33762370, 33764844)

    val errorMessage: LiveData<String>
    get() = _errorMessage

    val storiesList = MutableLiveData<List<Stories>>()
    val stories = ArrayList<Stories>()
    val readAllStories: LiveData<List<StoryEntity>>
    val storiesCount: LiveData<Int>
    private val repository: StoryRepository
    init{
        val storyDao = StoryDatabase.getDatabase(application).storyDAO()
        repository= StoryRepository(storyDao)
        readAllStories = repository.readAllStories
        storiesCount = repository.storiesCount
    }

    var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllStories() {
        loading.value = true

        viewModelScope.launch {
            for(id in storiesID) {
                when (val response = mainRepository.getAllStories(id)) {
                    is NetworkState.Success -> {
                        stories.add(response.data)
                    }
                    is NetworkState.Error -> {
                        loading.value = false
                        Log.e("Network error",response.response.message())

                    }
                }
            }
            storiesList.postValue(stories)
            loading.value = false

        }
    }

    fun addStory(story: StoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addStory(story)
        }
    }
    fun deleteAllStories() {

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteStory()
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}