//
// Created by Guest1 on 26/06/20.
//

#ifndef TICKETINGSYSTEM_TICKETQUEUE_H
#define TICKETINGSYSTEM_TICKETQUEUE_H
#pragma once

#include<iostream>
#include<climits>

#include "Ticket.h"

using namespace std;

void swap(Ticket *x, Ticket *y);

class TicketQueue
{
private:
    Ticket *harr;
    int capacity;
    int heap_size;

public:
    TicketQueue(int capacity);
    void trickleDown(int );
    int returnTicketQueueSize(){return capacity;}
    int parent(int i) { return (i-1)/2; }
    int leftChild(int i) { return (2*i + 1); }
    int rightChild(int i) { return (2*i + 2); }
    int getNumberOfTicketsInTicketQueue() {return heap_size;}
    Ticket ticketProcessed();
    void changeTicketPriority(Ticket i, int new_priority);
    Ticket getTicketWithHighestPriority();
    void deleteTicket(Ticket i);
    void insertTicket(Ticket k);
    void trickleUp(int i);
    void destroyList();
    ~TicketQueue();
};


#endif //TICKETINGSYSTEM_TICKETQUEUE_H
