//
// Created by Guest1 on 26/06/20.
//
#include "Ticket.h"

using namespace std;

int PRIO_0 = 0; // max priority
int PRIO_1 = 100;
int PRIO_E = 200;

int Ticket::ticketNumberAdmin = 0;
int Ticket::ticketNumberStudent = 0;
int Ticket::ticketNumberFaculty = 0;

Ticket::Ticket(){}

Ticket::Ticket(int _client_type) {

    if (_client_type == 0) {
        clientType = "Admin";
        _ticketNumberAdmin = ticketNumberAdmin;
        priority = PRIO_0 + _ticketNumberAdmin;
        ticketNumberAdmin++;
    } else if (_client_type == 1) {
        clientType = "Faculty";
        _ticketNumberFaculty = ticketNumberFaculty;
        priority = PRIO_1 + _ticketNumberFaculty;
        ticketNumberFaculty++;

    } else { // student
        clientType = "Student";
        _ticketNumberStudent = ticketNumberStudent;
        priority = PRIO_E + _ticketNumberStudent;
        ticketNumberStudent++;
    }
}

int Ticket::getPriority(){
    //complete
    return this->priority;
}

void Ticket::setPriority(int _priority){
    //complete
    this->priority = _priority;
}

std::string Ticket::getClientType() {
    //complete
    return this->clientType;
}

void Ticket::setClientType(std::string clientType) {
    this->clientType = clientType;
}

