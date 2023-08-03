package com.test.mini02_boardproject01.data.repository

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.test.mini02_boardproject01.data.model.Post
import java.io.File

class PostRepository {

    companion object {
        //게시글 정보 가져오기
        fun getOne(idx: Long, callback1: (Task<DataSnapshot>) -> Unit): Post {
            val database = Firebase.database
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

                        post = Post(
                            postType,
                            postType,
                            postTitle,
                            postContent,
                            postDate,
                            postImage,
                            postWriterIdx
                        )
                    }
                }
            }

            return post
        }

        //게시글 정보 가져오기
        fun getPostInfo(idx: Long, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = Firebase.database
            //firebase에서 인덱스가 idx인 놈 가져오기
            val postsRef = database.reference.child("posts")

            //이거 하려고 실시간데이터베이스 규칙 만들어줌
            postsRef.orderByChild("postIdx").equalTo(idx.toDouble()).get()
                .addOnCompleteListener(callback1)
        }

        //게시글 인덱스 번호를 가져온다.
        fun getPostIdx(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = Firebase.database
            //게시글 인덱스 번호
            val postIdxRef = database.reference.child("postIdx")
            postIdxRef.get().addOnCompleteListener(callback1)
        }

        //게시글 정보를 저장한다.
        fun addPostInfo(post: Post, callback1: (Task<Void>) -> Unit) {
            val database = Firebase.database
            val postRef = database.reference.child("posts")
            postRef.push().setValue(post).addOnCompleteListener(callback1)
        }

        //게시글 번호 저장
        fun setPostIdx(postIdx: Long, callback1: (Task<Void>) -> Unit) {
            val database = Firebase.database
            //게시글 인덱스 번호
            val postIdxRef = database.reference.child("postIdx")
            postIdxRef.get().addOnCompleteListener {
                it.result.ref.setValue(postIdx).addOnCompleteListener(callback1)
            }
        }

        fun uploadImage(
            fileName: String,
            uri: Uri?,
            callback1: (Task<UploadTask.TaskSnapshot>) -> Unit
        ) {
            val storage = Firebase.storage
            val imageRef = storage.reference.child(fileName)
            imageRef.putFile(uri!!).addOnCompleteListener(callback1)
        }

        //image/currenttimemillies.jpg 이름으로 firebase storage에서 받아와서 image.value에 넣어줘야 함
        fun getImage(fileName: String, callback1: (Task<ByteArray>) -> Unit) {
            val storage = Firebase.storage
            val imageRef = storage.reference.child(fileName)
            imageRef.getBytes(Long.MAX_VALUE).addOnCompleteListener(callback1)
        }

    }

}
