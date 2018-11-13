package com.test.audioplayer.ui.player

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.media.MediaPlayer
import com.mtechviral.mplaylib.MusicFinder
import com.test.audioplayer.data.Player
import com.test.audioplayer.data.PlayerRepository
import java.io.File

/**
 * Created by hafthorg on 07/11/2018.
 */
class PlayerViewModel(private val playerRepository: PlayerRepository) : ViewModel(){
    private val CURRENT_SONG : String = "current_song_name1"
    private val SONG_POSITION : String = "song_position1"

    var songCurrentIndex : Int = 0
    var songMaxIndex : Int = 0


    var player : Player = Player(MediaPlayer())

    fun getFiles() = playerRepository.getFiles()
    fun populateFiles( files: List<File>) = playerRepository.populateFiles(files)


    fun play(ctx: Context, newSong: Boolean = false){
        if(newSong){
            player.mediaPlayer?.reset()
            player.mediaPlayer = MediaPlayer.create(ctx, player.song.uri)
        }

        player.mediaPlayer?.start()
    }

    fun pause(){
        player.mediaPlayer?.pause()
    }

    fun fastForward(){
        player.mediaPlayer?.seekTo(getSongCurrentPosition() + 10000)
    }

    fun rewind(){
        player.mediaPlayer?.seekTo(getSongCurrentPosition() - 10000)
    }

    fun getLastPlayedSong(activity: Activity) : MusicFinder.Song{
        var songTitle = playerRepository.getCurrentSong(activity, CURRENT_SONG)

        player.songs.forEach {
            s -> if(s.title == songTitle){
            songCurrentIndex = player.songs.indexOf(s)
            return s
            }
        }

        return player.songs[0]
    }

    fun goToSongsSavedPosition(activity: Activity){
        var songPos = playerRepository.getSongPosition(activity, SONG_POSITION)
        player.mediaPlayer?.seekTo(songPos)
    }

    fun getSongCurrentPosition() : Int
    {
        var currPos = player.mediaPlayer?.currentPosition
        return if(currPos == null) 0 else currPos
    }

    fun saveState(activity: Activity){
        playerRepository.saveCurrentSong(activity, CURRENT_SONG, player.song.title)
        playerRepository.saveSongPosition(activity, SONG_POSITION, getSongCurrentPosition())
    }
}