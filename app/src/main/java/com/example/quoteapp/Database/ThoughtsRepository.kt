package com.example.quoteapp.Database

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.quoteapp.Interface.Dao
import com.example.quoteapp.ModelClass.ModelClassThoughts

class ThoughtsRepository(private val thoughtsDao: Dao) {
    fun insert(modelClassThoughts: ModelClassThoughts) {
        thoughtsDao.insert(modelClassThoughts)
    }

    fun getThoughtsByDate(givenDate: String): List<ModelClassThoughts> {
        return thoughtsDao.getThoughtsByDate(givenDate)
    }
    fun getThoughtsByDatetype(givenDate: String,type: String): List<ModelClassThoughts> {
        return thoughtsDao.getThoughtsByDateType(givenDate,type)
    }

    fun searchByWordType(word: String,type: String): List<ModelClassThoughts> {
        return thoughtsDao.searchByWordType(word,type)
    }

    fun getAllThoughtsGivenDate(): List<ModelClassThoughts> {
        return thoughtsDao.getAllThoughtsGivenDate()
    }

    fun updateThought(thought:String,date:String,type:String){
        return thoughtsDao.updateThought(thought,date,type)
    }

}






