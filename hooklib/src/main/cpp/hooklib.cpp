#include <jni.h>
#include "bytehook.h"

#include <android/log.h>
#include <dlfcn.h>
#include <fcntl.h>
#include <inttypes.h>
#include <jni.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#include <unistd.h>


#define HACKER_TAG "bytehook_tag"

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wgnu-zero-variadic-macro-arguments"
#define LOG(fmt, ...) __android_log_print(ANDROID_LOG_INFO, HACKER_TAG, fmt, ##__VA_ARGS__)
#pragma clang diagnostic pop

typedef int (*concurrent_t)(const char *, int, mode_t);


#define CONCURRENT_DEF(fn)                                                                                   \
  static fn##_t fn##_prev = NULL;                                                                            \
  static bytehook_stub_t fn##_stub = NULL;                                                                   \
  static void fn##_hooked_callback(bytehook_stub_t task_stub, int status_code, const char *caller_path_name, \
                                   const char *sym_name, void *new_func, void *prev_func, void *arg) {       \
    if (BYTEHOOK_STATUS_CODE_ORIG_ADDR == status_code) {                                                     \
      fn##_prev = (fn##_t)prev_func;                                                                         \
      LOG(">>>>> save original address: %" PRIxPTR, (uintptr_t)prev_func);                                   \
    } else {                                                                                                 \
      LOG(">>>>> hooked. stub: %" PRIxPTR                                                                    \
          ", status: %d, caller_path_name: %s, sym_name: %s, new_func: %" PRIxPTR ", prev_func: %" PRIxPTR   \
          ", arg: %" PRIxPTR,                                                                                \
          (uintptr_t)task_stub, status_code, caller_path_name, sym_name, (uintptr_t)new_func,                \
          (uintptr_t)prev_func, (uintptr_t)arg);                                                             \
    }                                                                                                        \
  }

CONCURRENT_DEF(concurrent)


static int concurrent_proxy_gc(const char *pathname, int flags, mode_t modes) {
    LOG("----------------hook task start to sleep------------------");

    sleep(3);

    int fd = BYTEHOOK_CALL_PREV(concurrent_proxy_gc, pathname, flags, modes);

    LOG("----------------hook task end ------------------ ");
    BYTEHOOK_POP_STACK();
    return fd;
}


extern "C"
JNIEXPORT jboolean JNICALL
Java_com_eason_hooklib_NativeLib_00024Companion_hookConcurrentGCTask(JNIEnv *env, jobject thiz) {

    void *concurrent_proxy = (void *) concurrent_proxy_gc;
    concurrent_stub = bytehook_hook_single("libart.so", NULL, "Run", concurrent_proxy ,
                                           concurrent_hooked_callback, NULL);

//    concurrent_stub = bytehook_hook_all(NULL, "Run", concurrent_proxy ,
//                                           concurrent_hooked_callback, NULL);

    if (concurrent_stub == nullptr) {
        LOG("----------------hook failed------------------ ");
        return JNI_FALSE;
    } else{
        LOG("----------------hook success------------------ ");
        return JNI_TRUE;
    }
}