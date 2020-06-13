cmake_minimum_required(VERSION 3.13)
project(Huffman_C C)

set(CMAKE_C_STANDARD 99)

add_executable(Huffman_C huffman.c huffman.h compress.c compress.h dictionary.c dictionary.h frequency.c frequency.h bitcode.c bitcode.h)