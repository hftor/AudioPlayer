package com.test.audioplayer.data

import android.app.Activity
import java.io.File
import java.nio.file.Files

/**
 * Created by hafthorg on 07/11/2018.
 */
class PlayerRepository private constructor(private val playerDao: PlayerDAO) {

    fun populateFiles(files : List<File>)
    {
        playerDao.populateFiles(files)
    }

    fun saveCurrentSong(activity: Activity, key: String, value: String){
        playerDao.saveCurrentSong(activity, key, value)
    }

    fun saveSongPosition(activity: Activity, key: String, value: Int){
        playerDao.saveSongPosition(activity, key, value)
    }

    fun getCurrentSong(activity: Activity, key: String) : String{
        return playerDao.getCurrentSong(activity, key)
    }

    fun getFiles() = playerDao.getFiles()

    companion object {
        @Volatile private var instance : PlayerRepository? = null

        fun getInstance(playerDao: PlayerDAO) =
                instance ?: synchronized(this){
                    instance ?: PlayerRepository(playerDao).also {
                        instance = it
                    }
                }
    }
}