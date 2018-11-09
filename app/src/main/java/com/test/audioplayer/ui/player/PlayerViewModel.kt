package com.test.audioplayer.ui.player

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.media.MediaPlayer
import com.mtechviral.mplaylib.MusicFinder
import com.test.audioplayer.data.PlayerRepository
import java.io.File

/**
 * Created by hafthorg on 07/11/2018.
 */
class PlayerViewModel(private val playerRepository: PlayerRepository) : ViewModel(){
    private val CURRENT_SONG : String = "current_song_name1"
    private val SONG_POSITION : String = "song_position1"

    var mediaPlayer: MediaPlayer? = null

    var songCurrentIndex : Int = 0
    var songMaxIndex : Int = 0

    lateinit var song : MusicFinder.Song
    lateinit var songs : MutableList<MusicFinder.Song>

    fun getFiles() = playerRepository.getFiles()
    fun populateFiles( files: List<File>) = playerRepository.populateFiles(files)


    fun play(ctx: Context, newSong: Boolean = false){
        if(newSong){
            mediaPlayer?.reset()
            mediaPlayer = MediaPlayer.create(ctx, song.uri)
        }

        mediaPlayer?.start()
    }

    fun pause(){
        mediaPlayer?.pause()
    }

    fun fastForward(){
        mediaPlayer?.seekTo(getSongCurrentPosition() + 10000)
    }

    fun rewind(){
        mediaPlayer?.seekTo(getSongCurrentPosition() - 10000)
    }

    fun getLastPlayedSong(activity: Activity) : MusicFinder.Song{
        var songTitle = playerRepository.getCurrentSong(activity, CURRENT_SONG)

        songs.forEach {
            s -> if(s.title == songTitle){
            songCurrentIndex = songs.indexOf(s)
            return s
            }
        }

        return songs[0]
    }

    fun goToSongsSavedPosition(activity: Activity){
        var songPos = playerRepository.getSongPosition(activity, SONG_POSITION)
        mediaPlayer?.seekTo(songPos)
    }

    fun getSongCurrentPosition() : Int
    {
        var currPos = mediaPlayer?.currentPosition
        return if(currPos == null) 0 else currPos
    }

    fun saveState(activity: Activity){
        playerRepository.saveCurrentSong(activity, CURRENT_SONG, song.title)
        playerRepository.saveSongPosition(activity, SONG_POSITION, getSongCurrentPosition())
    }
}