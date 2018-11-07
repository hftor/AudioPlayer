package com.test.audioplayer

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.mtechviral.mplaylib.MusicFinder

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    var songTitle: TextView? = null
    var songArtist: TextView? = null
    var albumArt: ImageView? = null

    var playButton: ImageButton? = null;

    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        }
        else
        {
            createPlayer()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            createPlayer()
        }
        else
        {
            longToast("Permission not granterd")
        }
    }

    private fun createPlayer()
    {
        var songJob = kotlinx.coroutines.experimental.async {
            val songFinder = MusicFinder(contentResolver)
            songFinder.prepare()
            songFinder.allSongs
        }

        launch(kotlinx.coroutines.experimental.android.UI) {
            val songs = songJob.await()

            val playerUI = object:AnkoComponent<MainActivity>
            {
                override fun createView(ui: AnkoContext<MainActivity>)= with(ui) {

                    relativeLayout{
                        backgroundColor = Color.BLACK

                        albumArt = imageView{
                            scaleType = ImageView.ScaleType.FIT_CENTER
                        }.lparams(matchParent, matchParent)

                        verticalLayout{
                            backgroundColor = Color.parseColor("#99000000")
                            songTitle = textView{
                                textColor = Color.WHITE
                                typeface = Typeface.DEFAULT_BOLD
                                textSize = 18f
                            }

                            songArtist = textView{
                                textColor = Color.WHITE
                            }

                            linearLayout{
                                playButton = imageButton{
                                    imageResource = R.drawable.ic_play_circle_outline_black_24dp
                                    onClick {
                                        playOrPause()
                                    }
                                }.lparams(0, wrapContent,0.5f)

                            }.lparams(matchParent, wrapContent){
                                topMargin = dip(5)
                            }



                        }.lparams(matchParent, wrapContent){
                            alignParentBottom()
                        }
                    }

                }

                fun playRandom(){
                    Collections.shuffle(songs)
                    val song = songs[0]
                    mediaPlayer?.reset()
                    mediaPlayer = MediaPlayer.create(ctx,song.uri)
                    mediaPlayer?.setOnCompletionListener {
                        playRandom()
                    }
                    albumArt?.imageURI = song.albumArt
                    songTitle?.text = song.title
                    songArtist?.text = song.artist
                    mediaPlayer?.start()
                    playButton?.imageResource = R.drawable.ic_play_circle_outline_black_24dp
                }

                fun playOrPause(){
                    var songPlaying:Boolean? = mediaPlayer?.isPlaying

                    if(songPlaying == true){
                        mediaPlayer?.pause()
                        playButton?.imageResource = R.drawable.ic_play_circle_outline_black_24dp
                    }
                    else{
                        mediaPlayer?.start()
                        playButton?.imageResource = R.drawable.ic_pause_circle_outline_black_24dp
                    }
                }
            }

            //playerUI.setContentView(this@MainActivity)
            //playerUI.playRandom()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }
}
