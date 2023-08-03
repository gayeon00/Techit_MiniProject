package com.test.mini02_boardproject01.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.test.mini02_boardproject01.data.model.Post
import com.test.mini02_boardproject01.data.repository.PostRepository
import com.test.mini02_boardproject01.data.repository.UserRepository
import java.io.ByteArrayInputStream

class PostViewModel : ViewModel() {
    var title = MutableLiveData<String>()
    var content = MutableLiveData<String>()
    var image = MutableLiveData<Bitmap>()
    var writer = MutableLiveData<String>()
    var date = MutableLiveData<String>()

    fun setPost(post: Post) {
        title.value = post.postTitle
        content.value = post.postContent
    }

    fun setImage(bitmap: Bitmap) {
        image.value = bitmap
    }

    //게시글 읽기 화면
    fun setPostReadData(postIdx: Long) {
        //게시글 데이터를 가져온다.
        PostRepository.getPostInfo(postIdx) { task ->
            if (task.result.exists()) {
                for (postSnapShot in task.result.children) {
                    val postType = postSnapShot.child("postType").value as Long
                    val postTitle = postSnapShot.child("postTitle").value as String
                    val postContent = postSnapShot.child("postContent").value as String
                    val postDate = postSnapShot.child("postDate").value as String
                    val postImage = postSnapShot.child("postImage").value as String
                    val postWriterIdx = postSnapShot.child("postWriterIdx").value as Long

                    title.value = postTitle
                    content.value = postContent
                    //사용자 받아오기
                    setWriter(postWriterIdx)
                    date.value = postDate
                    setPostImage(postImage)

                    val post = Post(
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
    }

    private fun setPostImage(postImage: String) {
        //image/currenttimemillies.jpg 이름으로 firebase storage에서 받아와서 image.value에 넣어줘야 함
        PostRepository.getImage(postImage){
            image.value = byteArrayToBitmap(it.result)
        }

    }

    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        val inputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun setWriter(postWriterIdx: Long) {
        UserRepository.getUserInfoByUserId(postWriterIdx.toString()) {
            //가져온 데이터가 있다면
            if (it.result.exists()) {
                for (c1 in it.result.children) {
                    writer.value = c1.child("userNickname").value as String
                }
            }
        }
    }
}
