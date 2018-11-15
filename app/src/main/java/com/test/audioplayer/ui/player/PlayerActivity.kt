package com.test.audioplayer.ui.player

import android.arch.lifecycle.ViewModelProviders
import android.databinding.BindingAdapter
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mtechviral.mplaylib.MusicFinder
import com.test.audioplayer.R
import com.test.audioplayer.utilities.InjectorUtils
import kotlinx.android.synthetic.main.activity_player.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.imageResource

import android.databinding.DataBindingUtil
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.test.audioplayer.databinding.ActivityPlayerBinding

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

            vm.player.songs = songJob.await()
            initializePlayer()
            initializeButtons()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        vm.saveState(this)
        super.onSaveInstanceState(outState)
    }

    private fun initializePlayer(){
        if(!vm.player.songs.any()){
            return
        }

        vm.player.song = vm.getLastPlayedSong(this)
        vm.songMaxIndex = vm.player.songs.count() - 1

        vm.player.mediaPlayer.reset()
        vm.player.mediaPlayer = MediaPlayer.create(ctx,vm.player.song.uri)

        vm.goToSongsSavedPosition(this)
    }


    private fun rewind(){
        vm.rewind()
    }

    private fun fastForward(){
        vm.fastForward()
    }

    private fun playPrevious(){
        if(vm.songCurrentIndex <= 0){
            return
        }

        vm.player.song = vm.player.songs[--vm.songCurrentIndex]
        play(true)
    }

    private fun playNext(){
        if(vm.songCurrentIndex >= vm.songMaxIndex){
            return
        }

        vm.player.song = vm.player.songs[++vm.songCurrentIndex]
        play(true)
    }

    private fun pause(){
        vm.pause()
    }

    private fun play(newSong: Boolean = false){
        vm.play(ctx, newSong)
    }

    fun playOrPause(){
        var songPlaying:Boolean? = vm.player.mediaPlayer.isPlaying

        if(songPlaying == true){
            pause()
            playOrPauseButton?.imageResource = R.drawable.ic_play_circle_outline_black_24dp
        }
        else{
            play()
            playOrPauseButton?.imageResource = R.drawable.ic_pause_circle_outline_black_24dp
        }
    }

    private fun initializeViewModel(){
        val binding: ActivityPlayerBinding = DataBindingUtil.setContentView(this, R.layout.activity_player)

        val factory = InjectorUtils.providePlayerViewModelFactory()
        vm = ViewModelProviders.of(this, factory)
                .get(PlayerViewModel::class.java)

        binding.player = vm.player
    }

    private fun initializeButtons(){
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

@BindingAdapter("imageUri")
fun ImageView.setImageUri(uri: Uri?) {
    Glide.with(context)
            .load(uri)
            .into(this)
}
