//
// Created by Guest1 on 26/06/20.
//
#include "TicketQueue.h"

TicketQueue::TicketQueue(int cap)
{
    harr = new Ticket[cap];
    capacity = cap;
    heap_size = 0;
}


Ticket TicketQueue::getTicketWithHighestPriority()
{
    //complete

}

void TicketQueue::insertTicket(Ticket k)
{
    //complete
}

void TicketQueue::changeTicketPriority(Ticket tkt, int new_priority)
{
    //complete
}

void  TicketQueue::trickleUp(int i)
{
    //complete
}

Ticket TicketQueue::ticketProcessed()
{
    //complete
}

void TicketQueue::deleteTicket(Ticket i)
{
    //complete
}

void TicketQueue::trickleDown(int i)
{
    //complete
}

void TicketQueue::destroyList()
{
    //complete
}

TicketQueue::~TicketQueue()
{
    //complete
}

void swap(Ticket *x, Ticket *y)
{
    //complete
}


