# Sample Run output

```bash
MacBook-Pro:airlineReservationSystem-c poojakokane$ make test
gcc -Wall -pthread -O0 -g3 -o test test.c ars.c utils.c
MacBook-Pro:airlineReservationSystem-c poojakokane$ ./test
no error
MacBook-Pro:airlineReservationSystem-c poojakokane$ make main
gcc -Wall -pthread -O0 -g3 -o main main.c ars.c utils.c
MacBook-Pro:airlineReservationSystem-c poojakokane$ ./main 10
10 threads
done threads 10 tickets 9992 time 6.352
MacBook-Pro:airlineReservationSystem-c poojakokane$ make wait
gcc -Wall -pthread -O0 -g3 -o wait wait.c ars.c utils.c
MacBook-Pro:airlineReservationSystem-c poojakokane$ ./wait 10
10 threads
done threads 10 time 6.811
MacBook-Pro:airlineReservationSystem-c poojakokane$ 
MacBook-Pro:airlineReservationSystem-c poojakokane$ 
```