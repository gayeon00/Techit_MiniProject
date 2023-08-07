package com.test.mini02_boardproject01.ui.board

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
import com.test.mini02_boardproject01.R
import com.test.mini02_boardproject01.data.model.Post
import com.test.mini02_boardproject01.data.repository.PostRepository
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
    var postType = 0

    //PostWriteFragment인지, PostModifyFragment인지
    var isModify = false
    //Modify에 진입해서 사진을 새로 고르는건지
    var isSelectNewImage = false
    var postIdx = 0L

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
        isModify = arguments?.getBoolean("isModify", false)!!
        postIdx = arguments?.getLong("postIdx")!!

        fragmentPostWriteBinding.run {

            if (isModify) {
                //감시자 달아줌
                //modify일 경우 감시자 작동함
                postViewModel.run {
                    title.observe(viewLifecycleOwner) {
                        textInputEditTextPostWriteSubject.setText(it)
                    }
                    content.observe(viewLifecycleOwner) {
                        textInputEditTextPostWriteText.setText(it)
                    }
                    image.observe(viewLifecycleOwner) {
                        imageView.setImageBitmap(it)
                    }
                    type.observe(viewLifecycleOwner) {
                        button.text = boardMainActivity.postTypeList[it.toInt() - 1]
                    }
                }

            }


            materialToolbar4.run {
                title = if (isModify) {
                    "글 수정"
                } else {
                    "글 작성"
                }

                setOnMenuItemClickListener { menu ->
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
                                boardMainActivity,
                                "com.test.mini02_boardproject01.file_provider",
                                file
                            )

                            newIntent.putExtra(MediaStore.EXTRA_OUTPUT, uploadUri)
                            cameraLauncher.launch(newIntent)
                        }

                        R.id.item_gallery -> {
                            //앨범 띄우기
                            val newIntent =
                                Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                            newIntent.setType("image/*")
                            newIntent.putExtra(Intent.EXTRA_MIME_TYPES, arrayListOf("image/*"))
                            albumLauncher.launch(newIntent)
                        }

                        R.id.item_complete -> {
                            // 입력한 내용을 가져온다.
                            val subject = textInputEditTextPostWriteSubject.text.toString()
                            val text = textInputEditTextPostWriteText.text.toString()

                            //게시글 저장
                            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val writeDate = sdf.format(Date(System.currentTimeMillis()))

                            if (isModify) {

                                val fileName = if (!isSelectNewImage) {
                                    //새로 고른게 아니라면 원래 쓰던 filename으로
                                    postViewModel.fileName.value
                                } else {
                                    //새로 골랐는데, 원래 없었다면
                                    if (postViewModel.fileName.value == "None") {
                                        "image/img_${System.currentTimeMillis()}.jpg"
                                    } else {
                                        //새로 골랐는데, 원래 있었다면
                                        postViewModel.fileName.value
                                    }
                                }

                                //원래 있던 자리에 덮어씌우기
                                val post = Post(
                                    postIdx,
                                    postType.toLong(),
                                    subject,
                                    text,
                                    writeDate,
                                    fileName!!,
                                    boardMainActivity.loginUser.userIdx
                                )
                                PostRepository.modifyPost(post, isSelectNewImage) {
                                    //새로운 이미지 업로드
                                    if (uploadUri != null) {

                                        PostRepository.uploadImage(fileName, uploadUri) {
                                            showSnackBar(
                                                fragmentPostWriteBinding.root,
                                                "저장됐습니다."
                                            )

//                                                postViewModel.setPost(post)
                                            val arg = Bundle()
                                            arg.putLong("postIdx", postIdx)
                                            findNavController().navigate(
                                                R.id.action_postWriteFragment_to_postReadFragment,
                                                arg
                                            )
                                        }

                                    } else {
                                        showSnackBar(
                                            fragmentPostWriteBinding.root,
                                            "저장됐습니다."
                                        )

//                                            postViewModel.setPost(post)
                                        val arg = Bundle()
                                        arg.putLong("postIdx", postIdx)
                                        findNavController().navigate(
                                            R.id.action_postWriteFragment_to_postReadFragment,
                                            arg
                                        )
                                    }
                                }


                            } else {

                                val fileName = if (uploadUri == null) {
                                    "None"
                                } else {
                                    "image/img_${System.currentTimeMillis()}.jpg"
                                }


                                //제목 내용 유효성 검사
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
                                if (postType == 0) {
                                    val builder = MaterialAlertDialogBuilder(boardMainActivity)
                                    builder.setTitle("게시판 종류 선택 오류")
                                    builder.setMessage("게시판 종류를 선택해주세요")
                                    builder.setPositiveButton("확인", null)
                                    builder.show()
                                    return@setOnMenuItemClickListener true
                                }

                                PostRepository.getPostIdx { task ->
                                    var postIdx = task.result.value as Long
                                    //게시글 인덱스 증가
                                    postIdx++


                                    val post = Post(
                                        postIdx,
                                        postType.toLong(),
                                        subject,
                                        text,
                                        writeDate,
                                        fileName!!,
                                        boardMainActivity.loginUser.userIdx
                                    )

                                    PostRepository.addPostInfo(post) {
                                        PostRepository.setPostIdx(postIdx) {

                                            if (uploadUri != null) {

                                                PostRepository.uploadImage(fileName, uploadUri) {
                                                    showSnackBar(
                                                        fragmentPostWriteBinding.root,
                                                        "저장됐습니다."
                                                    )

//                                                postViewModel.setPost(post)
                                                    val arg = Bundle()
                                                    arg.putLong("postIdx", postIdx)
                                                    findNavController().navigate(
                                                        R.id.action_postWriteFragment_to_postReadFragment,
                                                        arg
                                                    )
                                                }

                                            } else {
                                                showSnackBar(
                                                    fragmentPostWriteBinding.root,
                                                    "저장됐습니다."
                                                )

//                                            postViewModel.setPost(post)
                                                val arg = Bundle()
                                                arg.putLong("postIdx", postIdx)
                                                findNavController().navigate(
                                                    R.id.action_postWriteFragment_to_postReadFragment,
                                                    arg
                                                )
                                            }
                                        }
                                    }
                                }
                            }


                        }
                    }
                    true
                }
            }

            button.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(boardMainActivity)
                builder.setTitle("게시판 종류")
                builder.setItems(boardMainActivity.postTypeList) { dialogInterface: DialogInterface, i: Int ->
                    postType = i + 1
                    button.text = boardMainActivity.postTypeList[i]
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

    private fun showSnackBar(view: View, s: String) {
        Snackbar.make(
            view,
            s,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    //카메라 관련 설정
    fun cameraSetting(previewImageView: ImageView): ActivityResultLauncher<Intent> {
        //사진 촬영을 위한 런처
        val cameraContract = ActivityResultContracts.StartActivityForResult()
        val cameraLauncher = registerForActivityResult(cameraContract) {

            if (it?.resultCode == AppCompatActivity.RESULT_OK) {
                //새로운 이미지 선택 여부값 설정
                if (isModify) isSelectNewImage = true


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
                    //새로운 이미지 선택 여부값 설정
                    if (isModify) isSelectNewImage = true

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
                        }
                    }
                }
            }
        }
        return albumLauncher
    }

}