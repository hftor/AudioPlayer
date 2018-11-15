package com.test.audioplayer.data

import android.media.MediaPlayer
import com.mtechviral.mplaylib.MusicFinder
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.test.audioplayer.BR

/**
 * Created by hafthorg on 07/11/2018.
 */
class Player(var mediaPlayer: MediaPlayer) : BaseObservable() {
    var _song : MusicFinder.Song = MusicFinder.Song(0, null, null, null,0, 0)

    var song : MusicFinder.Song
        @Bindable get() = _song
        set(value) {
            _song = value
            notifyPropertyChanged(BR._all)
        }
    
    lateinit var songs : MutableList<MusicFinder.Song>
}