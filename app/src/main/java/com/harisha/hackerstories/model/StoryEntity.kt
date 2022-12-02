package com.harisha.hackerstories.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.parcelize.Parcelize
@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val by: String="",
    val descendants: String?,
    val storyID: String="",
    val kids : Int,
    val score: Int,
    val time: Int,
    val title: String="",
    val type: String?,
    val url: String?
): Parcelable