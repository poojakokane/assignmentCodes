# Sample output

```bash
pi@hogwarts:/mnt/raid1_1tb/scratchpad/alarmThreads $ ls
main.cpp  main.h  Makefile
pi@hogwarts:/mnt/raid1_1tb/scratchpad/alarmThreads $ make
g++ -o mot main.cpp -I. -lpthread
pi@hogwarts:/mnt/raid1_1tb/scratchpad/alarmThreads $ ./mot
Current time is: Sun Jul  5 15:23:13 2020
Current time is: Sun Jul  5 15:23:14 2020
Current time is: Sun Jul  5 15:23:15 2020
Current time is: Sun Jul  5 15:23:16 2020
Current time is: Sun Jul  5 15:23:17 2020
Current time is: Sun Jul  5 15:23:18 2020
Current time is: Sun Jul  5 15:23:19 2020
Current time is: Sun Jul  5 15:23:20 2020
Current time is: Sun Jul  5 15:23:21 2020
Current time is: Sun Jul  5 15:23:22 2020
Current time is: Sun Jul  5 15:23:23 2020
Current time is: Sun Jul  5 15:23:24 2020
Current time is: Sun Jul  5 15:23:25 2020
Current time is: Sun Jul  5 15:23:26 2020
Current time is: Sun Jul  5 15:23:27 2020
Current time is: Sun Jul  5 15:23:28 2020
Alarm went off!!! after 17s
Current time is: Sun Jul  5 15:23:29 2020
Current time is: Sun Jul  5 15:23:30 2020
Current time is: Sun Jul  5 15:23:31 2020
Current time is: Sun Jul  5 15:23:32 2020
Current time is: Sun Jul  5 15:23:33 2020
Current time is: Sun Jul  5 15:23:34 2020
Current time is: Sun Jul  5 15:23:35 2020
Current time is: Sun Jul  5 15:23:36 2020
Current time is: Sun Jul  5 15:23:37 2020
Exiting as timeout of 25s over...

pi@hogwarts:/mnt/raid1_1tb/scratchpad/alarmThreads $
```