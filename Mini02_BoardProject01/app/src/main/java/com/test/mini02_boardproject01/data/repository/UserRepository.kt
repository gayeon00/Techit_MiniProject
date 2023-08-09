package com.test.mini02_boardproject01.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.mini02_boardproject01.data.model.User

class UserRepository {
    companion object {
        //사용자 인덱스 번호 받아오기
        fun getUserIdx(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = Firebase.database
            val userIdxRef = database.reference.child("userIdx")

            userIdxRef.get().addOnCompleteListener(callback1)
        }

        //사용자 인덱스 번호 설정
        fun setUserIdx(userIdx: Long, callback1: (Task<Void>) -> Unit) {
            val database = Firebase.database
            val userIdxRef = database.reference.child("userIdx")

            //?????
            userIdxRef.get().addOnCompleteListener { task ->
                task.result.ref.setValue(userIdx).addOnCompleteListener(callback1)
            }
        }

        //사용자 정보 저장하기
        fun addUserInfo(user: User, callback1: (Task<Void>) -> Unit) {
            val database = Firebase.database

            val usersRef = database.reference.child("users")
            usersRef.push().setValue(user).addOnCompleteListener(callback1)
        }

        // 사용자 아이디를 통해 사용자 정보를 가져온다.
        fun getUserInfoByUserId(loginUserId: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = Firebase.database
            val userDataRef = database.getReference("users")

            // userId가 사용자가 입력한 아이디와 같은 데이터를 가져온다.
            userDataRef.orderByChild("userId").equalTo(loginUserId).get()
                .addOnCompleteListener(callback1)
        }

        //사용자 인덱스를 통해 사용자 정보 가져오기
        // 사용자 인덱스를 통해 사용자 정보를 가져온다.
        fun getUserInfoByUserIdx(userIdx: Long, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = Firebase.database
            val userDataRef = database.getReference("users")

            userDataRef.orderByChild("userIdx").equalTo(userIdx.toDouble()).get()
                .addOnCompleteListener(callback1)
        }

        //사용자 정보를 수정하는 메서드
        fun modifyUserInfo(user: User) {
            getUserInfoByUserIdx(user.userIdx) {
                for(a1 in it.result.children) {
                    //비밀번호 수정
                    a1.ref.child("userPw").setValue(user.userPw)
                    a1.ref.child("userNickname").setValue(user.userNickname)
                    a1.ref.child("userAge").setValue(user.userAge)
                    a1.ref.child("userJoinRoute1").setValue(user.userJoinRoute1)
                    a1.ref.child("userJoinRoute2").setValue(user.userJoinRoute2)
                    a1.ref.child("userJoinRoute3").setValue(user.userJoinRoute3)
                    a1.ref.child("userJoinRoute4").setValue(user.userJoinRoute4)
                    a1.ref.child("userJoinRoute5").setValue(user.userJoinRoute5)
                }
            }
        }
    }
}