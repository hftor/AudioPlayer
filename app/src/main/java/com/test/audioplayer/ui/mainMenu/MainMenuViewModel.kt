package com.test.audioplayer.ui.mainMenu

import android.arch.lifecycle.ViewModel
import android.media.MediaPlayer
import com.test.audioplayer.data.Player

/**
 * Created by hafthorg on 19/11/2018.
 */
class MainMenuViewModel : ViewModel() {

    var player : Player = Player(MediaPlayer())
}