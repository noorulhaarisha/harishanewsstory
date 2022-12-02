package com.harisha.hackerstories.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harisha.hackerstories.model.StoryEntity


@Database(
    entities = [StoryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class StoryDatabase: RoomDatabase() {
    abstract fun storyDAO(): StoriesDAO

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        fun getDatabase(context: Context): StoryDatabase{
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java,
                    "story_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}