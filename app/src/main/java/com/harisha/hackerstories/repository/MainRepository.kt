package com.harisha.hackerstories.repository

import com.harisha.hackerstories.api.NetworkState
import com.harisha.hackerstories.api.RetrofitService
import com.harisha.hackerstories.model.Stories

class MainRepository constructor(private val retrofitService: RetrofitService) {

    suspend fun getAllStories(id : Int) : NetworkState<Stories> {
            val response = retrofitService.getAllStories(id)
            return if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    NetworkState.Success(responseBody)
                } else {
                    NetworkState.Error(response)
                }
            } else {
                NetworkState.Error(response)
            }
        }


}