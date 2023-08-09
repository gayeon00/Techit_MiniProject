package com.test.mini02_boardproject01.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.mini02_boardproject01.data.model.Post
import com.test.mini02_boardproject01.data.repository.PostRepository
import com.test.mini02_boardproject01.data.repository.UserRepository
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class PostViewModel : ViewModel() {
    var title = MutableLiveData<String>()
    var content = MutableLiveData<String>()
    var image = MutableLiveData<Bitmap>()
    var writer = MutableLiveData<String>()
    var date = MutableLiveData<String>()
    var type = MutableLiveData<Long>()

    // 이미지 파일 이름
    var fileName = MutableLiveData<String>()

    //게시글 작성자 닉네임
    val writerNicknameList = MutableLiveData<MutableList<String>>()

    // 게시글 목록
    var postList = MutableLiveData<MutableList<Post>>()

    init {
        postList.value = mutableListOf()
        writerNicknameList.value = mutableListOf()
    }

    fun setPost(post: Post) {
        title.value = post.postTitle
        content.value = post.postContent
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

                    type.value = postType
                    title.value = postTitle
                    content.value = postContent
                    //사용자 받아오기
                    setWriter(postWriterIdx)
                    date.value = postDate
                    // 이미지 파일 이름
                    fileName.value = postImage


                    // 이미지가 있다면

                    setPostImage()
//                    val post = Post(
//                        postType,
//                        postType,
//                        postTitle,
//                        postContent,
//                        postDate,
//                        postImage,
//                        postWriterIdx
//                    )
                }
            }
        }
    }

    private fun setPostImage() {
        if (fileName.value != "None") {
            PostRepository.getImage(fileName.value!!) {
                thread {
                    // 파일에 접근할 수 있는 경로를 이용해 URL 객체를 생성한다.
                    val url = URL(it.result.toString())
                    // 접속한다.
                    val httpURLConnection = url.openConnection() as HttpURLConnection
                    // 이미지 객체를 생성한다.
                    val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)

                    //얜 왜 이렇게 하지? -> UI스레드에서 해주는거구나!
                    image.postValue(bitmap)
                }
            }
        } else {
            image.postValue(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888))
        }

    }

    private fun setWriter(postWriterIdx: Long) {
        UserRepository.getUserInfoByUserIdx(postWriterIdx) {
            //가져온 데이터가 있다면
            if (it.result.exists()) {
                for (c1 in it.result.children) {
                    writer.value = c1.child("userNickname").value as String
                }
            }
        }
    }

    //게시글 목록
    fun getPostAll(myPostType: Long) {
        val tempList = mutableListOf<Post>()
        val tempList2 = mutableListOf<String>()

        PostRepository.getPostAll { task ->
            for (postSnapShot in task.result.children) {
                val postIdx = postSnapShot.child("postIdx").value as Long
                val postType = postSnapShot.child("postType").value as Long
                val postTitle = postSnapShot.child("postTitle").value as String
                val postContent = postSnapShot.child("postContent").value as String
                val postDate = postSnapShot.child("postDate").value as String
                val postImage = postSnapShot.child("postImage").value as String
                val postWriterIdx = postSnapShot.child("postWriterIdx").value as Long

                if (myPostType != 0L && myPostType != postType) {
                    continue
                }

                val post = Post(
                    postIdx,
                    postType,
                    postTitle,
                    postContent,
                    postDate,
                    postImage,
                    postWriterIdx
                )
                tempList.add(post)


                UserRepository.getUserInfoByUserIdx(postWriterIdx) {
                    for (c2 in it.result.children) {
                        val postWriterNickName = c2.child("userNickname").value as String
                        tempList2.add(postWriterNickName)
                    }

                }

            }

            //데이터가 postIdx를 기준으로 오름차순 정렬 돼있어서 순서를 뒤집는다.
            tempList.reverse()
            tempList2.reverse()
            postList.value = tempList
            writerNicknameList.value = tempList2
        }
    }

    //postList 초기화
    fun resetPostList() {
        postList.value = mutableListOf<Post>()
        writerNicknameList.value = mutableListOf<String>()
    }

    //검색결과를 가져옴
    fun getSearchPostList(myPostType: Long, keyword: String) {

        val tempList = mutableListOf<Post>()
        val tempList2 = mutableListOf<String>()

        PostRepository.getPostAll { task ->
            for (postSnapShot in task.result.children) {
                val postIdx = postSnapShot.child("postIdx").value as Long
                val postType = postSnapShot.child("postType").value as Long
                val postTitle = postSnapShot.child("postTitle").value as String
                val postContent = postSnapShot.child("postContent").value as String
                val postDate = postSnapShot.child("postDate").value as String
                val postImage = postSnapShot.child("postImage").value as String
                val postWriterIdx = postSnapShot.child("postWriterIdx").value as Long

                if (myPostType != 0L && myPostType != postType) {
                    continue
                }

                //제목에도 없고 내용에도 없다면 넘어가기
                if (!postTitle.contains(keyword) && !postContent.contains(keyword)) {
                    continue
                }

                val post = Post(
                    postIdx,
                    postType,
                    postTitle,
                    postContent,
                    postDate,
                    postImage,
                    postWriterIdx
                )
                tempList.add(post)


                UserRepository.getUserInfoByUserIdx(postWriterIdx) {
                    for (c2 in it.result.children) {
                        val postWriterNickName = c2.child("userNickname").value as String
                        tempList2.add(postWriterNickName)
                    }

                }

            }

            //데이터가 postIdx를 기준으로 오름차순 정렬 돼있어서 순서를 뒤집는다.
            tempList.reverse()
            tempList2.reverse()
            postList.value = tempList
            writerNicknameList.value = tempList2
        }

    }


}



