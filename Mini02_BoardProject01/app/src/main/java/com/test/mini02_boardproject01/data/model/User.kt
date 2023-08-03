package com.test.mini02_boardproject01.data.model

import java.io.Serializable

data class User(
    var userIdx: Long = 0L,
    var userId: String = "",
    var userPw: String = "",
    var userNickname: String = "",
    var userAge: Long = 0L,
    var userJoinRoute1: Boolean = false,
    var userJoinRoute2: Boolean = false,
    var userJoinRoute3: Boolean = false,
    var userJoinRoute4: Boolean = false,
    var userJoinRoute5: Boolean = false
) : Serializable