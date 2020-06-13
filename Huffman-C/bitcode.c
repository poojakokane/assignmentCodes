#include "bitcode.h"

char * command_bitcode(FILE *i_file, char option) {
    struct tree* root = create_huffman_tree_from_file(i_file);

    char *result = get_bit_code_from_tree(root, option);
    return result;
}

int get_bit_code_helper(struct treeNode *pNode, char ch, char *buff, int *buffLen);

char *get_bit_code_from_tree(struct tree *tree, char ch) {
    struct treeNode *curr = tree->root;
    char* buff = (char*)malloc(sizeof(char) * 1024);
    int len = 0;
    get_bit_code_helper(curr, ch, buff, &len);

    buff[len] = '\0';
    return buff;
}

int get_bit_code_helper(struct treeNode *pNode, char ch, char *buff, int *buffLen) {
    if(pNode->isLeaf == 1){
        if(pNode->ch == ch){
            return 1;
        }

        // (*buffLen)--;
        return 0;
    }

    buff[(*buffLen)] = '1';
    (*buffLen)++;
    if(get_bit_code_helper(pNode->left, ch, buff, buffLen) == 1){
        return 1;
    }

    buff[(*buffLen) - 1] = '0';
    if(get_bit_code_helper(pNode->right, ch, buff, buffLen) == 1){
        return 1;
    }

    (*buffLen)--;
    return 0;
}

struct listOfNodes *get_list_of_frequncies(FILE *pFile);

void fill_up_all_frequencies(FILE *pFile, unsigned long pInt[256], int size);

struct treeNode *create_new_treeNode(unsigned long freq, char ch);

void append_to_list(struct listOfNodes *list, struct treeNode *pNode);

int list_len(struct listOfNodes *list);

void get_and_remove_two_minimum_nodes_from_list(struct listOfNodes *list, struct listNode **pNode1,
                                                struct listNode **pNode2);

void get_and_remove_minimum_node_from_list(struct listOfNodes *list, struct listNode **pNode);

void append_to_list_2(struct listOfNodes *list, struct treeNode *pNode);

unsigned long getTotalFileSize(struct listOfNodes *pNodes);

struct tree *create_huffman_tree_from_file(FILE *pFile) {
    struct listOfNodes* listOfNodes = get_list_of_frequncies(pFile);

    int listLen = list_len(listOfNodes);
    while (listLen >= 2) {
        struct listNode *one;
        struct listNode *two;
        get_and_remove_two_minimum_nodes_from_list(listOfNodes, &one, &two);

        struct treeNode *newNode = create_new_treeNode(one->treeNode->freqency
                + two->treeNode->freqency, '-');
        newNode->isLeaf = 0;
        newNode->left = two->treeNode;
        newNode->right = one->treeNode;
        one = NULL;
        two = NULL;
        
        append_to_list_2(listOfNodes, newNode);

        listLen = list_len(listOfNodes);
    }

    struct tree *res = (struct tree*) malloc(sizeof(struct tree));
    res->root = listOfNodes->head->treeNode;
    return res;
}

unsigned long getTotalFileSize(struct listOfNodes *pNodes) {
    unsigned long res = 0;

    struct listNode *curr = pNodes->head;
    while(curr != NULL){
        if(curr->treeNode->isLeaf == 1) {
            res += curr->treeNode->freqency;
        }

        curr = curr->next;
    }
    
    return res;
}

void append_to_list_2(struct listOfNodes *list, struct treeNode *pNode) {
    struct listNode* n = (struct listNode*) malloc(sizeof(struct listNode));
    n->treeNode = pNode;
    n->next = list->head;
    n->prev = NULL;

    if (list->head != NULL)
        list->head->prev = n;

    list->head = n;
}

void get_and_remove_two_minimum_nodes_from_list(struct listOfNodes *list, struct listNode **pNode1,
                                                struct listNode **pNode2) {
    get_and_remove_minimum_node_from_list(list, pNode1);
    get_and_remove_minimum_node_from_list(list, pNode2);
}

void get_and_remove_minimum_node_from_list(struct listOfNodes *list, struct listNode **pNode) {
    struct listNode *curr = list->head;
    unsigned long minFreq = ULLONG_MAX;
    while(curr != NULL){
        if(curr->treeNode->freqency < minFreq){
            minFreq = curr->treeNode->freqency;
            *pNode = curr;
        }

        curr = curr->next;
    }

    if(list->head == *pNode){
        list->head = (*pNode)->next;
        if(list->head != NULL) {
            list->head->prev = NULL;
        }
    }
    else{
        (*pNode)->prev->next = (*pNode)->next;
        if((*pNode)->next!=NULL){
            (*pNode)->next->prev = (*pNode)->prev;
        }
    }

    (*pNode)->prev = NULL;
    (*pNode)->next = NULL;
}

int list_len(struct listOfNodes *list) {
    int result = 0;

    struct listNode *curr = list->head;
    while(curr!= NULL){
        curr = curr->next;
        result++;
    }

    return result;
}

struct listOfNodes *get_list_of_frequncies(FILE *pFile) {
    unsigned long freq[256];
    fill_up_all_frequencies(pFile, freq, 256);

    struct listOfNodes *res = (struct listOfNodes*) malloc(sizeof(struct listOfNodes));

    for(int i=255; i>=0; i--){
        if(freq[i] > 0){
            append_to_list(res, create_new_treeNode(freq[i], (char)i));
        }
    }

    return res;
}

void append_to_list(struct listOfNodes *list, struct treeNode *pNode) {
    pNode->right = NULL;
    pNode->left = NULL;

    struct listNode* n = (struct listNode*) malloc(sizeof(struct listNode));
    n->treeNode = pNode;
    n->next = list->head;
    n->prev = NULL;

    if (list->head != NULL)
        list->head->prev = n;

    list->head = n;
}

struct treeNode *create_new_treeNode(unsigned long freq, char ch) {
    struct treeNode *res = (struct treeNode*) malloc(sizeof(struct treeNode));

    res->freqency = freq;
    res->ch = ch;
    res->isLeaf = 1;
    res->left = NULL;
    res->right = NULL;

    return res;
}

void fill_up_all_frequencies(FILE *pFile, unsigned long pInt[256], int size) {
    
    for(int i=0; i<size; i++){
        pInt[i] = 0;
    }
    
    char buffer;
    while(fread(&buffer, sizeof(char), 1, pFile)){
        pInt[buffer]++;
    }
}
