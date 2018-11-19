package com.test.audioplayer.data

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import com.mtechviral.mplaylib.MusicFinder
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.test.audioplayer.BR
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by hafthorg on 07/11/2018.
 */
class Player(var mediaPlayer: MediaPlayer) : BaseObservable() {

    val DEFAULT_DURATION : String = "00:00:00"

    var _songDuration : String = DEFAULT_DURATION
    var _songCurrentPosition : String = DEFAULT_DURATION

    var _song : MusicFinder.Song = MusicFinder.Song(0, null, null, null,0, 0)

    var songDuration : String
        @Bindable get() = _songDuration
        set(value) {
            _songDuration = value
            notifyPropertyChanged(BR._all)
        }

    var songCurrentPosition : String
        @Bindable get() = _songCurrentPosition
        set(value) {
            _songCurrentPosition = value

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

    fun play(ctx: Context, newSong: Boolean = false){
        if(newSong){
            mediaPlayer.reset()
            mediaPlayer = MediaPlayer.create(ctx,song.uri)
        }

        mediaPlayer.start()
    }

    fun goToSongsSavedPosition(songPos: Int){
        mediaPlayer.seekTo(songPos)
        setSongCurrentPosition()
    }

    private fun setSongDuration(){
        val durationMilliSec = song.duration
        if(durationMilliSec <= 0)
        {
            songDuration = DEFAULT_DURATION
        }

        var a = Date(durationMilliSec)
        songDuration = "${String.format("%02d",a.hours)}:${String.format("%02d",a.minutes)}:${String.format("%02d",a.seconds)}"
    }

    fun setSongCurrentPosition(){
        val durationMilliSec = mediaPlayer.currentPosition.toLong()
        if(durationMilliSec <= 0)
        {
            songCurrentPosition = DEFAULT_DURATION
        }

        var a = Date(durationMilliSec)
        songCurrentPosition = "${String.format("%02d",a.hours)}:${String.format("%02d",a.minutes)}:${String.format("%02d",a.seconds)}"

        Timer().schedule(1000){
            setSongCurrentPosition()
        }
    }


}