# Sample Run output

```bash
MacBook-Pro:airlineReservationSystem-c poojakokane$ make test; ./test; make main; ./main 10; make wait; ./wait 10
gcc -Wall -pthread -O0 -g3 -o test test.c ars.c utils.c
no error
gcc -Wall -pthread -O0 -g3 -o main main.c ars.c utils.c
10 threads
done threads 10 tickets 9995 time 6.270
gcc -Wall -pthread -O0 -g3 -o wait wait.c ars.c utils.c
10 threads
done threads 10 time 7.041
MacBook-Pro:airlineReservationSystem-c poojakokane$ 

```