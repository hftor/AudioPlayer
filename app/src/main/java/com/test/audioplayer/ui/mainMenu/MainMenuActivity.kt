package com.test.audioplayer.ui.mainMenu

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mtechviral.mplaylib.MusicFinder
import com.test.audioplayer.R
import org.jetbrains.anko.ctx

class MainMenuActivity : AppCompatActivity() {

    protected lateinit var vm : MainMenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        kotlinx.coroutines.experimental.async {
            var songJob = kotlinx.coroutines.experimental.async {
                val songFinder = MusicFinder(contentResolver)
                songFinder.prepare()
                songFinder.allSongs
            }

            vm.player.songs = songJob.await()
            //initializePlayer()
            //initializeButtons()
        }
    }
}
