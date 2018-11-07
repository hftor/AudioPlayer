package com.test.audioplayer.data

/**
 * Created by hafthorg on 07/11/2018.
 */
data class Player(val text: String) {
    override fun toString(): String {
        return text + " constructed"
    }
}