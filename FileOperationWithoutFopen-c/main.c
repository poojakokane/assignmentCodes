#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>

int main(int argc, char *argv[]) {
    //check if valid number of arguments are passed in program
    if (argc < 2) {
        printf("File name parameter is missing in arguments. Usage : newfile <fileName> [content]\n");
        return -1;
    }

    int outputFile = open(argv[1], O_WRONLY | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH);
    if (outputFile == -1) {
        printf("Error! File name supplied could not be opened for writing.");
        return -1;
    }

    //loop through all the remaining arguments
    //and output them to the file
    for (int i = 2; i < argc; i++) {
        write(outputFile, argv[i], strlen(argv[i]));
        write(outputFile, " ", 1);
    }
    //close the file
    close(outputFile);
    //return 0 at end of program
    return 0;
} 
