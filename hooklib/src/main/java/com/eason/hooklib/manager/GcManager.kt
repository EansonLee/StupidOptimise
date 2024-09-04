package com.eason.hooklib.manager

import android.util.Log
import com.bytedance.shadowhook.ShadowHook


object GcManager {

    @JvmStatic
    @Synchronized
    fun init() {
        // plthook
//        val res = ByteHook.init(
//            ByteHook.ConfigBuilder()
//                .setMode(ByteHook.Mode.AUTOMATIC)
//                .setDebug(true)
//                .setRecordable(true)
//                .build()
//        )

    // shadowhook
       val res =  ShadowHook.init(
            ShadowHook.ConfigBuilder()
                .setMode(ShadowHook.Mode.UNIQUE)
                .build()
        )
        Log.e("hook", "init $res")
    }
}