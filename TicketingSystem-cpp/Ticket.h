//
// Created by Guest1 on 26/06/20.
//

#ifndef TICKETINGSYSTEM_TICKET_H
#define TICKETINGSYSTEM_TICKET_H
#pragma once
#include <string>

class Ticket{

private:

    int priority;
    std::string clientType ;

public:

    static int ticketNumberAdmin, ticketNumberStudent, ticketNumberFaculty;
    int _ticketNumberAdmin, _ticketNumberStudent, _ticketNumberFaculty;
    Ticket();
    Ticket(int _client_type);

    int getPriority();
    void setPriority(int);
    std::string getClientType();

};

#endif //TICKETINGSYSTEM_TICKET_H
