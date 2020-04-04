package com.img23.d_test

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.img23.d_test.Model.Photo
import com.squareup.picasso.Picasso

class PhortoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val photos: MutableList<Photo> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserHolder(parent.context)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserHolder).bind(photos[position])
    }

    fun addData(users: List<Photo>) {
        this.photos.addAll(users)
        notifyDataSetChanged()
    }

    fun setData(users: List<Photo>) {
        this.photos.clear()

        addData(users)
    }

    override fun getItemCount() = photos.size

    inner class UserHolder(context: Context?): RecyclerView.ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_photo, null)) {

        private val avatarVK: ImageView = itemView.findViewById(R.id.avatarVK)

        fun bind(photo: Photo) {

            if (!TextUtils.isEmpty(photo.image)) {
                Picasso.get().load(photo.image).error(R.drawable.user_placeholder).into(avatarVK)
            } else {
                avatarVK.setImageResource(R.drawable.user_placeholder)
            }
        }
    }
}