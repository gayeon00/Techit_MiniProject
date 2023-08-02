package com.test.mini02_boardproject01.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.mini02_boardproject01.Post

class PostRepository {

    companion object {
        private val database = Firebase.database
        fun getOne(idx: Long): Post {
            //firebase에서 인덱스가 idx인 놈 가져오기
            val postsRef = database.reference.child("posts")

            var post = Post()
            //이거 하려고 실시간데이터베이스 규칙 만들어줌
            postsRef.orderByChild("postIdx").equalTo(idx.toDouble()).get().addOnCompleteListener {
                if (it.result.exists()) {
                    for (postSnapShot in it.result.children) {
                        val postType = postSnapShot.child("postType").value as Long
                        val postTitle = postSnapShot.child("postTitle").value as String
                        val postContent = postSnapShot.child("postContent").value as String
                        val postDate = postSnapShot.child("postDate").value as String
                        val postImage = postSnapShot.child("postImage").value as String
                        val postWriterIdx = postSnapShot.child("postWriterIdx").value as Long

                        post = Post(postType, postType, postTitle, postContent, postDate, postImage, postWriterIdx)
                    }
                }
            }

            return post
        }
    }

}
