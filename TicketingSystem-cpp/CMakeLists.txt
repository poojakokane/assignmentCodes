cmake_minimum_required(VERSION 3.16)
project(TicketingSystem)

set(CMAKE_CXX_STANDARD 14)

add_executable(TicketingSystem main.cpp TicketQueue.cpp TicketQueue.h Ticket.cpp Ticket.h)