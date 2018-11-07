package com.test.audioplayer.ui.player

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mtechviral.mplaylib.MusicFinder
import com.test.audioplayer.R
import com.test.audioplayer.utilities.InjectorUtils
import kotlinx.android.synthetic.main.activity_player.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.image
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.imageURI
import java.util.*


class PlayerActivity : AppCompatActivity() {

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
            if(songs == null || !songs.any()){
                songTitle.text = "No songs :("
            }
            else{
                song = songs[0]
            }

            playRandom()
        }

        initializeUi()
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

    private suspend fun getFiles(){

    }

    fun playRandom(){

        mediaPlayer?.reset()
        mediaPlayer = MediaPlayer.create(ctx,song.uri)
        mediaPlayer?.setOnCompletionListener {
            playRandom()
        }
        //albumArt?.imageURI = song.albumArt
        songTitle?.text = song.title
        imageView.imageURI = song.albumArt
        //songArtist?.text = song.artist
        //mediaPlayer?.start()
        //playButton?.imageResource = R.drawable.ic_play_circle_outline_black_24dp
    }

    fun playOrPause(){
        var songPlaying:Boolean? = mediaPlayer?.isPlaying

//        if(songPlaying == true){
//            mediaPlayer?.pause()
//            playButton?.imageResource = R.drawable.ic_play_circle_outline_black_24dp
//        }
//        else{
//            mediaPlayer?.start()
//            playButton?.imageResource = R.drawable.ic_pause_circle_outline_black_24dp
//        }
    }
}
