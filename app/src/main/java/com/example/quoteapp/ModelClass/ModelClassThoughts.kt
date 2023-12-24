package com.example.quoteapp.ModelClass

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "thoughts")
data class ModelClassThoughts(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val thought: String,
    val thoughtType: String,
    val date: String
)
