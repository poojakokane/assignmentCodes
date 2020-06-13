#include "compress.h"
#include "bitcode.h"

void fillUpOpFile(FILE *ipFile, FILE *opFile, char *opSymbol[256], unsigned int opSymbolLen[256]);

void command_compress(FILE *i_file, FILE *op_file) {

    if(DEBUG) {
        printf("Currently in command_compress\n");
    }

    char* opSymbol[256];
    unsigned int opSymbolLen[256];

    get_symbol_table(i_file, opSymbol, opSymbolLen);

    fillUpOpFile(i_file, op_file, opSymbol, opSymbolLen);

}

void fillUpOpFile(FILE *ipFile, FILE *opFile, char *opSymbol[256], unsigned int opSymbolLen[256]) {
    char currentChar;
    rewind(ipFile);

    char outputChar = 0;
    int opCharLen = 0;
    while(fread(&currentChar, sizeof(char), 1, ipFile)){
        char *currSymbol = opSymbol[currentChar];
        unsigned int currSymbolLen = opSymbolLen[currentChar];
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

unsigned long get_symbol_table(FILE *i_file, char **opSymbol, unsigned int *opSymbolLen) {
    struct tree* root = create_huffman_tree_from_file(i_file);
    for(int i=0; i<256; i++){
        char *symbol = get_bit_code_from_tree(root, (char)i);
        unsigned int symbolLen = strlen(symbol);

        if(symbolLen > 0){
            opSymbol[i] = symbol;
            opSymbolLen[i] = symbolLen;
        }
        else{
            opSymbol[i] = "";
            opSymbolLen[i] = 0;
        }
    }

    return root->root->freqency;
}
