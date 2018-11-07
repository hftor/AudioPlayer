package com.test.audioplayer.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import java.io.File

/**
 * Created by hafthorg on 07/11/2018.
 */
class PlayerDAO {
    private val filesList = mutableListOf<File>()
    private val files = MutableLiveData<List<File>>()

    init {
        files.value = filesList
    }

    fun populateFiles(files: List<File>){
        filesList.add(files[0])
    }

    fun getFiles() = files
}