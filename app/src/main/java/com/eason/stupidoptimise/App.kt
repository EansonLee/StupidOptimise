package com.eason.stupidoptimise

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.bytedance.android.bytehook.ByteHook

class App : MultiDexApplication() {

    private val TAG = "bytehook_tag"
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        System.loadLibrary("stupidoptimise")
        System.loadLibrary("hacker")
        System.loadLibrary("hookee")

        val r = ByteHook.init(
            ByteHook.ConfigBuilder()
                .setMode(ByteHook.Mode.AUTOMATIC) //                .setMode(ByteHook.Mode.MANUAL)
                .setDebug(true)
                .setRecordable(true)
                .build()
        )
        Log.i(TAG, "bytehook init, return: $r")
    }
}