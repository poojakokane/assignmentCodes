
// CPP program to demonstrate multithreading 
// using three different callables. 
#include <iostream> 
#include <thread> 
#include <chrono>
#include <csignal>
using namespace std; 
  
// A dummy function 
void secondsThread(int Z);

void signal_handler(int signal);

void printTimeOutThread(int Z);

void alarmTimerThread(int Z);

void signalHandlerThread();
