package com.harisha.hackerstories

import com.harisha.hackerstories.model.Stories
import com.harisha.hackerstories.util.ValidationUtil
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ValidationUtilTest {

    @Test
    fun validateMovieTest() {
        val movie = Stories("test","testUrl","main")
        assertEquals(true, ValidationUtil.validateStory(movie))
    }

    @Test
    fun validateMovieEmptyTest() {
        val movie = Stories("","testUrl","main")
        assertEquals(false, ValidationUtil.validateStory(movie))
    }

}