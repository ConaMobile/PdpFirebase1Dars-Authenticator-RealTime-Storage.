package com.conamobile.pdpfirebase1dars.database

import com.conamobile.pdpfirebase1dars.model.Post

interface DatabaseHandler {
    fun onSuccess(post: Post? = null, posts: ArrayList<Post> = ArrayList())
    fun onError()
}