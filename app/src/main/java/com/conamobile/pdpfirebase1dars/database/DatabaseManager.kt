package com.conamobile.pdpfirebase1dars.database

import com.conamobile.pdpfirebase1dars.model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DatabaseManager {
    companion object {
        private var database = FirebaseDatabase.getInstance().reference
        private var reference = database.child("posts")

        fun storePost(post: Post, handler: DatabaseHandler) {
            val key = reference.push().key
            if (key == null) return
            post.id = key
            reference.child(key).setValue(post)
                .addOnSuccessListener {
                    handler.onSuccess()
                }.addOnFailureListener {
                    handler.onError()
                }
        }

        fun apiLoadPosts(handler: DatabaseHandler) {
            var posts = ArrayList<Post>()
            reference.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    //snapshot.exists null bo'lmasa degani
                    if (snapshot.exists()) {
                        posts.clear()
                        for (snap in snapshot.children) {
                            val post: Post? = snap.getValue(Post::class.java)
                            post.let {
                                posts.add(post!!)
                            }
                        }

                        handler.onSuccess(posts = posts)
                    } else {
                        handler.onSuccess(posts = ArrayList())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    handler.onError()
                }
            })
        }

        fun apiDeletePost(post: Post, databaseHandler: DatabaseHandler) {
            val key = post.id
            reference.child(key!!).removeValue()
                .addOnSuccessListener {
                    databaseHandler.onSuccess()
                }.addOnFailureListener {
                    databaseHandler.onError()
                }
        }
    }
}