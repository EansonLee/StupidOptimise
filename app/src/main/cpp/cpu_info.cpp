//
// Created by xmiles on 2024/8/19.
//
#include <string>
#include <fstream>
#include <dirent.h>
#include <stdexcept>
#include "cpu_info.h"
#include "android/log.h"

int getMaxFreqCPU() {
    int maxFreq = -1;
    int maxFreqCore = -1; // 用于存储最大频率对应的 CPU 核心编号
    for (int i = 0; i < getNumberOfCPUCores(); i++) {
        std::string filename = "/sys/devices/system/cpu/cpu" +
                               std::to_string(i) + "/cpufreq/cpuinfo_max_freq";
        std::ifstream cpuInfoMaxFreqFile(filename);
        if (cpuInfoMaxFreqFile.is_open()) {
            std::string line;
            if (std::getline(cpuInfoMaxFreqFile, line)) {
                try {
                    int freqBound = std::stoi(line);
                    if (freqBound > maxFreq) {
                        maxFreq = freqBound;
                        maxFreqCore = i; // 更新最大频率对应的 CPU 核心编号
                    }
                } catch (const std::invalid_argument& e) {
                    __android_log_print(ANDROID_LOG_ERROR,
                                        "TCpuAffinity",
                                        "getMaxFreqCPU failed");
                }
            }
            cpuInfoMaxFreqFile.close();
        }
    }
    return maxFreqCore;
}

int getNumberOfCPUCores() {
    int cores = 0;
    DIR *dir;
    struct dirent *ent;
    if ((dir = opendir("/sys/devices/system/cpu/")) != NULL) {
        while ((ent = readdir(dir)) != NULL) {
            std::string path = ent->d_name;
            if (path.find("cpu") == 0) {
                bool isCore = true;
                for (int i = 3; i < path.length(); i++) {
                    if (path[i] < '0' || path[i] > '9') {
                        isCore = false;
                        break;
                    }
                }
                if (isCore) {
                    cores++;
                }
            }
        }
        closedir(dir);
    }
    return cores;
}
