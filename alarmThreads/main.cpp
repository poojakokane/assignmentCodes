
// CPP program to demonstrate multithreading 
// using three different callables. 
#include <main.h>
using namespace std; 
  
// A dummy function 
void secondsThread(int Z) 
{ 
	/*
    for (int i = 0; i < Z; i++) { 
        cout << i << ":Thread using function"
               " pointer as callable\n"; 
    } 
	*/
	std::this_thread::sleep_for(std::chrono::seconds(Z+1));
	cout << "Exiting as timeout of " << Z <<"s over...\n";

	std::raise(SIGTERM);
} 

void signal_handler(int signal){
	if(signal == SIGTERM){
		//cout << "Caught signal" << endl;
		raise(SIGINT);
	}
	if(signal == SIGINT){
		exit(0);
	}
}

void printTimeOutThread(int Z) {
	std::signal(SIGTERM, signal_handler);

	while(1){
		std::this_thread::sleep_for(std::chrono::seconds(Z));
		auto timenow = chrono::system_clock::to_time_t(chrono::system_clock::now()); 
    	cout << "Current time is: " << ctime(&timenow); 
	}
}

void alarmTimerThread(int Z) {
	std::this_thread::sleep_for(std::chrono::seconds(Z));
	cout << "Alarm went off!!! after " << Z << "s\n";		
}

void signalHandlerThread() {
	std::signal(SIGTERM, signal_handler);
}

int main(int argc, char** argv) 
{ 
	int seconds = 25;
	int printTimeOut = 1;
	int alarmTimer = 17;

	if (argc >= 2){
		seconds = atoi(argv[1]);
	}
	if (argc >= 3){
		printTimeOut = atoi(argv[2]);		
	}
	if (argc >= 4) {
		alarmTimer = atoi(argv[3]);
	}

	//cout << seconds << " " << printTimeOut << " " << alarmTimer << endl;

    // This thread is launched by using  
    // function pointer as callable 
    thread th1(secondsThread, seconds); 
    thread th2(printTimeOutThread, printTimeOut); 
    thread th3(alarmTimerThread, alarmTimer); 
	thread th4(signalHandlerThread);
  
    // Wait for the threads to finish 
    // Wait for thread t1 to finish 
    th1.join(); 
    th2.join(); 
    th3.join(); 
	th4.join();
  
    return 0; 
} 
