package com.eason.stupidoptimise.cpuboost

import android.os.Looper
import android.os.Process
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object CpuBoostUtil {

    private var MAX_RETRY_COUNT = 3

    private var DELAY_MILLS = 30_000L

    @JvmStatic
    fun setRetryCount(count: Int) {
        MAX_RETRY_COUNT = count
    }


    @JvmStatic
    fun setDelayMills(delayMills: Long) {
        DELAY_MILLS = delayMills
    }

    @JvmStatic
    fun boostCpuBindBigCore() {
        CoroutineScope(Dispatchers.Main).launch {
            retryWithDelay { bindCore() }
        }
    }

    private suspend fun retryWithDelay(block: suspend () -> Boolean) {
        var currentRetry = 0
        while (currentRetry <= MAX_RETRY_COUNT) {
            if (block()) {
                return // 如果执行成功，直接返回
            }
            currentRetry++
            delay(DELAY_MILLS)
        }
    }

    // 使用示例
    private suspend fun bindCore(): Boolean {
        // 你的方法实现
        val tid = NativeThread.getTid(Looper.getMainLooper().thread)
        Log.e("cpuBind", "主线程tid：" + tid + "，pid：" + Process.myPid())
        val result = ThreadCpuAffinityManager.setCpuAffinityToBigAndPlusCore(tid)
        Log.e("cpuBind", "绑定大核：$result")
        return result
    }
}