package com.example.quoteapp.Database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quoteapp.Interface.Dao
import com.example.quoteapp.ModelClass.ModelClassThoughts

@Database(entities = [ModelClassThoughts::class], version = 1, exportSchema = false)
abstract class ThoughtsDatabase : RoomDatabase() {
    abstract fun thoughtsDao(): Dao
}