package com.harisha.hackerstories.repository

import androidx.lifecycle.LiveData
import com.harisha.hackerstories.data.StoriesDAO
import com.harisha.hackerstories.model.Stories
import com.harisha.hackerstories.model.StoryEntity



class StoryRepository(private val storiesDAO: StoriesDAO) {
    val readAllStories: LiveData<List<StoryEntity>> = storiesDAO.readStories()
    val storiesCount: LiveData<Int> = storiesDAO.getCount()

    suspend fun addStory(story: StoryEntity) {
       var inserted =  storiesDAO.insertStory(story)
    }

    suspend fun deleteStory() {
        storiesDAO.deleteAllStories()
    }


}