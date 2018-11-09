package com.test.audioplayer.ui.player

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.media.MediaPlayer
import com.mtechviral.mplaylib.MusicFinder
import com.test.audioplayer.data.PlayerRepository
import kotlinx.android.synthetic.main.activity_player.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.imageURI
import java.io.File

/**
 * Created by hafthorg on 07/11/2018.
 */
class PlayerViewModel(private val playerRepository: PlayerRepository) : ViewModel(){
    private val CURRENT_SONG : String = "current_song_name"

    var mediaPlayer: MediaPlayer? = null

    var songCurrentIndex : Int = 0
    var songMaxIndex : Int = 0

    lateinit var song : MusicFinder.Song
    lateinit var songs : MutableList<MusicFinder.Song>

    fun getFiles() = playerRepository.getFiles()
    fun populateFiles( files: List<File>) = playerRepository.populateFiles(files)
    fun saveCurrentSong(activity: Activity, value: String) = playerRepository.saveCurrentSong(activity, CURRENT_SONG, value)

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

    fun getSongCurrentPosition() : Int
    {
        var currPos = mediaPlayer?.currentPosition
        return if(currPos == null) 0 else currPos
    }

    fun play(activity: Activity, ctx: Context, newSong: Boolean = false){
        if(newSong){
            mediaPlayer?.reset()
            mediaPlayer = MediaPlayer.create(ctx, song.uri)
        }

        mediaPlayer?.start()
        saveCurrentSong(activity, song.title)
    }
}