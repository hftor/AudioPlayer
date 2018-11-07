package com.test.audioplayer.ui.player

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.test.audioplayer.data.PlayerRepository

/**
 * Created by hafthorg on 07/11/2018.
 */
class PlayerViewModelFactory(private val playerRepository: PlayerRepository)
    : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayerViewModel(playerRepository) as T
    }
}