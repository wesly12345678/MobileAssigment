package com.example.real

data class User(
    var id: String,
    var username: String,
    var email: String,
    var password: String,
    var confirmPassword: String,
    var role: String
)