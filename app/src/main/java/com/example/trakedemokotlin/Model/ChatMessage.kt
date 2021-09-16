package com.example.trakedemokotlin.Model

import java.util.*

data class ChatMessage(
    val text: String,
    val user: String?,
    val timestamp: Date?
)