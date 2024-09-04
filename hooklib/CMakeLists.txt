cmake_minimum_required(VERSION 3.22.1)


project("hooklib")

find_package(bytehook REQUIRED CONFIG)
find_package(shadowhook REQUIRED CONFIG)

add_library(${CMAKE_PROJECT_NAME} SHARED
        src/main/cpp/hooklib.cpp)


target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        log
        bytehook::bytehook
        shadowhook::shadowhook)