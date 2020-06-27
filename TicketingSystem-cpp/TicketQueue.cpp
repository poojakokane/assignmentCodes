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
    return harr[heap_size-1];
}

void TicketQueue::insertTicket(Ticket k)
{
    //complete
    harr[heap_size] = k;
    trickleUp(heap_size);
    heap_size++;
}

void TicketQueue::changeTicketPriority(Ticket tkt, int new_priority)
{
    //complete
    int i;
    int found = 0;
    for(i=0; i<heap_size; i++){
        if(harr[i].getPriority() == tkt.getPriority() && harr[i].getClientType() == tkt.getClientType()){
            found = 1;
            break;
        }
    }

    if(!found){
        return;
    }

    harr[i].setPriority(new_priority);
    int pid = (i-1)/2;

    if(i == 0){
        trickleDown(i);
    } else{
        if(harr[i].getPriority() < harr[pid].getPriority()){
            trickleUp(i);
        } else{
            trickleDown(i);
        }
    }
}

void  TicketQueue::trickleUp(int i)
{
    //complete
    while(i>0 && harr[i].getPriority() < harr[(i-1)/2].getPriority()){
        swap(&harr[i], &harr[(i-1)/2]);
        i = (i-1)/2;
    }
}

Ticket TicketQueue::ticketProcessed()
{
    //complete
    swap(&harr[0], &harr[heap_size-1]);
    heap_size--;
    trickleDown(0);
    return harr[heap_size];
}

void TicketQueue::deleteTicket(Ticket i)
{
    //complete
    i.setPriority(INT32_MIN);
    ticketProcessed();
}

void TicketQueue::trickleDown(int i)
{
    //complete
    int lch = (i * 2) + 1;
    int rch = (i * 2) + 2;

    while(i < heap_size
    && (lch < heap_size && (harr[i].getPriority() > harr[lch].getPriority())
    || (rch < heap_size && harr[i].getPriority() > harr[rch].getPriority()))){
        if(rch < heap_size) {
            if (harr[lch].getPriority() < harr[rch].getPriority()) {
                swap(&harr[i], &harr[lch]);
                i = lch;
            } else {
                swap(&harr[i], &harr[rch]);
                i = rch;
            }
        }
        else{
            swap(&harr[i], &harr[lch]);
            i = lch;
        }

        lch = (i * 2) + 1;
        rch = (i * 2) + 2;
    }
}

void TicketQueue::destroyList()
{
    //complete
}

TicketQueue::~TicketQueue()
{
    //complete
    delete[] harr;
    capacity = 0;
    heap_size = 0;
}

void swap(Ticket *x, Ticket *y)
{
    //complete
    Ticket temp;
    temp.setClientType(x->getClientType());
    temp.setPriority(x->getPriority());

    x->setClientType(y->getClientType());
    x->setPriority(y->getPriority());

    y->setClientType(temp.getClientType());
    y->setPriority(temp.getPriority());
}


