package com.conamobile.pdpfirebase1dars.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.conamobile.pdpfirebase1dars.R
import com.conamobile.pdpfirebase1dars.activity.MainActivity
import com.conamobile.pdpfirebase1dars.model.Post
import com.squareup.picasso.Picasso

class PostAdapter(var activity: MainActivity, var items: ArrayList<Post>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_list, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = items[position]
        if (holder is PostViewHolder) {
            holder.tv_title.text = post.title!!.toUpperCase()
            holder.tv_body.text = post.body

            if (post.img.isNotEmpty()) {
                Picasso.get()
                    .load(post.img)
                    .into(holder.iv_main)
            }

            holder.ll_post.setOnLongClickListener {
                activity.apiDeletePost(post)
                return@setOnLongClickListener false
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class PostViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tv_title: TextView
        var tv_body: TextView
        var ll_post: LinearLayout
        var iv_main: ImageView

        init {
            ll_post = view.findViewById(R.id.ll_post)
            tv_body = view.findViewById(R.id.tv_body)
            tv_title = view.findViewById(R.id.tv_title)
            iv_main = view.findViewById(R.id.iv_main)
        }
    }

    init {
        this.activity = activity
        this.items = items
    }

}