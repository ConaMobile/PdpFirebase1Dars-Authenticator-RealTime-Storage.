package com.conamobile.pdpfirebase1dars.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.conamobile.pdpfirebase1dars.R
import com.conamobile.pdpfirebase1dars.adapter.PostAdapter
import com.conamobile.pdpfirebase1dars.database.DatabaseHandler
import com.conamobile.pdpfirebase1dars.database.DatabaseManager
import com.conamobile.pdpfirebase1dars.managers.AuthManager
import com.conamobile.pdpfirebase1dars.model.Post
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : BaseActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var exitBtn: ImageView
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        findViewById(R.id.swipeRefreshLayout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        exitBtn = findViewById(R.id.exitBtn)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        val fabBtn = findViewById<FloatingActionButton>(R.id.floatingBtn)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }

        fabBtn.setOnClickListener {
            callCreateActivity()
        }

        exitBtn.setOnClickListener {
            AuthManager.signOut()
            callSignInActivity(this)
        }

        apiLoadPosts()
    }

    fun apiLoadPosts() {
        showLoading(this)
        DatabaseManager.apiLoadPosts(object : DatabaseHandler {

            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                dismissLoading()
                refreshAdapter(posts)
            }

            override fun onError() {
                dismissLoading()
            }

        })
    }

    fun apiDeletePost(post: Post) {

        DatabaseManager.apiDeletePost(post, object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                apiLoadPosts()
            }

            override fun onError() {
                TODO("Not yet implemented")
            }
        })

    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // Load all posts...
                apiLoadPosts()
            }
        }

    private fun callCreateActivity() {

        val intent = Intent(context, CreateActivity::class.java)
        resultLauncher.launch(intent)

    }

    fun refreshAdapter(posts: ArrayList<Post>) {

        val adapter = PostAdapter(this, posts)
        recyclerView.adapter = adapter

    }
}