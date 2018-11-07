package com.test.audioplayer.data

/**
 * Created by hafthorg on 07/11/2018.
 */
class FakeDatabase private constructor(){

    var playerDao =  PlayerDAO()
        private set

    companion object {
        @Volatile private var instance : FakeDatabase? = null

        fun getFakeInstance() =
            instance ?: synchronized(this){
                instance ?: FakeDatabase().also {
                    instance = it
                }
            }
    }
}