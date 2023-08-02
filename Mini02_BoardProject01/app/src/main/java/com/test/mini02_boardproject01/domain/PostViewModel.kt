package com.test.mini02_boardproject01.domain

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.mini02_boardproject01.Post

class PostViewModel : ViewModel() {
    var title = MutableLiveData<String>()
    var content = MutableLiveData<String>()
    var image = MutableLiveData<Bitmap>()

    fun setPost(post: Post) {
        title.value = post.postTitle
        content.value = post.postContent
    }

    fun setImage(bitmap: Bitmap) {
        image.value = bitmap
    }
}
