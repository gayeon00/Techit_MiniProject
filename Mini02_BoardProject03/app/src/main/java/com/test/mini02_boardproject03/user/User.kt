package com.test.mini02_boardproject03.user

data class User(
    val userId: String,
    val userPw: String,
    val userNickname: String,
    val userAge: Long,
    val userJoinRoute: List<String>
)