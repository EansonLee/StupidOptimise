package com.eason.hooklib.manager

import android.util.Log
import com.bytedance.android.bytehook.ByteHook

object GcManager {

    @JvmStatic
    @Synchronized
    fun init() {
        val res = ByteHook.init(
            ByteHook.ConfigBuilder()
                .setMode(ByteHook.Mode.AUTOMATIC)
                .setDebug(true)
                .setRecordable(true)
                .build()
        )
        Log.e("hook", "init $res")
    }
}