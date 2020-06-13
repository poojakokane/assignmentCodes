#include "huffman.h"

int main(int argc, char **argv) {

    if(argc != 4) {
        printf("Invalid number of arguments!\n");
        return -1;
    }

    char *input_file = argv[1];
    char *command = argv[2];
    char *option = argv[3];

    FILE *f = fopen(input_file, "r");

    if(f == NULL) {
        printf("Input file is not found!\n");
        return -1;
    }

    if(strcmp(command, "frequency") == 0) {

        int option_int = atoi(option);

        if(option_int < 0 || option_int > 255) {
            printf("Input option is invalid!\n");
            return -1;
        }

        unsigned long count = command_frequency(f, option_int);

        printf("%lu\n", count);


    } else if (strcmp(command, "bitcode") == 0) {

        int option_int = atoi(option);

        if(option_int < 0 || option_int > 255) {
            printf("Input option is invalid!\n");
            return -1;
        }

        char* bitCode = command_bitcode(f, (char) option_int);

        printf("%s\n", bitCode);


    } else if (strcmp(command, "compress") == 0) {

        FILE *output_file = fopen(option, "wb");

        if(output_file == NULL) {
            printf("Output file is invalid!\n");
            return -1;
        }

        command_compress(f, output_file);

        fclose(output_file);

    } else if (strcmp(command, "dictionary") == 0) {

        FILE *output_file = fopen(option, "wb");

        if(output_file == NULL) {
            printf("Output file is invalid!\n");
            return -1;
        }

        command_dictionary(f, output_file);

        fclose(output_file);

    } else {
        printf("Command is incorrect!\n");
        return -1;
    }

    fclose(f);
    return 0;

}