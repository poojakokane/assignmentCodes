cmake_minimum_required(VERSION 3.13)
project(Shapes_cpp)

set(CMAKE_CXX_STANDARD 14)

add_executable(Shapes_cpp main.cpp Point.cpp Point.hpp Circle.cpp Circle.hpp Cylinder.cpp Cylinder.hpp)