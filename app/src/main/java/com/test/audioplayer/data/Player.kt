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

    var _songDurationSec : Int = 0
    var _songDuration : String = DEFAULT_DURATION

    var _songCurrentPositionSec : Int = 0
    var _songCurrentPosition : String = DEFAULT_DURATION

    var songCurrentIndex : Int = 0
    var songMaxIndex : Int = 0

    var _song : MusicFinder.Song = MusicFinder.Song(0, null, null, null,0, 0)

    var songDurationSec : Int
        @Bindable get() = _songDurationSec
        set(value) {
            _songDurationSec = value
            notifyPropertyChanged(BR._all)
        }

    var songDuration : String
        @Bindable get() = _songDuration
        set(value) {
            _songDuration = value
            notifyPropertyChanged(BR._all)
        }

    var songCurrentPositionSec : Int
        @Bindable get() = _songCurrentPositionSec
        set(value) {
            _songCurrentPositionSec = value
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

    fun playNext(ctx: Context){
        if(songCurrentIndex >= songMaxIndex){
            return
        }

        song = songs[++songCurrentIndex]
        play(ctx,true)
    }

    fun playPrevious(ctx: Context){
        if(songCurrentIndex <= 0){
            return
        }

        song = songs[--songCurrentIndex]
        play(ctx,true)
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
        songDurationSec = (song.duration / 1000).toInt()
    }

    fun setSongCurrentPosition(currentPosToGoTo : Int = -1){
        val currentPositionMilliSec = if(currentPosToGoTo == -1)
            mediaPlayer.currentPosition.toLong() else
            (currentPosToGoTo * 1000).toLong()

        if(currentPositionMilliSec <= 0)
        {
            songCurrentPosition = DEFAULT_DURATION
            songCurrentPositionSec = 0
        }

        if(currentPosToGoTo != -1){
            mediaPlayer.seekTo(currentPosToGoTo * 1000)
        }

        var a = Date(currentPositionMilliSec)
        songCurrentPosition = "${String.format("%02d",a.hours)}:${String.format("%02d",a.minutes)}:${String.format("%02d",a.seconds)}"
        songCurrentPositionSec = mediaPlayer.currentPosition / 1000

        Timer().schedule(1000){
            setSongCurrentPosition()
        }
    }


}