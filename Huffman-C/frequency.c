#include "frequency.h"

unsigned long command_frequency(FILE *i_file, int option) {

    unsigned long result = 0;

    char buffer;

    while(fread(&buffer, sizeof(char), 1, i_file)) {
        if(buffer == option){
             result++;
        }
    }

    return result;

}