#include "dictionary.h"
#include "compress.h"

void write_dictionary_to_file(FILE *opFile, char *symbol[256], unsigned int symbolLen[256]);

void command_dictionary(FILE *i_file, FILE *op_file) {

    if(DEBUG) {
        printf("Currently in command_dictionary\n");
    }

    char* opSymbol[256];
    unsigned int opSymbolLen[256];

    unsigned long compressedFileLen = get_symbol_table(i_file, opSymbol, opSymbolLen);

    write_dictionary_to_file(op_file, opSymbol, opSymbolLen);
}

void write_dictionary_to_file(FILE *opFile, char *symbol[256], unsigned int symbolLen[256]) {

    char outputChar = 0;
    unsigned int opCharLen = 0;

    for(int sIdx = 0; sIdx<256; sIdx++){
        char *currSymbol = symbol[sIdx];
        unsigned int currSymbolLen = symbolLen[sIdx];

        char maskForOutputChar1 = ((char)0b11111111) << opCharLen;
        char maskForOutputChar2 = ~maskForOutputChar1;
        unsigned int nextOpCharLen = 8 - opCharLen;
        outputChar = outputChar << (8 - opCharLen);
        outputChar += (currSymbolLen & maskForOutputChar1);
        fwrite(&outputChar, sizeof(char), 1, opFile);
        opCharLen = 0;
        outputChar = 0;

        if(maskForOutputChar2 > 0) {
            outputChar += (currSymbolLen & ~maskForOutputChar2);
            opCharLen = nextOpCharLen;
        }
        
        for(unsigned int i=0; i < currSymbolLen; i++){
            outputChar *= 2;
            outputChar += (currSymbol[i] == '1')? 1 : 0;
            opCharLen++;

            if(opCharLen == 8){
                fwrite(&outputChar, sizeof(char), 1, opFile);
                opCharLen = 0;
                outputChar = 0;
            }
        }
    }

    // Put the last byte with the padding bits included
    char paddingBits = 0;
    if(opCharLen > 0 && opCharLen < 8) {
        while (opCharLen < 8) {
            outputChar *= 2;
            opCharLen++;
            paddingBits++;
        }
        fwrite(&outputChar, sizeof(char ), 1, opFile);
    }

    // Print the number of padding bits added
    fwrite(&paddingBits, sizeof(char), 1, opFile);
}
