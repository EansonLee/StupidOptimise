//
// Created by Knight-ZXW on 2023/6/1.
//
//#include <xdl.h>
//#include "art_xdl.h"
#include "art_thread.h"

//uint32_t art::Thread::GetThreadId() {
//  static uint32_t offset = 0;
//  if (offset == 0) {
//    int api_level = android_get_device_api_level();
//    if (api_level >= __ANDROID_API_S__) { // >=Android 12
//      offset = offsetof(tls_32bit_sized_values, thin_lock_thread_id);
//    } else if (api_level <= __ANDROID_API_Q__) { // <=Android 10
//      offset = offsetof(tls_32bit_sized_values_android10, thin_lock_thread_id);
//    }
//  }
//  return *(uint32_t *) ((char *) this + offset);
//}

uint32_t art::Thread::GetTid() {
    static uint32_t offset = -1;
    if (offset == -1) {
        int api_level = android_get_device_api_level();
        if (api_level >= __ANDROID_API_R__) { // >=Android 12
            offset = offsetof(tls_32bit_sized_values_12_14, tid);
        } else if (api_level >= __ANDROID_API_L__) { // <=Android 10
            offset = offsetof(tls_32bit_sized_values_android_5_11, tid);
        }
    }
    return *(uint32_t *) ((char *) this + offset);
}

//art::Thread *art::Thread::Current() {
//  void *thread = __get_tls()[TLS_SLOT_ART_THREAD_SELF];
//  return reinterpret_cast<Thread *>(thread);
//}
//
//uint64_t art::Thread::GetCpuMicroTime() {
//  static GetCpuMicroTime_t get_cpu_micro_time = nullptr;
//  if (UNLIKELY( get_cpu_micro_time == nullptr)) {
//    get_cpu_micro_time =
//        reinterpret_cast<GetCpuMicroTime_t>(xdl_dsym(get_art_handle(),
//                                                     "_ZNK3art6Thread15GetCpuMicroTimeEv",
//                                                     nullptr));
//  }
//  return get_cpu_micro_time(this);
//}
