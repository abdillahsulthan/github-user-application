package com.dicoding.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.data.local.entity.FavoriteUser
import com.dicoding.githubuser.data.local.room.FavoriteUserDao
import com.dicoding.githubuser.data.local.room.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao : FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUser()

    fun getFavoriteByUsername(username : String): LiveData<FavoriteUser> = mFavoriteUserDao.getFavoriteUserByUsername(username)

    fun insert (favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }

    fun delete (favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }

}