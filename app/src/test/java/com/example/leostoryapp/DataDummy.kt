package com.example.leostoryapp

import com.example.leostoryapp.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items : MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..10) {
            val story = ListStoryItem(
                id = i.toString(),
                name = "name + $i",
                description = "desc $i",
                photoUrl = "url $i",
                createdAt = "date $i",
                lat = ("lat $i").replace("lat", "").toDouble(),
                lon = ("lon $i").replace("lon", "").toDouble()
            )
            items.add(story)
        }
        return items
    }
}
