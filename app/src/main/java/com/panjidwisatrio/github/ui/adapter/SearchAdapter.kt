package com.panjidwisatrio.github.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.panjidwisatrio.github.databinding.ItemRowUserBinding
import com.panjidwisatrio.github.model.User
import com.panjidwisatrio.github.ui.detail.DetailActivity
import com.panjidwisatrio.github.util.Constanta.EXTRA_USER

class SearchAdapter : ListAdapter<User, SearchAdapter.ViewHolder>(DIFF_UTIL) {

    // DONE: Membuat DiffUtil untuk mengecek perubahan data pada RecyclerView
    companion object {
        // membuat object DiffUtil
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<User>() {
            // mengecek apakah item sama atau tidak
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.username == newItem.username
            }

            // mengecek apakah konten item sama atau tidak
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    // merepresentasikan setiap item pada daftar pengguna
    inner class ViewHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // binding data ke view
        fun bind(user: User) {
            binding.apply {
                username.text = user.username
            }

            // menggunakan Glide untuk menampilkan gambar
            Glide.with(itemView.context)
                .load(user.avatar)
                .into(binding.avatars)

            // menambahkan aksi ketika item di klik
            itemView.setOnClickListener {
                // membuat intent untuk berpindah ke DetailActivity
                itemView.context.startActivity(
                    Intent(itemView.context, DetailActivity::class.java)
                        // menambahkan data username ke intent
                        .putExtra(EXTRA_USER, user.username)
                )
            }
        }
    }

    /* membuat sebuah instance dari ViewHolder, yaitu dengan menghubungkan layout item_row_user
       sebagai tampilan dari setiap item pada RecyclerView. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRowUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    // menampilkan data pada posisi tertentu pada RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder) {
        bind(getItem(position))
    }
}
