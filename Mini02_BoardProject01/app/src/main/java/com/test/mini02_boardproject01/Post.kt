package com.test.mini02_boardproject01

data class Post(
    var postIdx: Long = 0L,         //게시글 인덱스 번호
    var postType: Long = 0L,        //게시판 종류
    var postTitle: String = "",   //제목
    var postContent: String = "",      //내용
    var postDate: String = "", //작성일
    var postImage: String = "",     //첨부이미지 파일이름
    var postWriterIdx: Long = 0L    //작성자 인덱스 번호
) {
}