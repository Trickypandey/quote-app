package com.example.quoteapp.Interface

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quoteapp.ModelClass.ModelClassThoughts


@androidx.room.Dao

interface Dao {

        @Insert
        fun insert(modelClassThoughts: ModelClassThoughts)


        @Update
        fun update(modelClassThoughts: ModelClassThoughts)

        @Delete
        fun delete(modelClassThoughts: ModelClassThoughts)

        @Query("DELETE FROM thoughts")
        fun deleteAllThoughts()

        @Query("SELECT * FROM thoughts WHERE date = :givenDate")
        fun getThoughtsByDate(givenDate: String): List<ModelClassThoughts>

        @Query("SELECT * FROM thoughts WHERE date = :givenDate and thoughtType = :type")
        fun getThoughtsByDateType(givenDate: String,type: String): List<ModelClassThoughts>

        @Query("SELECT * FROM thoughts")
        fun getAllThoughtsGivenDate(): List<ModelClassThoughts>

        @Query("SELECT * FROM thoughts WHERE thought LIKE '%' || :searchWord || '%' and thoughtType = :type")
        fun searchByWordType(searchWord: String,type: String): List<ModelClassThoughts>

        @Query("UPDATE thoughts SET thought = :thought WHERE date = :date and thoughtType=:type")
        fun updateThought(thought: String, date: String,type: String)

}