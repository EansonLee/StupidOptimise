package com.eason.stupidoptimise.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleCoroutineScope;

import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eason.stupidoptimise.cpuboost.NativeThread;
import com.eason.stupidoptimise.cpuboost.ThreadCpuAffinityManager;
import com.eason.stupidoptimise.databinding.ActivityMainBinding;

import java.util.Arrays;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("stupidoptimise");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.tvGetTid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tid = NativeThread.getTid(Looper.getMainLooper().getThread());
                Log.e("cpuBind", "主线程tid：" + tid + "，pid：" + Process.myPid());
                int[] affinity = ThreadCpuAffinityManager.getCpuAffinity(tid);
                Log.e("cpuBind", "主线程亲和性：" + tid + "，affinity：" + Arrays.toString(affinity));

                int[] newAffinity = new int[]{2};
                ThreadCpuAffinityManager.setCpuAffinityToThread(Looper.getMainLooper().getThread(), newAffinity);

            }
        });


        binding.tvGetAffinity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tid = NativeThread.getTid(Looper.getMainLooper().getThread());
                Log.e("cpuBind", "主线程tid：" + tid + "，pid：" + Process.myPid());
                int[] affinity = ThreadCpuAffinityManager.getCpuAffinity(tid);
                Log.e("cpuBind", "主线程亲和性：" + tid + "，affinity：" + Arrays.toString(affinity));
            }
        });

    }


    public native String stringFromJNI();

    public native int sasd();
}