package com.example.tiket2

data class Users(
    //menyimpan data user
    var username: String = "",
    var email: String = "",
    var phone: String = "",
    var password: String = "",
    var role: String = "",
    var bookmark: List<String>?
)
