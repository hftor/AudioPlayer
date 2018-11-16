package com.test.audioplayer.data

import android.media.MediaPlayer
import com.mtechviral.mplaylib.MusicFinder
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.test.audioplayer.BR
import java.util.*

/**
 * Created by hafthorg on 07/11/2018.
 */
class Player(var mediaPlayer: MediaPlayer) : BaseObservable() {

    val DEFAULT_DURATION : String = "00:00:00"

    var _songDuration : String = DEFAULT_DURATION

    var _song : MusicFinder.Song = MusicFinder.Song(0, null, null, null,0, 0)

    var songDuration : String
        @Bindable get() = _songDuration
        set(value) {
            _songDuration = value
            notifyPropertyChanged(BR._all)
        }

    var song : MusicFinder.Song
        @Bindable get() = _song
        set(value) {
            _song = value
            setSongDuration()
            notifyPropertyChanged(BR._all)
        }
    
    lateinit var songs : MutableList<MusicFinder.Song>



    private fun setSongDuration(){
        val durationMilliSec = song.duration
        if(durationMilliSec <= 0)
        {
            songDuration = DEFAULT_DURATION
        }

        var a = Date(durationMilliSec)
        songDuration = "${String.format("%02d",a.hours)}:${String.format("%02d",a.minutes)}:${String.format("%02d",a.seconds)}"

    }
}