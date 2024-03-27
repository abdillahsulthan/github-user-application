package com.dicoding.githubuser.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.FavoriteUserRepository
import com.dicoding.githubuser.data.local.entity.FavoriteUser

class FavoriteGithubUserViewModel(mApplication: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavoriteUserRepository : FavoriteUserRepository = FavoriteUserRepository(mApplication)

    fun getAllFavoriteUser() : LiveData<List<FavoriteUser>> {
        _isLoading.value = false
        return mFavoriteUserRepository.getAllFavoriteUser()
    }
}