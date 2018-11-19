package com.test.audioplayer.ui.mainMenu

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mtechviral.mplaylib.MusicFinder
import com.test.audioplayer.R
import com.test.audioplayer.databinding.ActivityMainMenuBinding
import com.test.audioplayer.databinding.ActivityPlayerBinding
import com.test.audioplayer.ui.player.PlayerViewModel
import com.test.audioplayer.utilities.InjectorUtils
import org.jetbrains.anko.ctx

class MainMenuActivity : AppCompatActivity() {

    protected lateinit var vm : MainMenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        initializeViewModel()

        kotlinx.coroutines.experimental.async {
            var songJob = kotlinx.coroutines.experimental.async {
                val songFinder = MusicFinder(contentResolver)
                songFinder.prepare()
                songFinder.allSongs
            }

            vm.player.songs = songJob.await()
            vm.player.song = vm.player.songs[0]
        }
    }

    private fun initializeViewModel(){
        val binding: ActivityMainMenuBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_menu)

        val factory = InjectorUtils.provideMainMenuViewModelFactory()
        vm = ViewModelProviders.of(this, factory)
                .get(MainMenuViewModel::class.java)

        binding.player = vm.player
    }
}
