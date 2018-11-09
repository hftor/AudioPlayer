package com.test.audioplayer.ui.player

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mtechviral.mplaylib.MusicFinder
import com.test.audioplayer.R
import com.test.audioplayer.utilities.InjectorUtils
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.anko.ctx
import org.jetbrains.anko.image
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.imageURI
import java.util.*


class PlayerActivity : AppCompatActivity() {

    protected var songCurrentIndex : Int = 0
    protected var songMaxIndex : Int = 0
    protected lateinit var song : MusicFinder.Song
    protected lateinit var songs : MutableList<MusicFinder.Song>
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

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

        initializeUi()
    }

    private fun initializePlayer(){
        if(!songs.any()){
            songTitle.text = "No songs :("
            return
        }
        song = songs[0]
        songMaxIndex = songs.count() - 1
    }

    private fun rewind(){
        mediaPlayer?.seekTo(getSongCurrentPosition() - 10000)
    }

    private fun playNext(){
        if(songCurrentIndex >= songMaxIndex){
            return
        }

        song = songs[++songCurrentIndex]
        play()
    }

    private fun fastForward(){
        mediaPlayer?.seekTo(getSongCurrentPosition() + 10000)
    }

    private fun playPrevious(){
        if(songCurrentIndex <= 0){
            return
        }

        song = songs[--songCurrentIndex]
        play()
    }

    private fun getSongCurrentPosition() : Int
    {
        var currPos = mediaPlayer?.currentPosition
        return if(currPos == null) 0 else currPos
    }

    private fun initializeUi(){
        val factory = InjectorUtils.providePlayerViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory)
                .get(PlayerViewModel::class.java)

        viewModel.getFiles().observe(this, Observer { files ->
            val stringBuilder = StringBuilder()
            files?.forEach { file ->
                stringBuilder.append("$file\n\n")
            }
            songTitle.text = "someCool text"
        })
    }

    fun play(){

        mediaPlayer?.reset()
        mediaPlayer = MediaPlayer.create(ctx,song.uri)
        mediaPlayer?.setOnCompletionListener {
            play()
        }

        songArtist.text = song.artist
        songTitle?.text = song.title
        imageView.imageURI = song.albumArt
        mediaPlayer?.start()
    }

    fun pause(){
        mediaPlayer?.pause()
    }

    fun playOrPause(){
        var songPlaying:Boolean? = mediaPlayer?.isPlaying

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
