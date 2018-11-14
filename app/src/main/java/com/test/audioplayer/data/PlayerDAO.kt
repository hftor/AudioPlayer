package com.test.audioplayer.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import java.io.File
import android.content.Context
import android.app.Activity;

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

    fun saveCurrentSong(activity: Activity, key: String, value: String){
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun saveSongPosition(activity: Activity, key: String, value: Int){
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(key, value)
            commit()
        }
    }

    fun getCurrentSong(activity: Activity, key: String) : String{
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return ""
        val defaultValue = ""
        return sharedPref.getString(key, defaultValue)
    }

    fun getSongPosition(activity: Activity, key: String) : Int{
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return 0
        val defaultValue = 0
        return sharedPref.getInt(key, defaultValue)
    }

    fun getFiles() = files
}