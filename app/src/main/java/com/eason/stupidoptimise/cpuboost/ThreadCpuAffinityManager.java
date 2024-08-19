package com.eason.stupidoptimise.cpuboost;


public class ThreadCpuAffinityManager {

//    static {
//        System.loadLibrary("stupidoptimise");
//    }

    public static native boolean setCpuAffinity(int tid,int[] cpuSet);

    public static native boolean resetCpuAffinity(int tid);

    public static native int[] getCpuAffinity(int tid);

    public static native boolean setCpuAffinityToBigAndPlusCore(int tid);

    public static boolean setCpuAffinityToThread(Thread thread,int[] cpuSet){
        int tid = NativeThread.getTid(thread);
        return setCpuAffinity(tid, cpuSet);
    }

    public  static boolean resetCpuAffinity(Thread thread){
        int tid = NativeThread.getTid(thread);
        return resetCpuAffinity(tid);
    }


    public static boolean setCpuAffinityToBigAndPlusCore(Thread thread){
        int tid = NativeThread.getTid(thread);
        return  true;
    }

    public static boolean resetBind(Thread thread){
        int tid = NativeThread.getTid(thread);
        return resetCpuAffinity(tid);
    }
}
