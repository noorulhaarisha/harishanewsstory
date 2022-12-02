package com.harisha.hackerstories.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harisha.hackerstories.model.Stories
import com.harisha.hackerstories.model.StoryEntity

@Dao
interface StoriesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity) : Long

    @Query("SELECT * from stories")
    fun readStories(): LiveData<List<StoryEntity>>

    @Query("DELETE FROM stories")
    suspend fun deleteAllStories()

    @Query("SELECT COUNT(*) FROM stories")
    fun getCount(): LiveData<Int>

}