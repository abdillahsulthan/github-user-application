package com.dicoding.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.remote.response.ItemsItem
import com.dicoding.githubuser.databinding.FragmentDetailFollowBinding

class DetailFollowFragment : Fragment() {

    private lateinit var binding: FragmentDetailFollowBinding
    private val detailViewModel: DetailFollowViewModel by activityViewModels()
    private var position: Int? = null
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailFollowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvGithubUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvGithubUser.addItemDecoration(itemDecoration)

        if (position == 1) {
            detailViewModel.findFollowers(username!!)
            detailViewModel.userFollowers.observe(viewLifecycleOwner){
                    followList -> setFollowGithubUser(followList)
            }
            detailViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }

        } else {
            detailViewModel.findFollowing(username!!)
            detailViewModel.userFollowing.observe(viewLifecycleOwner){
                    followList -> setFollowGithubUser(followList)
            }
            detailViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }
    }

    private fun setFollowGithubUser(githubUser: List<ItemsItem>) {
        val adapter = GithubUserAdapter()
        adapter.submitList(githubUser)
        binding.rvGithubUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_POSITION = "param1"
        const val ARG_USERNAME = "param2"
    }
}