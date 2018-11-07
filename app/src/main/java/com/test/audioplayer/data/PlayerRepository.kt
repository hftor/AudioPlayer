package com.test.audioplayer.data

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