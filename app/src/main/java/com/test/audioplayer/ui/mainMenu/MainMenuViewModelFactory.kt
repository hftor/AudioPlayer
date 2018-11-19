package com.test.audioplayer.ui.mainMenu

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * Created by hafthorg on 19/11/2018.
 */
class MainMenuViewModelFactory
    : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainMenuViewModel() as T
    }
}