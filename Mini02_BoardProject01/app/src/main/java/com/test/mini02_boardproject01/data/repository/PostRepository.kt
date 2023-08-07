package com.test.mini02_boardproject01.data.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.test.mini02_boardproject01.data.model.Post

class PostRepository {

    companion object {
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
        fun getImage(fileName: String, callback1: (Task<Uri>) -> Unit) {
            val storage = Firebase.storage
            val fileRef = storage.reference.child(fileName)

            // 데이터를 가져올 수 있는 경로를 가져온다.
            fileRef.downloadUrl.addOnCompleteListener(callback1)

        }

        // 게시글 정보 전체를 가져온다.
        fun getPostAll(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = Firebase.database
            val postDataRef = database.getReference("posts")
            postDataRef.orderByChild("postIdx").get().addOnCompleteListener(callback1)
        }

        //특정 게시판의 글 정보만 가져온다.
        fun getPostByPostType(postType: Long, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = Firebase.database
            val postDataRef = database.getReference("posts")
            postDataRef.orderByChild("postType").equalTo(postType.toDouble())
                .orderByChild("postIdx").get().addOnCompleteListener(callback1)
        }

        // 이미지 삭제
        fun removeImage(fileName: String, callback1: (Task<Void>) -> Unit) {
            val storage = Firebase.storage
            val fileRef = storage.reference.child(fileName)
            // 파일을 삭제한다.
            fileRef.delete().addOnCompleteListener(callback1)
        }

        //글 삭제
        fun removePost(postIdx: Long, callback1: (Task<Void>) -> Unit) {
            val database = Firebase.database
            val postDataRef = database.getReference("posts")

            postDataRef.orderByChild("postIdx").equalTo(postIdx.toDouble()).get()
                .addOnCompleteListener {
                    for (a1 in it.result.children) {
                        //해당 데이터 삭제
                        a1.ref.removeValue().addOnCompleteListener(callback1)
                    }
                }
        }

        //글 수정
        fun modifyPost(post: Post, isNewImage: Boolean, callback1: (Task<Void>) -> Unit) {
            getPostInfo(post.postIdx) {
                for (a1 in it.result.children) {
                    if (isNewImage) {
                        a1.ref.child("postImage").setValue(post.postImage)
                    }
                    a1.ref.child("postTitle").setValue(post.postTitle)
                    a1.ref.child("postContent").setValue(post.postContent)
                        .addOnCompleteListener(callback1)
                }
            }
        }

    }

}
