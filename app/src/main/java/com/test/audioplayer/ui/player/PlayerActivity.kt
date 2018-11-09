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

            vm.songs = songJob.await()
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
        if(!vm.songs.any()){
            songTitle.text = "No songs :("
            return
        }
        vm.song = vm.getLastPlayedSong(this)
        vm.songMaxIndex = vm.songs.count() - 1


        vm.mediaPlayer?.reset()
        vm.mediaPlayer = MediaPlayer.create(ctx,vm.song.uri)
    }


    private fun rewind(){
        vm.mediaPlayer?.seekTo(getSongCurrentPosition() - 10000)
    }

    private fun playNext(){
        if(vm.songCurrentIndex >= vm.songMaxIndex){
            return
        }

        vm.song = vm.songs[++vm.songCurrentIndex]
        play(true)
    }

    private fun fastForward(){
        vm.mediaPlayer?.seekTo(getSongCurrentPosition() + 10000)
    }

    private fun playPrevious(){
        if(vm.songCurrentIndex <= 0){
            return
        }

        vm.song = vm.songs[--vm.songCurrentIndex]
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
            vm.mediaPlayer = MediaPlayer.create(ctx,vm.song.uri)
        }
        songArtist.text = vm.song.artist
        songTitle?.text = vm.song.title
        imageView.imageURI = vm.song.albumArt
        vm.mediaPlayer?.start()
        vm.saveCurrentSong(this, vm.song.title)
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
