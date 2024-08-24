cmake_minimum_required(VERSION 3.22.1)

project("stupidoptimise")

set(CMAKE_CXX_STANDARD 17)

find_package(xdl REQUIRED CONFIG)
find_package(bytehook REQUIRED CONFIG)

add_library(${CMAKE_PROJECT_NAME} SHARED
        art_def.h
        native-lib.cpp
        art_thread.h
        common.h
        tls.h
        cpu_info.h
        cpu_info.cpp
        art_thread.cpp
        hacker.cpp
        hookee.cpp)


target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        log
        xdl::xdl
        bytehook::bytehook)