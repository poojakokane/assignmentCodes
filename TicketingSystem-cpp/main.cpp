#include "Ticket.h"
#include "TicketQueue.h"

int main()
{
    TicketQueue tq(11);

    Ticket t0(0);

    Ticket t1(1);
    Ticket t2(2);

    tq.insertTicket(t0);
    tq.insertTicket(t1);
    tq.insertTicket(t2);
    tq.deleteTicket(t0);

    Ticket i0 = tq.ticketProcessed();
    cout<<i0.getClientType()<<"\t"<<i0.getPriority()<<endl;
    Ticket i1 = tq.ticketProcessed();
    cout<<i1.getClientType()<<"\t"<<i1.getPriority()<<endl;

    return 0;
}

