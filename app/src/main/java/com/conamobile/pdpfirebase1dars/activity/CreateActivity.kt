package com.conamobile.pdpfirebase1dars.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.conamobile.pdpfirebase1dars.R
import com.conamobile.pdpfirebase1dars.database.DatabaseHandler
import com.conamobile.pdpfirebase1dars.database.DatabaseManager
import com.conamobile.pdpfirebase1dars.model.Post
import com.conamobile.pdpfirebase1dars.storage.StorageHandler
import com.conamobile.pdpfirebase1dars.storage.StorageManager
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter

class CreateActivity : BaseActivity() {
    private lateinit var iv_photo: ImageView
    var pickPhoto: Uri? = null
    var allPhotos = ArrayList<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        initViews()
    }

    private fun initViews() {
        val iv_close = findViewById<ImageView>(R.id.iv_close)
        val et_title = findViewById<EditText>(R.id.et_title)
        val et_body = findViewById<EditText>(R.id.et_body)
        val b_create = findViewById<Button>(R.id.b_create)
        iv_photo = findViewById<ImageView>(R.id.iv_photo)
        val iv_camera = findViewById<ImageView>(R.id.iv_camera)


        iv_camera.setOnClickListener {
            pickUserPhoto()

        }
        iv_close.setOnClickListener {
            finish()
        }
        b_create.setOnClickListener {
            val title = et_title.text.toString().trim()
            val body = et_body.text.toString().trim()
            val post = Post(title, body)
            storePost(post)
        }
    }


    private fun pickUserPhoto() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setCamera(true)
            .exceptGif(true)
            .setButtonInAlbumActivity(false)
            .setCamera(true)
            .exceptGif(true)
            .setReachLimitAutomaticClose(true)
            .setAllViewTitle("All")
            .setMenuAllDoneText("All Done")
            .exceptMimeType(listOf(MimeType.GIF))
            .hasCameraInPickerPage(true)
            .setButtonInAlbumActivity(true)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)


    }

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                allPhotos =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickPhoto = allPhotos.get(0)
                iv_photo.setImageURI(pickPhoto)
            }
        }

    private fun storePost(post: Post) {
        if (pickPhoto != null) {
            storeStorage(post)
        } else {
            storeDatabase(post)
        }
    }

    private fun storeStorage(post: Post) {
        showLoading(this)
        StorageManager.uploadPhoto(pickPhoto!!, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                post.img = imgUrl
                storeDatabase(post)
            }

            override fun onError(exception: Exception?) {
                storeDatabase(post)
            }
        })
    }


    fun storeDatabase(post: Post) {
        DatabaseManager.storePost(post, object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                Log.d("@@@", "post is saved")
                dismissLoading()
                finishIntent()
            }

            override fun onError() {
                dismissLoading()
                Log.d("@@@", "post is not saved")
            }
        })
    }

    fun finishIntent() {
        val returnIntent = intent
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}