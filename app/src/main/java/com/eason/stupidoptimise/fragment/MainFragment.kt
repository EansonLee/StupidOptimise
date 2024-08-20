package com.eason.stupidoptimise.fragment

import android.os.*
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.eason.stupidoptimise.cpuboost.NativeThread
import com.eason.stupidoptimise.cpuboost.ThreadCpuAffinityManager
import com.eason.stupidoptimise.databinding.FragmentMainBinding
import com.eason.stupidoptimise.util.ThreadUtil
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instanceRef of this fragment.
 */
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        CpuBoostManager.init(requireContext())
    }

    var cpuPrintStart = false;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        LooperMsgOptimizeManager.getInstance().log("main Fragment onCreateView")
//        return inflater.inflate(R.layout.fragment_main, container, false)
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }


    override fun onResume() {
        super.onResume()

        binding.btnAndroidDebugFix.setOnClickListener{
//            Android14DebuggableBugFixer.fix()
        }

        binding.btnDisableClassVerify.setOnClickListener {
//            KbArt.nDisableClassVerify()
//            KbArt.nDelayJit()
        }


        binding.btnCpuBoost.setOnClickListener { cpuFrequencyBoostTest() }

        binding.btnCpuBindCore.setOnClickListener {
            threadCpuBindTest()
        }

        binding.btnTestGetThreadCpuTime.setOnClickListener {

//            val nativePeer = ArtThread.getNativePeer(Looper.getMainLooper().thread)
//            val frames = Sliver.nativeGetMethodStackTrace(nativePeer);
//            val prettyMethods = Sliver.prettyMethods(frames)
//            Log.e("zxw","methods is "+prettyMethods)

//            val mainThreadCpuMicroTime = ArtThread.getCpuMicroTime(nativePeer)
//            Log.d("zxw","主线程CpuMicroTime "+mainThreadCpuMicroTime)


        }


        binding.btnGetMainThreadAffinity.setOnClickListener {

//            var mainThreadId = ArtThread.getTid(Looper.getMainLooper().thread)
//            Log.e("cpuBind", "主线程tid ${mainThreadId}  pid ${Process.myPid()}")
//            var cpuAffinity = ThreadCpuAffinityManager.getCpuAffinity(mainThreadId)
//            Log.d("cpuBind", "-> 主线程 CPU亲和性相关 -> ${cpuAffinity.joinToString(",")}")
        }

        binding.btnThreadPriorityTest.setOnClickListener {
            threadPriorityTest()
        }

        binding.jdwpTest.setOnClickListener {
//            var nIsJdwpAllow = KbArt.nIsJdwpAllow()
//            Log.e("art", "nIsJdwpAllow ${nIsJdwpAllow}")
//            KbArt.nSetJavaDebuggable(true)
//            KbArt.nSetJdwpAllowed(true)
//             nIsJdwpAllow = KbArt.nIsJdwpAllow()
//            Log.e("art", "nIsJdwpAllow ${nIsJdwpAllow}")
        }
    }

    /**
     * Cpu提频 测试
     */
    private fun cpuFrequencyBoostTest() {
//        thread {
//            Log.w("cpuFreq", "提频前↓")
//            for (i in 0 until 3) {
//                CpuReadUtil.printAllCpuFreq()
//                Thread.sleep(1000)
//            }
//            Log.w("cpuFreq", "开始提频5秒↓")
//            CpuBoostManager.boostCpu(5_000, "test")
//            Log.w("cpuFreq", "提频后↓")
//            for (i in 0 until 5) {
//                CpuReadUtil.printAllCpuFreq()
//                Thread.sleep(900)
//            }
//            Log.w("cpuFreq", "提频时间结束后↓")
//            for (i in 0 until 3) {
//                CpuReadUtil.printAllCpuFreq()
//                Thread.sleep(1000)
//            }
//        }
    }

    /**
     * 线程优先级测试
     *
     */
    private fun threadPriorityTest() {
//        Thread{
//            var currentThread = Thread.currentThread()
//            var tid = ArtThread.getTid(currentThread)
//            Log.e("priorityTest","当前线程 $tid" +
//                    " java优先级 ${currentThread.priority} nice值 ${ThreadUtil.getNice(tid)}")
//            currentThread.priority=Thread.MAX_PRIORITY;
//            Log.e("priorityTest","使用 Thread.setPriority 设置最高优级10 后  nice值 ${ThreadUtil.getNice(tid)}")
//            Process.setThreadPriority(tid,-20)
//            Log.e("priorityTest","使用 Process.setThreadPriority 设置最高优级-20 后  nice值 ${ThreadUtil.getNice(tid)}")
//        }.start()

        startTask("task1", false);
        startTask("task2", false);
        startTask("task3", false);
        startTask("task4", true);

    }

    private fun startTask(taskName: String, upgradePriority: Boolean) {
        val thread = Thread {
            //将工作线程绑定在同一个CPU上
            val tid = NativeThread.getTid(Thread.currentThread())
            Log.e(
                "threadPriority", "线程${taskName} 刚开始运行在 ${ThreadUtil.getLastRunOnCpu(tid)}," +
                        "亲和性 ${ThreadCpuAffinityManager.getCpuAffinity(tid).joinToString(",")}"
            )
            ThreadCpuAffinityManager.setCpuAffinityToThread(Thread.currentThread(), intArrayOf(7))
            if (upgradePriority) {
                Thread.currentThread().priority = Thread.MAX_PRIORITY
//                Process.setThreadPriority(tid,-20)
            }
            val beginCpuTime = SystemClock.currentThreadTimeMillis()
            val beginTime = SystemClock.elapsedRealtime()
            var timeOut = false;
            while (!timeOut) {
                if (SystemClock.elapsedRealtime() - beginTime > 5000) {
                    timeOut = true
                }
            }

            val endCpuTime = SystemClock.currentThreadTimeMillis()
            val endTime = SystemClock.elapsedRealtime()

            Log.e(
                "threadPriority", "线程${taskName} 经过 ${endTime - beginTime}秒" +
                        " 实际获得执行的cpu时间 ${endCpuTime - beginCpuTime} ,最后一次运行在CPU ${ThreadUtil.getLastRunOnCpu(tid)}"
            )
        }
        thread.name = taskName
        thread.start()
    }

    companion object {
        /**
         * Use this factory method to create a new instanceRef of
         * this fragment using the provided parameters.
         * @return A new instanceRef of fragment MainFragment.
         */
        @JvmStatic fun newInstance() =
            MainFragment()
    }

    fun threadCpuBindTest() {
//        val newThread = Thread {
//            val begin = SystemClock.elapsedRealtime()
//            Thread.sleep(1000L)
//            while ((SystemClock.elapsedRealtime() - begin) < 30_000) {
//                val a = 10000.0
//                val b = 20000.0
//                val c = a * b
//                val d = a / b
//                val e = a + b
//                val f = a - b
//            }
//            Log.e("cpuBind", "工作线程${Thread.currentThread().id} 结束")
//        }
//        newThread.start()

//        thread {
            val targetThread = Looper.getMainLooper().thread
            var targetTid = NativeThread.getTid(targetThread)
            val TAG = "cpuBind"
            Log.e(TAG, "目标线程tid" + targetTid)
            var cpuAffinity = ThreadCpuAffinityManager.getCpuAffinity(targetTid)
            val cpus = binding.bindCpu.text.split(" ").map { it.toInt() }.toIntArray()
            Log.d(TAG, "-> 目标线程 CPU亲和性相关 -> ${cpuAffinity.joinToString(",")}")

            val commandExecutor = Runtime.getRuntime().exec("taskset -p 0x3 ${Process.myPid()}")
            val exitValue = commandExecutor.waitFor()

            Log.e(TAG, "命令执行结果 ${exitValue}")
            for (i in 0 until 5) {
                Thread.sleep(200)
                Log.d(TAG, "目标线程 目前运行在CPU ${ThreadUtil.getLastRunOnCpu(targetTid)}")
            }
            var isSuccess = ThreadCpuAffinityManager.setCpuAffinityToThread(targetThread, cpus)
            Log.d(
                TAG,
                "Cpu亲和性尝试修改为 ${cpus.joinToString(" ")}, 结果 $isSuccess ,读取最新affinity结果${
                    ThreadCpuAffinityManager.getCpuAffinity(targetTid)
                        .joinToString(" ")
                }"
            )
            Thread.sleep(50)
            for (i in 0 until 20) {
                Thread.sleep(200)
                Log.d(TAG, "目标线程 目前运行在CPU ${ThreadUtil.getLastRunOnCpu(targetTid)}")
            }
            isSuccess = ThreadCpuAffinityManager.resetCpuAffinity(targetThread)
            Log.d("cpuBind", "重置CPU亲和性 " + isSuccess)
            Thread.sleep(50)
            cpuAffinity = ThreadCpuAffinityManager.getCpuAffinity(targetTid)
            Log.d("cpuBind", "-> 目标线程 CPU亲和性相关 -> ${cpuAffinity.joinToString(",")}")
//        }

    }
}