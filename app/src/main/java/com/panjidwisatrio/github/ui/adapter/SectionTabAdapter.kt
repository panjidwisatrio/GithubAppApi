package com.panjidwisatrio.github.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.panjidwisatrio.github.ui.followers.FollowersFragment
import com.panjidwisatrio.github.ui.following.FollowingFragment
import com.panjidwisatrio.github.util.Constanta.TAB_TITLES

class SectionTabAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity) {
    // mengembalikan jumlah tab yang akan ditampilkan
    override fun getItemCount(): Int = TAB_TITLES.size

    // membuat fragment sesuai dengan posisi tab
    override fun createFragment(position: Int): Fragment {
        // membuat variabel fragment yang bertipe Fragment? (nullable)
        var fragment: Fragment? = null
        // menentukan fragment yang akan ditampilkan sesuai dengan posisi tab
        when (position) {
            // jika posisi tab 0 maka akan menampilkan fragment FollowersFragment
            0 -> fragment = FollowersFragment.getInstance(username)
            // jika posisi tab 1 maka akan menampilkan fragment FollowingFragment
            1 -> fragment = FollowingFragment.getInstance(username)
        }
        // mengembalikan nilai fragment yang bertipe Fragment
        return fragment as Fragment
    }
}