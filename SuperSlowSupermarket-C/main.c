#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//#include <file.h>

//using namespace::std;

typedef struct customer{
    unsigned long inTime;
    unsigned long lineId;
    char* name;
    unsigned int items;
} Customer;

typedef struct node{
    Customer *customer;
    struct node* next;
} Node;

typedef struct queue{
    Node* front;
    Node* back;
}Queue;

#define QUEUES 12
#define MAX_NAME_LEN 10
#define MAX_IN_TIME 2000000000
#define MAX_ITEMS 100

#define cout fout




Customer *createCustomer(unsigned long time, unsigned long line, char *name, unsigned int items);

void enqueue(Queue *pQueue, Customer *pCustomer);

int queueEmpty(Queue queue);

void runSimulation(Queue *pQueue, FILE *pFile);

Customer *getNextCustomer(Queue *pQueue, unsigned long *currTime);

void checkoutCustomer(Customer *pCustomer, unsigned long *currTime);

Customer *getCustomerWithMinInTime(Queue *pQueue);

Customer *getCustomerWithinTimeWithMinItems(Queue *pQueue, unsigned long currTime);

int main() {
    char* inputFile = "in.txt";
    char* outFile = "out.txt";

    Queue queues[QUEUES];
    for(int i=0; i<QUEUES; i++){
        queues[i].front = NULL;
        queues[i].back = NULL;
    }

    FILE *fin = fopen(inputFile, "r");
    FILE *fptr = fopen(outFile, "w");

    int testcases;
    fscanf(fin, "%d", &testcases);

    while(testcases > 0){
        int customers;
        fscanf(fin, "%d", &customers);

        for(int i=0; i<customers; i++) {
            unsigned long inTime;
            unsigned long line;
            char* name = (char*) malloc(MAX_NAME_LEN * sizeof(char));
            unsigned int items;

            fscanf(fin, "%lu %lu %s %d\n", &inTime, &line, name, &items);
            line--;
            enqueue(queues, createCustomer(inTime, line, name, items));
        }

        runSimulation(queues, fptr);
//        clearAllQueues(queues);
        testcases--;
    }

    fclose(fptr);
    return 0;
}

void runSimulation(Queue *pQueue, FILE *pFile) {
    unsigned long currentTime = 0;

    Customer *c = getNextCustomer(pQueue, &currentTime);
    while(c != NULL){
        checkoutCustomer(c, &currentTime);
        fprintf(pFile, "%s from line %lu checks out at time %lu.\n", c->name, c->lineId+1,
                currentTime);

        free(c->name);
        c->name = NULL;
        free(c);
        c = NULL;

        c = getNextCustomer(pQueue, &currentTime);
    }
}

void checkoutCustomer(Customer *pCustomer, unsigned long *currTime) {
    *currTime += 30 + (pCustomer->items * 5);
    printf("%s from line %lu checks out at time %lu.\n", pCustomer->name, pCustomer->lineId+1, *currTime);
}

Customer *getNextCustomer(Queue *pQueue, unsigned long *currTime) {
    Customer *c = getCustomerWithinTimeWithMinItems(pQueue, *currTime);

    if(c == NULL) {
        c = getCustomerWithMinInTime(pQueue);
        if (c != NULL && c->inTime > *currTime)
            *currTime = c->inTime;
    }

    return c;
}

Customer *getCustomerWithinTimeWithMinItems(Queue *pQueue, unsigned long currTime) {
    int minIdx = -1;
    int minItems = MAX_ITEMS + 10;
    int found = 0;
    for(int i=0; i<QUEUES; i++){
        if(pQueue[i].front != NULL && pQueue[i].front->customer->inTime <= currTime
        && pQueue[i].front->customer->items < minItems){
            minItems = pQueue[i].front->customer->items;
            minIdx = i;
            found = 1;
        }
    }

    if(!found)
        return NULL;

    Node* n = pQueue[minIdx].front;
    if(pQueue[minIdx].front == pQueue[minIdx].back){
        pQueue[minIdx].front = NULL;
        pQueue[minIdx].back = NULL;
    }
    else {
        pQueue[minIdx].front = pQueue[minIdx].front->next;
    }
    n->next = NULL;

    Customer *c = n->customer;
    n->customer = NULL;
    free(n);

    return c;
}

Customer *getCustomerWithMinInTime(Queue *pQueue) {
    int minIdx;
    unsigned long minInTime = MAX_IN_TIME;
    int found = 0;
    for(int i=0; i<QUEUES; i++){
        if(pQueue[i].front != NULL && pQueue[i].front->customer->inTime < minInTime){
            minInTime = pQueue[i].front->customer->inTime;
            minIdx = i;
            found = 1;
        }
    }

    if(!found)
        return NULL;

    Node* n = pQueue[minIdx].front;
    if(pQueue[minIdx].front == pQueue[minIdx].back){
        pQueue[minIdx].front = NULL;
        pQueue[minIdx].back = NULL;
    }
    else {
        pQueue[minIdx].front = pQueue[minIdx].front->next;
    }
    n->next = NULL;

    Customer *c = n->customer;
    n->customer = NULL;
    free(n);

    return c;
}

void enqueue(Queue *pQueue, Customer *pCustomer) {
    Node* n = (Node*) malloc(sizeof(Node));
    n->customer = pCustomer;
    n->next = NULL;

    if(queueEmpty(pQueue[pCustomer->lineId])){
        pQueue[pCustomer->lineId].front = n;
        pQueue[pCustomer->lineId].back = n;
    }
    else{
        pQueue[pCustomer->lineId].back->next = n;
        pQueue[pCustomer->lineId].back = n;
    }
}

int queueEmpty(Queue queue) {
    return (queue.front == NULL && queue.back == NULL);
}

Customer *createCustomer(unsigned long time, unsigned long line, char *name, unsigned int items) {
    Customer *c = (Customer*) malloc(sizeof(Customer));
    c->inTime = time;
    c->lineId = line;

    c->name = (char*) malloc(MAX_NAME_LEN * sizeof(char));
    sprintf(c->name, "%s", name);

    c->items = items;

    return c;
}
