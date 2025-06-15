package com.example.myapplication.data

data class UserData(
    val uid: String = "",
    val fullName: String = "",
    val city: String = "",
    val age: Int = 0,
    val gender: String = "",
    val email: String = "",
    val profileImageUrl: String? = null,
    val points: Int = 0,
    val cardNumber: String? = null
) 