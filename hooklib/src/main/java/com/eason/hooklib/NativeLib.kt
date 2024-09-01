package com.eason.hooklib

class NativeLib {

//    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'hooklib' library on application startup.
        init {
            System.loadLibrary("hooklib")
        }

        external fun hookConcurrentGCTask() : Boolean
    }
}