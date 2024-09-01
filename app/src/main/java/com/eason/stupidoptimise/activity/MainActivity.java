package com.eason.stupidoptimise.activity;

import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.eason.hooklib.NativeLib;
import com.eason.stupidoptimise.cpuboost.CpuBoostUtil;
import com.eason.stupidoptimise.cpuboost.NativeThread;
import com.eason.stupidoptimise.cpuboost.ThreadCpuAffinityManager;
import com.eason.stupidoptimise.databinding.ActivityMainBinding;
import com.eason.stupidoptimise.util.ThreadUtil;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.tvGetTid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int tid = NativeThread.getTid(Looper.getMainLooper().getThread());
//                Log.e("cpuBind", "主线程tid：" + tid + "，pid：" + Process.myPid());
//                int[] affinity = ThreadCpuAffinityManager.getCpuAffinity(tid);
//                int core = ThreadUtil.INSTANCE.getLastRunOnCpu(tid);
//
//                Log.e("cpuBind", "主线程亲和性：" + tid + "，affinity：" + Arrays.toString(affinity) + "，运行在：" + core);
//
//                int[] newAffinity = new int[]{6};
//                ThreadCpuAffinityManager.setCpuAffinityToThread(Looper.getMainLooper().getThread(), newAffinity);
                bindMainAffinityTest();
            }
        });


        binding.tvGetAffinity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tid = NativeThread.getTid(Looper.getMainLooper().getThread());
                Log.e("cpuBind", "主线程tid：" + tid + "，pid：" + Process.myPid());
                int[] affinity = ThreadCpuAffinityManager.getCpuAffinity(tid);
                Log.e("cpuBind", "主线程亲和性：" + tid + "，affinity：" + Arrays.toString(affinity));
                int core = ThreadUtil.INSTANCE.getLastRunOnCpu(tid);
                Log.e("cpuBind", "主线程亲和性：" + tid + "，affinity：" + Arrays.toString(affinity) + "，运行在：" + core);

            }
        });

        binding.tvSetBigAffinity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int tid = NativeThread.getTid(Looper.getMainLooper().getThread());
//                Log.e("cpuBind", "主线程tid：" + tid + "，pid：" + Process.myPid());
//                boolean result = ThreadCpuAffinityManager.setCpuAffinityToBigAndPlusCore(tid);
//                Log.e("cpuBind", "绑定大核：" + result);
                CpuBoostUtil.boostCpuBindBigCore();
            }
        });

        binding.tvHookLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean res = NativeLib.Companion.hookConcurrentGCTask();
                Log.e("hook-java", "result：" + res);
            }
        });

        binding.tvGc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allocateMemory();
                System.gc();
            }
        });
    }


    private void bindMainAffinityTest() {
        int tid = NativeThread.getTid(Looper.getMainLooper().getThread());
        Log.e("cpuBind", "主线程tid：" + tid);
        int core = ThreadUtil.INSTANCE.getLastRunOnCpu(tid);
        int[] affinity = ThreadCpuAffinityManager.getCpuAffinity(tid);
        Log.e("cpuBind", "主线程亲和性：" + tid + "，affinity：" + Arrays.toString(affinity) + "，运行在：" + core);
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(200);
                core = ThreadUtil.INSTANCE.getLastRunOnCpu(tid);
                Log.e("cpuBind", "运行在：" + core);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        testLocal();

        int[] newAffinity = new int[]{6, 7};
        boolean res = ThreadCpuAffinityManager.setCpuAffinityToThread(Looper.getMainLooper().getThread(), newAffinity);
        Log.e("cpuBind", "尝试修改亲和性为：" + Arrays.toString(newAffinity) + "，修改结果：" + res);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(200);
                core = ThreadUtil.INSTANCE.getLastRunOnCpu(tid);
                Log.e("cpuBind", "运行在CPU：" + core);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        testLocal();
    }

    private void testLocal() {
        long beginTime = SystemClock.elapsedRealtimeNanos();
        long cpuBeginTime = SystemClock.currentThreadTimeMillis();
        int res = 1;
        for (int i = 0; i < 99999999; i++) {
            res += i;
        }
        long iEndTime = SystemClock.elapsedRealtimeNanos();
        long iCpuEndTime = SystemClock.currentThreadTimeMillis();
        Log.e("cpuBind", "cost wallTime：" + ((iEndTime - beginTime) / 1000) + "，cpu Time：" + (iCpuEndTime - cpuBeginTime));
    }


    public void allocateMemory() {

        int SIZE = 1024 * 1024 * 50; // 50 MB

        try {
            // 创建一个大数组以占用内存
            byte[] memoryHog = new byte[SIZE];

            // Optional: 对数组进行操作，防止被编译器优化
            for (int i = 0; i < memoryHog.length; i++) {
                memoryHog[i] = (byte) i;
            }

            // 保留引用，防止垃圾收集器过早回收
            System.out.println("Allocated 50 MB of memory.");
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory!");
        }

    }

    public native String stringFromJNI();

}