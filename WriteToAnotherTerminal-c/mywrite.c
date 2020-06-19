#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <utmp.h>
#include <time.h>
#include <utmpx.h>

#define      SHOWHOST

char *showtime(long);

int writeToRemote(char buf[8192], int fdd, char *uname, char* hostname);

void getAllFdds(char *uname, int fdds[50], int *pSize, char *selfUsername, char *selfHostname);

int main(int ac, char *av[]) {
    if (ac < 2) {
        fprintf(stderr, "usage: write0 username\n");
        exit(1);
    }


    char buf[BUFSIZ];
    int fdd[50];
    int len = 0;
    char self_userName[100];
    char self_hostName[100];

    getAllFdds(av[1], fdd, &len, self_userName, self_hostName);
    /* loop until EOF on input */
    while (fgets(buf, BUFSIZ, stdin) != NULL) {

        for (int i = 0; i < len; i++) {
            writeToRemote(buf, fdd[i], self_userName, self_hostName);
        }
    }

    char *done = "EOF\n";
    for (int i = 0; i < len; i++) {
        write(fdd[i], done, strlen("done"));
        close(fdd[i]);
    }

    return 0;


}

void getAllFdds(char *uname, int fdds[50], int *pSize, char *selfUsername, char *selfHostname) {
    struct utmp *n = (struct utmp *) malloc(sizeof(struct utmp));
    setutent();
    n = getutent();

    while (n) {
        if (n->ut_type == USER_PROCESS) {
//            printf("%9s%12s (%s)\n", n->ut_user, n->ut_line, n->ut_host);

            if (*pSize == 0) {
                char buffUname[500];
                char buffHostname[500];

                sprintf(selfUsername, "%s", n->ut_user);
#ifdef SHOWHOST
                sprintf(selfHostname, "%s", n->ut_host);
#endif

            }
            if (n->ut_line[0] == 'p' && n->ut_line[1] == 't' && n->ut_line[2] == 's'
                && strcmp(n->ut_user, uname) == 0) {
                char fname[500];
                sprintf(fname, "%s/%s", "/dev", n->ut_line);
                int fdd;
                fdd = open(fname, O_WRONLY);
                if (fdd == -1) {
                    perror(fname);
                    exit(1);
                }

                fdds[*pSize] = fdd;
                (*pSize)++;
            }
        }
        n = getutent();
    }
}

int writeToRemote(char buf[8192], int fdd, char *uname, char *hostname) {
//    write ( fdd, buffer, strlen(buffer) );
    char buffer[500];
    long t;
    time(&t);
    sprintf(buffer, "Message from %s@%s on yourtty at %s \n", uname, hostname, showtime(t));
    write(fdd, buffer, strlen(buffer));
    write(fdd, buf, strlen(buf));
}


char *showtime(long timeval) {
    char *min = malloc(10 * sizeof(char));
    char *hour = malloc(10 * sizeof(char));
    struct tm *mytime;
    mytime = localtime(&timeval);

    sprintf(hour, "%.2d:", (mytime->tm_hour));
    sprintf(min, "%.2d", (mytime->tm_min));
    strcat(hour, min);
    return hour;
}