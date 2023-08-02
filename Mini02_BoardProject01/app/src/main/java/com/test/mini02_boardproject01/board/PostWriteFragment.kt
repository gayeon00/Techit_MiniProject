package com.test.mini02_boardproject01.board

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.test.mini02_boardproject01.Post
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.databinding.FragmentPostWriteBinding
import com.test.mini02_boardproject01.domain.PostViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostWriteFragment : Fragment() {
    lateinit var fragmentPostWriteBinding: FragmentPostWriteBinding
    lateinit var boardMainActivity: BoardMainActivity

    lateinit var postViewModel: PostViewModel

    // 게시판 종류
    var boardType = 0


    // 업로드할 이미지의 Uri
    var uploadUri: Uri? = null

    //카메라 사용 인텐트로 넘어갈 런처
    lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        fragmentPostWriteBinding = FragmentPostWriteBinding.inflate(layoutInflater)
        boardMainActivity = activity as BoardMainActivity


        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()

        //카메라 설정
        cameraLauncher = cameraSetting(fragmentPostWriteBinding.imageView)
        //앨범 설정
        albumLauncher = albumSetting(fragmentPostWriteBinding.imageView)

        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]

        fragmentPostWriteBinding.run {
            materialToolbar4.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.item_camera -> {
                        val newIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                        //사진이 저장될 파일 이름
                        val fileName = "/temp_upload.jpg"
                        //경로
                        val filePath = boardMainActivity.getExternalFilesDir(null).toString()
                        //경로+파일이름
                        val picPath = "${filePath}/${fileName}"

                        //사진이 저장될 경로를 관리할 Uri객체를 만들어준다.
                        //업로드할 때 사용할 Uri
                        val file = File(picPath)
                        uploadUri = FileProvider.getUriForFile(
                            boardMainActivity, "com.test.mini02_boardproject01.file_provider", file
                        )

                        newIntent.putExtra(MediaStore.EXTRA_OUTPUT, uploadUri)
                        cameraLauncher.launch(newIntent)
                    }

                    R.id.item_gallery -> {
                        //앨범 띄우기
                        val newIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        newIntent.setType("image/*")
                        newIntent.putExtra(Intent.EXTRA_MIME_TYPES, arrayListOf("image/*"))
                        albumLauncher.launch(newIntent)
                    }

                    R.id.item_complete -> {
                        //제목 내용 유효성 검사
                        // 입력한 내용을 가져온다.
                        val subject = textInputEditTextPostWriteSubject.text.toString()
                        val text = textInputEditTextPostWriteText.text.toString()

                        if (subject.isEmpty()) {
                            val builder = MaterialAlertDialogBuilder(boardMainActivity)
                            builder.setTitle("제목 입력 오류")
                            builder.setMessage("제목을 입력해주세요")
                            builder.setPositiveButton("확인", null)
                            builder.show()
                            return@setOnMenuItemClickListener true
                        }

                        if (text.isEmpty()) {
                            val builder = MaterialAlertDialogBuilder(boardMainActivity)
                            builder.setTitle("내용 입력 오류")
                            builder.setMessage("내용을 입력해주세요")
                            builder.setPositiveButton("확인", null)
                            builder.show()
                            return@setOnMenuItemClickListener true
                        }
                        if (boardType == 0) {
                            val builder = MaterialAlertDialogBuilder(boardMainActivity)
                            builder.setTitle("게시판 종류 선택 오류")
                            builder.setMessage("게시판 종류를 선택해주세요")
                            builder.setPositiveButton("확인", null)
                            builder.show()
                            return@setOnMenuItemClickListener true
                        }

                        val database = Firebase.database
                        //게시글 인덱스 번호
                        val postIdxRef = database.reference.child("postIdx")
                        postIdxRef.get().addOnCompleteListener { task ->
                            var postIdx = task.result.value as Long
                            //게시글 인덱스 증가
                            postIdx++

                            //게시글 저장
                            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val writeDate = sdf.format(Date(System.currentTimeMillis()))

                            val fileName = if (uploadUri == null) {
                                "None"
                            } else {
                                "image/img_${System.currentTimeMillis()}.jpg"
                            }

                            val post = Post(
                                postIdx,
                                boardType.toLong(),
                                subject,
                                text,
                                writeDate,
                                fileName,
                                boardMainActivity.loginUser.userIdx
                            )

                            val postRef = database.reference.child("posts")
                            postRef.push().setValue(post).addOnCompleteListener {
                                postIdxRef.get().addOnCompleteListener {
                                    it.result.ref.setValue(postIdx).addOnCompleteListener {
                                        if (uploadUri != null) {
                                            val storage = Firebase.storage
                                            val imageRef = storage.reference.child(fileName)
                                            imageRef.putFile(uploadUri!!).addOnCompleteListener {
                                                Snackbar.make(
                                                    fragmentPostWriteBinding.root,
                                                    "저장됐습니다.",
                                                    Snackbar.LENGTH_SHORT
                                                ).show()

                                                postViewModel.setPost(post)
                                                findNavController().navigate(R.id.action_postWriteFragment_to_postReadFragment)
                                            }
                                        } else {
                                            Snackbar.make(
                                                fragmentPostWriteBinding.root,
                                                "저장됐습니다.",
                                                Snackbar.LENGTH_SHORT
                                            ).show()

                                            postViewModel.setPost(post)
                                            findNavController().navigate(R.id.action_postWriteFragment_to_postReadFragment)
                                        }
                                    }

                                }
                            }
                        }

                    }
                }
                true
            }

            button.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(boardMainActivity)
                builder.setTitle("게시판 종류")
                builder.setItems(boardMainActivity.boardTypeList) { dialogInterface: DialogInterface, i: Int ->
                    boardType = i + 1
                    button.text = boardMainActivity.boardTypeList[i]
                }
                builder.setNegativeButton("취소", null)
                builder.show()
            }

            materialToolbar4.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        // Inflate the layout for this fragment
        return fragmentPostWriteBinding.root
    }

    //카메라 관련 설정
    fun cameraSetting(previewImageView: ImageView): ActivityResultLauncher<Intent> {
        //사진 촬영을 위한 런처
        val cameraContract = ActivityResultContracts.StartActivityForResult()
        val cameraLauncher = registerForActivityResult(cameraContract) {

            if (it?.resultCode == AppCompatActivity.RESULT_OK) {

                // Uri를 이용해 이미지에 접근하여 Bitmap 객체로 생성한다.
                val bitmap = BitmapFactory.decodeFile(uploadUri?.path)

                //이미지 크기 조정
                val ratio = 1024.0 / bitmap.width
                val targetHeight = (bitmap.height * ratio).toInt()
                val bitmap2 = Bitmap.createScaledBitmap(bitmap, 1024, targetHeight, false)

                //회전 각도값을 가져온다.
                val degree = getDegree(uploadUri!!)

                //회전 이미지 생성
                val matrix = Matrix()
                matrix.postRotate(degree.toFloat())
                val bitmap3 =
                    Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.width, bitmap2.height, matrix, false)
                previewImageView.setImageBitmap(bitmap3)

                postViewModel.setImage(bitmap3)
            }
        }

        return cameraLauncher
    }

    fun getDegree(uri: Uri): Int {
        var exifInterface: ExifInterface? = null

        //사진 파일로부터 tag정보를 관리하는 객체를 추출
        exifInterface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val photoUri = MediaStore.setRequireOriginal(uri)
            //파일과 연결된 스트림 추출
            val inputStream = boardMainActivity.contentResolver.openInputStream(photoUri)
            //ExifInterface정보 읽어오기
            ExifInterface(inputStream!!)
        } else {
            ExifInterface(uri.path!!)
        }

        var degree = 0
        //각도 값 가져오기

        when (exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
        }

        return degree
    }

    // 앨범 관련 설정
    fun albumSetting(previewImageView: ImageView): ActivityResultLauncher<Intent> {
        val albumContract = ActivityResultContracts.StartActivityForResult()
        val albumLauncher = registerForActivityResult(albumContract) {

            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                // 선택한 이미지에 접근할 수 있는 Uri 객체를 추출한다.
                if (it.data?.data != null) {
                    uploadUri = it.data?.data

                    // 안드로이드 10 (Q) 이상이라면...
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // 이미지를 생성할 수 있는 디코더를 생성한다.
                        val source = ImageDecoder.createSource(
                            boardMainActivity.contentResolver, uploadUri!!
                        )
                        // Bitmap객체를 생성한다.
                        val bitmap = ImageDecoder.decodeBitmap(source)

                        previewImageView.setImageBitmap(bitmap)
                        postViewModel.setImage(bitmap)
                    } else {
                        // 컨텐츠 프로바이더를 통해 이미지 데이터 정보를 가져온다.
                        val cursor = boardMainActivity.contentResolver.query(
                            uploadUri!!, null, null, null, null
                        )
                        if (cursor != null) {
                            cursor.moveToNext()

                            // 이미지의 경로를 가져온다.
                            val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                            val source = cursor.getString(idx)

                            // 이미지를 생성하여 보여준다.
                            val bitmap = BitmapFactory.decodeFile(source)
                            previewImageView.setImageBitmap(bitmap)
                            postViewModel.setImage(bitmap)
                        }
                    }
                }
            }
        }
        return albumLauncher
    }

}