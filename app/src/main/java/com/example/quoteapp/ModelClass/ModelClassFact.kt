package com.example.quoteapp.ModelClass

import java.time.ZoneId


data class MOdelClassFactResponse(
    val result:Boolean,
    val requestId:String,
    val message: String,
    val messageLBL:String,
    val payload:ModelClassFact
)
data class ModelClassFact(
    val id: Int,
    val fact:String
)
