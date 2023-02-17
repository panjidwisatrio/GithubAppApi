package com.panjidwisatrio.github.ui.following

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.panjidwisatrio.github.data.Resource
import com.panjidwisatrio.github.databinding.FragmentFollowingBinding
import com.panjidwisatrio.github.model.User
import com.panjidwisatrio.github.ui.adapter.SearchAdapter
import com.panjidwisatrio.github.util.ViewStateCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowingFragment :
    Fragment(), ViewStateCallback<List<User>> {

    private val viewModel: FollowingViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter
    private var username: String? = null
    private lateinit var binding: FragmentFollowingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(KEY_BUNDLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAdapter = SearchAdapter()

        showRecylerView()
        getUserFollowing(username.toString())
    }

    private fun getUserFollowing(username: String) {
        viewModel.getUserFollowing(username).observe(viewLifecycleOwner) {
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

    private fun showRecylerView() {
        binding.rvListUser.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onSuccess(data: List<User>) {
        Log.d("FollowingFragment", "onSuccess: $data")
        binding.apply {
            rvListUser.visibility = visible
            progressBarFollowing.visibility = invisible
            followingTextPlaceholder.visibility = invisible
        }
    }

    override fun onLoading() {
        binding.apply {
            rvListUser.visibility = invisible
            progressBarFollowing.visibility = visible
            followingTextPlaceholder.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {
        binding.apply {
            if (message == null) {
                followingTextPlaceholder.visibility = visible
            } else {
                followingTextPlaceholder.text = message
                followingTextPlaceholder.visibility = visible
            }

            progressBarFollowing.visibility = invisible
            rvListUser.visibility = invisible
        }
    }

    companion object {
        private const val KEY_BUNDLE = "USERNAME"

        fun getInstance(username: String): Fragment {
            return FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_BUNDLE, username)
                }
            }
        }
    }
}