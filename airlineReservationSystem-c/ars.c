// a toy Airline Reservations System

#include "ars.h"
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <pthread.h>
#include <stdio.h>
#include <unistd.h>
struct flight_info {
  int next_tid; // +1 everytime
  int nr_booked; // booked <= seats
  pthread_mutex_t big_lock;
  //#HG - add a new conditionaql variable
  struct ticket tickets[]; // all issued tickets of this flight
};


int __nr_flights = 0;
int __nr_seats = 0;
struct flight_info ** flights = NULL;

static int ticket_cmp(const void * p1, const void * p2)
{
  const uint64_t v1 = *(const uint64_t *)p1;
  const uint64_t v2 = *(const uint64_t *)p2;
  if (v1 < v2)
    return -1;
  else if (v1 > v2)
    return 1;
  else
    return 0;
}

void tickets_sort(struct ticket * ts, int n)
{
  qsort(ts, n, sizeof(*ts), ticket_cmp);
}

void ars_init(int nr_flights, int nr_seats_per_flight)
{
  flights = malloc(sizeof(*flights) * nr_flights);
  for (int i = 0; i < nr_flights; i++) {
    flights[i] = calloc(1, sizeof(flights[i][0]) + (sizeof(struct ticket) * nr_seats_per_flight));
    flights[i]->next_tid = 1;
    pthread_mutex_init(&(flights[i]->big_lock), NULL);
  }
  __nr_flights = nr_flights;
  __nr_seats = nr_seats_per_flight;

  
}

int book_flight(short user_id, short flight_number)
{

  // wrong number
  if (flight_number >= __nr_flights)
    {//pthread_mutex_unlock(&(flights[flight_number]->big_lock));
    return -1;}

  pthread_mutex_lock(&(flights[flight_number]->big_lock));


  struct flight_info * fi = flights[flight_number];
  // full
  if (fi->nr_booked >= __nr_seats) {
    {pthread_mutex_unlock(&(flights[flight_number]->big_lock));
    return -1;}
   
  
  }

  int tid = fi->next_tid++;
  // book now
  fi->tickets[fi->nr_booked].uid = user_id;
  fi->tickets[fi->nr_booked].fid = flight_number;
  fi->tickets[fi->nr_booked].tid = tid;
  fi->nr_booked++;
  pthread_mutex_unlock(&(flights[flight_number]->big_lock));
  return tid;
}

// a helper function for cancel/change
// search a ticket of a flight and return its offset if found
static int search_ticket(struct flight_info * fi, short user_id, int ticket_number)
{
  for (int i = 0; i < fi->nr_booked; i++)
    if (fi->tickets[i].uid == user_id && fi->tickets[i].tid == ticket_number)
      return i; // cancelled

  return -1;
}

// a helper function for cancel/change
// search a flight and return its offset if found
/*
static int search_flight(int flight_number)
{
  for (int i = 0; i < __nr_flights; i++)
    if (flights[i]->)
      return i; // cancelled

  return -1;
}
*/


bool cancel_flight(short user_id, short flight_number, int ticket_number)
{
  //printf("acquire lock from cancel_flight\n");
  if (flight_number >= __nr_flights){
    //printf("release lock from cancel_flight\n");
    //pthread_mutex_unlock(&(flights[flight_number]->big_lock));
    return false;
  }


  pthread_mutex_lock(&(flights[flight_number]->big_lock));


  struct flight_info * fi = flights[flight_number];
  int offset = search_ticket(fi, user_id, ticket_number);
  if (offset >= 0) {
    fi->tickets[offset] = fi->tickets[fi->nr_booked-1];
    fi->nr_booked--;
    //printf("release lock from cancel_flight\n");
    pthread_mutex_unlock(&(flights[flight_number]->big_lock));
    return true; // cancelled
  }
  //printf("release lock from cancel_flight\n");
  pthread_mutex_unlock(&(flights[flight_number]->big_lock));
  return false; // not found
}

int change_flight(short user_id, short old_flight_number, int old_ticket_number,
                  short new_flight_number)
{
  // wrong number or no-op
  //printf("acquire lock from change_flight\n");
  //pthread_mutex_lock(&big_lock);
  if (old_flight_number >= __nr_flights ||
      new_flight_number >= __nr_flights ||
      old_flight_number == new_flight_number)
    {//printf("release lock from change_flight\n");
      //pthread_mutex_unlock(&big_lock);
      return -1;}

  // two things must be done atomically: (1) cancel the old ticket and (2) book a new ticket
  // if any of the two operations cannot be done, nothing should happen
  // for example, if the new flight has no seat, the existing ticket must remain valid
  // if the old ticket number is invalid, don't acquire a new ticket
  // TODO: your code here
  int tid = book_flight(user_id, new_flight_number);
  if (tid >= 0 && cancel_flight(user_id, old_flight_number, old_ticket_number))
  {
    //printf("release lock from change_flight\n");
    //pthread_mutex_unlock(&big_lock);
    return tid;
  }
		
  else {
    if(tid >= 0){
      cancel_flight(user_id, new_flight_number, tid);
    }
    //printf("release lock from change_flight\n");
    //pthread_mutex_unlock(&big_lock);
  	return -1;
  }
	

}

// malloc and dump all tickets in the returned array
struct ticket * dump_tickets(int * n_out)
{
  int n = 0;
  for (int i = 0; i < __nr_flights; i++)
    n += flights[i]->nr_booked;

  struct ticket * const buf = malloc(sizeof(*buf) * n);
  assert(buf);
  n = 0;
  for (int i = 0; i < __nr_flights; i++) {
    memcpy(buf+n, flights[i]->tickets, sizeof(*buf) * flights[i]->nr_booked);
    n += flights[i]->nr_booked;
  }
  *n_out = n; // number of tickets
  return buf;
}

int book_flight_can_wait(short user_id, short flight_number)
{
  // wrong number
  if (flight_number >= __nr_flights)
    return -1;
  //struct flight_info * fi = flights[flight_number];
  // full
  while (1){
    int tid = book_flight(user_id, flight_number);
    if(tid>=0) return tid;

  }

  return -1;
}
