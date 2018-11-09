package com.test.audioplayer.ui.player

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mtechviral.mplaylib.MusicFinder
import com.test.audioplayer.R
import com.test.audioplayer.data.PlayerRepository
import com.test.audioplayer.utilities.InjectorUtils
import kotlinx.android.synthetic.main.activity_player.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.imageURI


class PlayerActivity : AppCompatActivity() {

    protected var songCurrentIndex : Int = 0
    protected var songMaxIndex : Int = 0
    protected lateinit var song : MusicFinder.Song
    protected lateinit var songs : MutableList<MusicFinder.Song>
    protected lateinit var vm : PlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        initializeViewModel()

        kotlinx.coroutines.experimental.async {
            var songJob = kotlinx.coroutines.experimental.async {
                val songFinder = MusicFinder(contentResolver)
                songFinder.prepare()
                songFinder.allSongs
            }

            songs = songJob.await()
            initializePlayer()

            previousButton.setOnClickListener({
                playPrevious()
            })

            rewindButton.setOnClickListener {
                rewind()
            }

            playOrPauseButton.setOnClickListener {
                playOrPause()
            }

            forwardButton.setOnClickListener {
                fastForward()
            }

            nextButton.setOnClickListener({
                playNext()
            })
        }
    }

    private fun initializePlayer(){
        if(!songs.any()){
            songTitle.text = "No songs :("
            return
        }
        song = getLastPlayedSong()
        songMaxIndex = songs.count() - 1


        vm.mediaPlayer?.reset()
        vm.mediaPlayer = MediaPlayer.create(ctx,song.uri)
    }

    private fun getLastPlayedSong() : MusicFinder.Song{
        var songTitle = vm.getLastPlayedSong(this)

        songs.forEach {
            s -> if(s.title == songTitle){
                songCurrentIndex = songs.indexOf(s)
                return s
            }
        }

        return songs[0]
    }

    private fun rewind(){
        vm.mediaPlayer?.seekTo(getSongCurrentPosition() - 10000)
    }

    private fun playNext(){
        if(songCurrentIndex >= songMaxIndex){
            return
        }

        song = songs[++songCurrentIndex]
        play(true)
    }

    private fun fastForward(){
        vm.mediaPlayer?.seekTo(getSongCurrentPosition() + 10000)
    }

    private fun playPrevious(){
        if(songCurrentIndex <= 0){
            return
        }

        song = songs[--songCurrentIndex]
        play(true)
    }

    private fun getSongCurrentPosition() : Int
    {
        var currPos = vm.mediaPlayer?.currentPosition
        return if(currPos == null) 0 else currPos
    }

    private fun initializeViewModel(){
        val factory = InjectorUtils.providePlayerViewModelFactory()
        vm = ViewModelProviders.of(this, factory)
                .get(PlayerViewModel::class.java)
    }

    fun play(newSong: Boolean = false){
        if(newSong){
            vm.mediaPlayer?.reset()
            vm.mediaPlayer = MediaPlayer.create(ctx,song.uri)
        }
        songArtist.text = song.artist
        songTitle?.text = song.title
        imageView.imageURI = song.albumArt
        vm.mediaPlayer?.start()
        vm.saveCurrentSong(this, song.title)
    }

    fun pause(){
        vm.mediaPlayer?.pause()
    }

    fun playOrPause(){
        var songPlaying:Boolean? = vm.mediaPlayer?.isPlaying

        if(songPlaying == true){
            pause()
            playOrPauseButton?.imageResource = R.drawable.ic_play_circle_outline_black_24dp
        }
        else{
            play()
            playOrPauseButton?.imageResource = R.drawable.ic_pause_circle_outline_black_24dp
        }
    }
}
