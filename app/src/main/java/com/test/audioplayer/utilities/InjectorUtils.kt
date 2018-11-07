package com.test.audioplayer.utilities

import com.test.audioplayer.data.FakeDatabase
import com.test.audioplayer.data.PlayerRepository
import com.test.audioplayer.ui.player.PlayerViewModelFactory

/**
 * Created by hafthorg on 07/11/2018.
 */
object InjectorUtils {
    fun providePlayerViewModelFactory() : PlayerViewModelFactory{
        val playerRepository = PlayerRepository.getInstance(FakeDatabase.getFakeInstance().playerDao)
        return PlayerViewModelFactory(playerRepository)
    }
}