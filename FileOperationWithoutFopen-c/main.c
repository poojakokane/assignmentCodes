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
    // //open the file for writing to
    // FILE *outputFile = fopen(argv[1], "w"); //***CANT USE FILE STRUCT OR FOPEN!!!***
    // //check if file is opened successfully for writing
    // if (outputFile == NULL) {
    //     printf("Error! File name supplied could not be opened for writing.");
    //     return -1;
    // }
    int outputFile = open(argv[1], O_WRONLY | O_CREAT);
    if (outputFile == -1) {
        printf("Error! File name supplied could not be opened for writing.");
        return -1;
    }

    //loop through all the remaining arguments
    //and output them to the file
    for (int i = 2; i < argc; i++) {
        write(outputFile, argv[i], strlen(argv[1]));
        write(outputFile, " ", 1);
    }
    //close the file
    close(outputFile);
    //return 0 at end of program
    return 0;
} 