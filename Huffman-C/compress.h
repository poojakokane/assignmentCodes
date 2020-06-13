#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define DEBUG 0

void command_compress(FILE *i_file, FILE *op_file);
unsigned long get_symbol_table(FILE *i_file, char **opSymbol, unsigned int *opSymbolLen);
