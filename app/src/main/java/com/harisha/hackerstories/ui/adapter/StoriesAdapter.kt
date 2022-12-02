package com.harisha.hackerstories.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.harisha.hackerstories.databinding.ItemStoryBinding
import com.harisha.hackerstories.model.Stories
import com.harisha.hackerstories.util.ValidationUtil
import java.util.*
import kotlin.collections.ArrayList

class StoriesAdapter : RecyclerView.Adapter<MainViewHolder>() {

    var storiesList: ArrayList<Stories> = ArrayList()
    var storiesFilterList: ArrayList<Stories> = ArrayList()

    init {
        storiesFilterList = storiesList
    }

    fun setStories(stories: ArrayList<Stories>) {
        this.storiesList = stories
        storiesFilterList = storiesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoryBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val story = storiesFilterList[position]
        if (ValidationUtil.validateStory(story)) {
            holder.binding.name.text = story.title.replace("Show HN:", "")
            holder.binding.story.text = story.by
        }
    }

    override fun getItemCount(): Int {
        return storiesFilterList.size
    }

    //Getting filtered data based on title and author
    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) storiesFilterList = storiesList else {
                    val filteredList = ArrayList<Stories>()
                    storiesList
                        .filter {
                            it.title.contains(constraint!!) ||
                                    (it.by.contains(constraint))

                        }
                        .forEach { filteredList.add(it) }
                    storiesFilterList = filteredList

                    Log.e("Filtering t1", filteredList.size.toString())

                }
                return FilterResults().apply { values = storiesFilterList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                storiesFilterList = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Stories>
                notifyDataSetChanged()

                Log.e("Filtering t2 ", "called" + storiesFilterList.size)

            }
        }
    }

}


class MainViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

}