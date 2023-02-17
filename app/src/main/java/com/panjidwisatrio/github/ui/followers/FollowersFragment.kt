package com.panjidwisatrio.github.ui.followers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.panjidwisatrio.github.data.Resource
import com.panjidwisatrio.github.databinding.FragmentFollowersBinding
import com.panjidwisatrio.github.model.User
import com.panjidwisatrio.github.ui.adapter.SearchAdapter
import com.panjidwisatrio.github.util.ViewStateCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowersFragment : Fragment(), ViewStateCallback<List<User>> {

    // inisialisasi viewmodel, adapter, username dan binding
    private val viewModel: FollowersViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter
    private var username: String? = null
    private lateinit var binding: FragmentFollowersBinding

    // lifecycle fragment oncreate yang dijalankan pertama kali
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // mengambil data dari bundle dengan lambda dan dengan key KEY_BUNDLE
        arguments?.let {
            username = it.getString(KEY_BUNDLE)
        }
    }

    // lifecycle fragment oncreateview yang dijalankan setelah oncreate
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // menghubungkan fragment_followers.xml dengan fragment
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        // mengembalikan view
        return binding.root
    }

    // lifecycle fragment onviewcreated yang dijalankan setelah oncreateview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // inisisalisasi adapter
        searchAdapter = SearchAdapter()

        showRecyclerView()
        getUserFollowers(username.toString())
    }

    private fun getUserFollowers(username: String) {
        viewModel.getUserFollowers(username).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> onFailed(it.message)
                is Resource.Loading -> onLoading()
                is Resource.Success -> it.data?.let { it1 ->
                    onSuccess(it1)
                    searchAdapter.submitList(it1)
                }
            }
        }
    }

    private fun showRecyclerView() {
        // menampilkan recyclerview
        binding.rvListUser.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onSuccess(data: List<User>) {
        binding.apply {
            rvListUser.visibility = visible
            followersTextPlaceholder.visibility = invisible
            progressBarFollowers.visibility = invisible
        }
    }

    override fun onLoading() {
        binding.apply {
            rvListUser.visibility = invisible
            followersTextPlaceholder.visibility = invisible
            progressBarFollowers.visibility = visible
        }
    }

    override fun onFailed(message: String?) {
        binding.apply {
            if (message == null) {
                followersTextPlaceholder.visibility = visible
            } else {
                followersTextPlaceholder.text = message
                followersTextPlaceholder.visibility = visible
            }
            progressBarFollowers.visibility = invisible
            rvListUser.visibility = invisible
        }
    }

    companion object {
        // membuat key untuk bundle
        private const val KEY_BUNDLE = "USERNAME"

        // membuat fungsi getInstance untuk mengirimkan data ke fragment
        fun getInstance(username: String): Fragment {
            // mengembalikan fragment dengan mengirimkan data ke bundle
            return FollowersFragment().apply {
                // membuat bundle
                arguments = Bundle().apply {
                    // mengirimkan data ke bundle dengan key KEY_BUNDLE
                    putString(KEY_BUNDLE, username)
                }
            }
        }
    }

}