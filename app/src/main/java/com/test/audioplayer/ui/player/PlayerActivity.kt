package com.test.audioplayer.ui.player

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.test.audioplayer.R
import com.test.audioplayer.utilities.InjectorUtils


class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
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
            //textViewppp.text
        })
    }
}
