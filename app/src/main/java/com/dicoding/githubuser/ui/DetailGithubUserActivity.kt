package com.dicoding.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.local.entity.FavoriteUser
import com.dicoding.githubuser.data.remote.response.DetailUserResponse
import com.dicoding.githubuser.databinding.ActivityDetailGithubUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailGithubUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailGithubUserBinding

    private val detailViewModel by viewModels<DetailGithubUserViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val username = "username"
        const val avatarURL = "url"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGithubUserBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val usernameData = intent.getStringExtra(username)
        val avatarUrlData = intent.getStringExtra(avatarURL)

        if (usernameData != null) {
            detailViewModel.findDetailGithubUser(usernameData)
        }
        detailViewModel.userDetail.observe(this){
                userDetail -> setDetailUser(userDetail, usernameData!!, avatarUrlData)
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        if (usernameData != null) {
            sectionsPagerAdapter.username = usernameData
        }
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.getFavoriteUser(usernameData!!).observe(this) { user ->
            if (user == null) {
                binding.fabFav.setImageDrawable(ContextCompat.getDrawable(binding.fabFav.context, R.drawable.ic_favorite_border))
                binding.fabFav.setOnClickListener {
                    detailViewModel.insertFavoriteUser(FavoriteUser(usernameData, avatarUrlData))
                }
            } else {
                binding.fabFav.setImageDrawable(ContextCompat.getDrawable(binding.fabFav.context, R.drawable.ic_favorite))
                binding.fabFav.setOnClickListener {
                    detailViewModel.deleteFavoriteUser(user)
                }
            }
        }
    }

    private fun setDetailUser(responseBody: DetailUserResponse, usernameData: String, avatarUrlData: String?) {
        binding.textViewName.text = responseBody.name
        binding.textViewFollower.text = getString(R.string.user_follower, responseBody.followers)
        binding.textViewFollowing.text = getString(R.string.user_following, responseBody.following)
        Glide.with(binding.root).load(avatarUrlData).into(binding.imageViewProfile)
        binding.textViewUsername.text = usernameData
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}