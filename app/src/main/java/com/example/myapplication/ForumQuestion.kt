package com.example.myapplication

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ForumQuestion(
    @DocumentId
    var id: String = "",
    var title: String = "",
    var content: String = "",
    var timestamp: Timestamp = Timestamp.now(),
    var author: String = ""
)

data class ForumAnswer(
    val userId: String = "",
    val userName: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now()
) 