package com.harisha.hackerstories.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.harisha.hackerstories.*
import com.harisha.hackerstories.api.RetrofitService
import com.harisha.hackerstories.databinding.ActivityMainBinding
import com.harisha.hackerstories.model.Stories
import com.harisha.hackerstories.model.StoryEntity
import com.harisha.hackerstories.repository.MainRepository
import com.harisha.hackerstories.viewmodel.MainViewModel
import com.harisha.hackerstories.viewmodel.MyViewModelFactory


import com.harisha.hackerstories.ui.adapter.StoriesAdapter
import com.harisha.hackerstories.util.ValidationUtil


class MainActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener{
    lateinit var viewModel: MainViewModel
    private val adapter = StoriesAdapter()
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)
        val search_layout = findViewById<androidx.appcompat.widget.SearchView>(R.id.search_view)
        search_layout.setOnQueryTextListener(this)

        binding.refresh.setOnRefreshListener {
            getStoriesFromApi()

        }

        viewModel = ViewModelProvider(this, MyViewModelFactory(mainRepository,application)).get(MainViewModel::class.java)
        supportActionBar?.hide(); // hide the title bar

        getStoriesFromApi()

        viewModel.storiesList.observe(this) {
              //  viewModel.deleteAllStories()
                insertDataToDatabase(it) // save data in local
                displayStoriesFromLocal() //Display data from local

        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this, Observer {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })

    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        adapter.getFilter().filter(query)
        return false

    }

    override fun onQueryTextChange(newText: String?): Boolean {

        adapter.getFilter().filter(newText)

        return false

    }

    private fun getStoriesFromApi(){
        if(ValidationUtil.checkForInternet(this)) {
            viewModel.deleteAllStories()
            viewModel.getAllStories()
        }
        else {
            displayStoriesFromLocal()
            Toast.makeText(this, resources.getText(R.string.connect_internet),
                Toast.LENGTH_SHORT).show()
            binding.progressDialog.visibility = View.GONE
            binding.refresh.isRefreshing = false

        }
    }

    private fun insertDataToDatabase(storyList: List<Stories>) {

        try {
            for (pos in storyList.indices) {
                var story = storyList[pos]
                val storySave = StoryEntity(
                    0, story.by, story.descendants, story.id, 0,
                    story.score, story.time, story.title, story.type, story.url
                )
                viewModel.addStory(storySave)
            }
        }
        catch (e: Exception){
            println("Insert exception ${e.message}")

        }


    }
    private fun displayStoriesFromLocal(){
        binding.recyclerview.adapter = adapter

        try {
            viewModel.readAllStories.observe(this, Observer { storyList ->
                val storyListData = ArrayList<Stories>()
                for (pos in storyList.indices) {

                    val story = storyList[pos]
                    storyListData
                        .add(
                            Stories(
                                story.by, story.descendants, story.storyID, null,
                                story.score, story.time, story.title, story.type, story.url
                            )
                        )
                }
                adapter.setStories(storyListData)

            })
        } catch(e: Exception){
            println("Insert exception ${e.message}")
        }
        binding.refresh.isRefreshing = false

    }


}