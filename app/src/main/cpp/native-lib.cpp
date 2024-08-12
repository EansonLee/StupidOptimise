#include <jni.h>
#include "unistd.h"
#include "sched.h"
#include "android/log.h"
#include "art_thread.h"
#include <string>


static jfieldID nativePeerField = NULL;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_eason_stupidoptimise_activity_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_eason_stupidoptimise_cpuboost_NativeThread_getTid(JNIEnv *env, jclass clazz,
                                                           jobject thread) {
    if (nativePeerField == nullptr) {
        jclass threadClass = env->GetObjectClass(thread);
        nativePeerField = env->GetFieldID(threadClass, "nativePeer", "J");
    }
    jlong nativePeerValue = env->GetLongField(thread, nativePeerField);
    if (nativePeerValue == 0){ //native thread has not yet been created/started, or has been destroyed.
        return -1;
    }
    auto *artThread = reinterpret_cast<art::Thread *>(nativePeerValue);
    uint32_t tid = artThread->GetTid();
    //double check, 保证上面计算tid时，线程还未被销毁，避免计算出一个异常的tid
    nativePeerValue = env->GetLongField(thread, nativePeerField);
    if (nativePeerValue == 0){ //native thread has not yet been created/started, or has been destroyed.
        return -1;
    }
    return tid;
}



//extern "C"
//JNIEXPORT jlong JNICALL
//Java_com_eason_stupidoptimise_cpuboost_NativeThread_getCpuMicroTime(JNIEnv *env, jclass clazz, jlong native_peer) {
//
//}


extern "C"
JNIEXPORT jboolean JNICALL
Java_com_eason_stupidoptimise_cpuboost_ThreadCpuAffinityManager_setCpuAffinity(JNIEnv *env,
                                                                               jclass clazz,
                                                                               jint tid,
                                                                               jintArray cpu_set) {
    if (tid <= 0) {
        tid = gettid();
    }
    // 获取当前CPU核心数
    int cpu_count = sysconf(_SC_NPROCESSORS_CONF);
    jsize size = env->GetArrayLength(cpu_set);
    jint bind_cpus[size];
    env->GetIntArrayRegion(cpu_set, 0, size, bind_cpus);

    cpu_set_t mask;
    CPU_ZERO(&mask);
    for (jint cpu : bind_cpus) {
        if (cpu > 0 && cpu < cpu_count) {
            CPU_SET(cpu, &mask); //设置对应cpu位置的值为1
        } else {
            __android_log_print(ANDROID_LOG_ERROR,
                                "TCpuAffinity",
                                "try bind illegal cpu index %d",cpu);
        }
    }

    int code = sched_setaffinity(tid, sizeof(mask), &mask);
    if (code == 0) {
        // return success
        return JNI_TRUE;
    } else {
        __android_log_print(ANDROID_LOG_ERROR,
                            "TCpuAffinity",
                            "setCpuAffinity() failed code %d",code);
        // return failed
        return JNI_FALSE;
    }
}


extern "C"
JNIEXPORT jboolean JNICALL
Java_com_eason_stupidoptimise_cpuboost_ThreadCpuAffinityManager_resetCpuAffinity(JNIEnv *env,
                                                                                 jclass clazz,
                                                                                 jint tid) {
    if (tid < 0) {
        tid = gettid();
    }

    int cpu_count = sysconf(_SC_NPROCESSORS_ONLN);
    cpu_set_t cpuSet;
    CPU_ZERO(&cpuSet);
//  for (int cpuIndex = 0; cpuIndex < cpu_count; ++cpuIndex) {
//    CPU_CLR(cpuIndex, &cpuSet); // CPU_CLR 无法清除，因此目前的方式是 尝试绑定所有线程
//  }

    for (int cpuIndex = 0; cpuIndex < cpu_count; ++cpuIndex) {
        CPU_SET(cpuIndex, &cpuSet);
    }


    int code = sched_setaffinity(tid, sizeof(cpuSet), &cpuSet);
    if (code == 0) {
        // log reset thread affinity failed
        return JNI_TRUE;
    } else {
        __android_log_print(ANDROID_LOG_ERROR, "TCpuAffinity", "resetCpuAffinity() failed code %d",code);
        return JNI_FALSE;
    }
}


extern "C"
JNIEXPORT jintArray JNICALL
Java_com_eason_stupidoptimise_cpuboost_ThreadCpuAffinityManager_getCpuAffinity(JNIEnv *env,
                                                                               jclass clazz,
                                                                               jint tid) {
    cpu_set_t cpuSet;
    CPU_ZERO(&cpuSet);
    if (sched_getaffinity(tid, sizeof(cpuSet), &cpuSet) != 0) {
        __android_log_print(ANDROID_LOG_ERROR, "TCpuAffinity", "sched_getaffinity() failed");
        return env->NewIntArray(0);
    }

    jint cpuArr[CPU_SETSIZE];
    int index = 0;
    for (int i = 0; i < CPU_SETSIZE; i++) {
        if (CPU_ISSET(i, &cpuSet)) {
            cpuArr[index++] = i;
        }
    }

    jintArray result = env->NewIntArray(index);
    env->SetIntArrayRegion(result, 0, index, cpuArr);
    return result;
}