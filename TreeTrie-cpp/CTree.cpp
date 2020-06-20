//
// Created by HarryZ on 6/20/2020.
//

#include <sstream>
#include <iostream>
#include <cassert>
#include <string>
#include "CTree.h"

CTree::CTree(char ch){
    this->data = ch;
    this->prev = NULL;
    this->kids = NULL;
    this->sibs = NULL;
}

CTree::~CTree() {
    // Destroy all kids
    while(this->kids != NULL){
        CTree *tmp = this->kids;
        this->kids = tmp->sibs;
        tmp->destroyDown();
        tmp = NULL;
    }

    // Destroy all siblings to the right
    while(this->sibs != NULL){
        CTree *tmp = this->sibs;
        this->sibs = tmp->sibs;
        tmp->destroyDown();
        tmp = NULL;
    }
}

void CTree::destroyDown() {
    // Destroy all kids
    while(this->kids != NULL){
        CTree *tmp = this->kids;
        this->kids = tmp->sibs;
        delete tmp;
        tmp = NULL;
    }

//    delete this;
}

std::string CTree::toString() {
    if(this == NULL){
        return "";
    }

    std::string res;
    res.push_back(this->data);
    res.append("\n");

    // Append kids
    CTree *curr = this->kids;
    while(curr != NULL){
        res.append(curr->toStringDown());
        curr = curr->sibs;
    }

    // Append sibs
    curr = this->sibs;
    while(curr != NULL){
        res.append(curr->toStringDown());
        curr = curr->sibs;
    }

    return res;
}

std::string CTree::toStringDown() {
    if(this == NULL){
        return "";
    }

    std::string res;
    res.push_back(this->data);
    res.append("\n");

    // Append kids
    CTree *curr = this->kids;
    while(curr != NULL){
        res.append(curr->toStringDown());
        curr = curr->sibs;
    }

    return res;
}

bool CTree::addChild(char ch) {
    CTree *newNode = new CTree(ch);

    return this->addChild(newNode);
}

bool CTree::addSibling(char ch) {
    if(this->prev == NULL)
        return false;

    CTree *newNode = new CTree(ch);

    CTree *curr = this->kids;
    CTree *prev = this;

    while(curr != NULL && curr->data < ch){
        prev = curr;
        curr = curr->sibs;

        if(curr->data == ch){
            return false;
        }
    }

    if(curr != NULL && curr->data == newNode->data){
        return false;
    }

    newNode->sibs = curr;
    newNode->prev = prev;

    prev->sibs = newNode;

    return true;
}

bool CTree::addChild(CTree *root) {
    CTree *newNode = root;
    CTree *curr = this->kids;
    CTree *prev = this;

    while(curr != NULL && curr->data < newNode->data){
        prev = curr;
        curr = curr->sibs;

        if(curr != NULL && curr->data == newNode->data){
            return false;
        }
    }

    if(curr != NULL && curr->data == newNode->data){
        return false;
    }

    newNode->sibs = curr;
    newNode->prev = prev;

    if(this->kids == curr){
        prev->kids = newNode;
    }
    else{
        prev->sibs = newNode;
    }

    return true;
}

CTree &CTree::operator^(CTree &rt) {
    this->addChild(&rt);
    return *this;
}

std::ostream &operator<<(std::ostream &os, CTree &rt) {
    os << rt.toString();
    return os;
}

bool CTree::operator==(const CTree &root) {

    if (this->data != root.data){
        return false;
    }

    CTree *currChild = this->kids;
    CTree *targetChild = root.kids;

    while(currChild != NULL){
        if(targetChild == NULL){
            return false;
        }

        if(!(*currChild == *targetChild)){
            return false;
        }

        currChild = currChild->sibs;
        targetChild = targetChild->sibs;
    }

    if(targetChild != NULL){
        return false;
    }

    return true;
}
