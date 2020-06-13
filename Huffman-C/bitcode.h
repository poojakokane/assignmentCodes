#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

struct treeNode{
    unsigned long freqency;
    int isLeaf;
    char ch;
    struct treeNode *left;
    struct treeNode *right;
};

struct tree{
    struct treeNode* root;
};

struct listOfNodes{
    struct listNode* head;
};

struct listNode{
    struct treeNode* treeNode;
    struct listNode* next;
    struct listNode* prev;
};



char * command_bitcode(FILE *i_file, char option);

struct tree *create_huffman_tree_from_file(FILE *pFile);

char *get_bit_code_from_tree(struct tree *tree, char ch);