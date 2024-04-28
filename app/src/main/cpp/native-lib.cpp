#include <jni.h>
#include "unistd.h"
#include "sched.h"
#include "android/log.h"
#include <string>

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
}



extern "C"
JNIEXPORT jlong JNICALL
Java_com_eason_stupidoptimise_cpuboost_NativeThread_getCpuMicroTime(JNIEnv *env, jclass clazz,
                                                                    jlong native_peer) {
}


extern "C"
JNIEXPORT jboolean JNICALL
Java_com_eason_stupidoptimise_cpuboost_ThreadCpuAffinityManager_setCpuAffinity(JNIEnv *env,
                                                                               jclass clazz,
                                                                               jint tid,
                                                                               jintArray cpu_set) {
}


extern "C"
JNIEXPORT jboolean JNICALL
Java_com_eason_stupidoptimise_cpuboost_ThreadCpuAffinityManager_resetCpuAffinity(JNIEnv *env,
                                                                                 jclass clazz,
                                                                                 jint tid) {
}


extern "C"
JNIEXPORT jintArray JNICALL
Java_com_eason_stupidoptimise_cpuboost_ThreadCpuAffinityManager_getCpuAffinity(JNIEnv *env,
                                                                               jclass clazz,
                                                                               jint tid) {
}