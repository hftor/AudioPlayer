package com.test.audioplayer.ui.player

import android.app.Activity
import android.arch.lifecycle.ViewModel
import com.test.audioplayer.data.PlayerRepository
import java.io.File

/**
 * Created by hafthorg on 07/11/2018.
 */
class PlayerViewModel(private val playerRepository: PlayerRepository) : ViewModel(){
    private val CURRENT_SONG : String = "current_song_name"

    fun getFiles() = playerRepository.getFiles()
    fun populateFiles( files: List<File>) = playerRepository.populateFiles(files)
    fun saveCurrentSong(activity: Activity, value: String) = playerRepository.saveCurrentSong(activity, CURRENT_SONG, value)
    fun getLastPlayedSong(activity: Activity) = playerRepository.getCurrentSong(activity, CURRENT_SONG)
}