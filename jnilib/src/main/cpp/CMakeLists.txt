cmake_minimum_required(VERSION 3.4.1)


add_library(
        yuv-lib
        SHARED
        yuv-lib.cpp)

find_library(
        log-lib
        log)
target_link_libraries(
        yuv-lib
        ${log-lib})