cmake_minimum_required(VERSION 3.10.2)


project("hooklib")

find_package(bytehook REQUIRED CONFIG)


add_library(${CMAKE_PROJECT_NAME} SHARED
        src/main/cpp/hooklib.cpp)


target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        log
        bytehook::bytehook)