#Sample run

Logged in as pi
```
pi@hogwarts:/tmp/tmp.U5IqybnQZG $ gcc mywrite.c
mywrite.c: In function ‘getAllFdds’:
mywrite.c:71:20: warning: ‘strcmp’ argument 1 declared attribute ‘nonstring’ [-Wstringop-overflow=]
&& strcmp(n->ut_user, uname) == 0) {
^~~~~~~~~~~~~~~~~~~~~~~~~
In file included from /usr/include/utmp.h:29,
from mywrite.c:6:
/usr/include/arm-linux-gnueabihf/bits/utmp.h:65:8: note: argument ‘ut_user’ declared here
char ut_user[UT_NAMESIZE]
^~~~~~~
pi@hogwarts:/tmp/tmp.U5IqybnQZG $
pi@hogwarts:/tmp/tmp.U5IqybnQZG $
pi@hogwarts:/tmp/tmp.U5IqybnQZG $ sudo ./a.out pi2
hello
there
hey
world
pi@hogwarts:/tmp/tmp.U5IqybnQZG $
```
<br/>
<br/>

The output on another terminal where pi2 is logged in
```
Message from pi2@192.168.1.119 on yourtty at 21:27
hello
Message from pi2@192.168.1.119 on yourtty at 21:27
there
Message from pi2@192.168.1.119 on yourtty at 21:27
hey
Message from pi2@192.168.1.119 on yourtty at 21:27
world
EOF


```