package com.example.myapplication

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ForumQuestion(
    @DocumentId
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val userId: String = "",
    val userName: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val answers: List<ForumAnswer> = emptyList()
)

data class ForumAnswer(
    val userId: String = "",
    val userName: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now()
) 