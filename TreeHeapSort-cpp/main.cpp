#include <iostream>
#include <vector>
#include <cstdlib>
using namespace std;

template <typename E>
class VectorCompleteTree
{
private: //member data
    std::vector<E> V; //tree contents
public: //publicly accessible types
    typedef typename std::vector<E>::iterator Position; //a position in the tree

protected: //protected utility functions
    Position pos(int i) //map an index to a position
    {
        return V.begin() + i;
    }
    int idx(const Position &p) const //map a position to an index
    {
        return p - V.begin();
    }

public:
    VectorCompleteTree() : V(1) {} //constructor
    int size() const { return V.size() - 1; }
    Position left(const Position &p) { return pos(2 * idx(p)); }
    Position right(const Position &p) { return pos(2 * idx(p) + 1); }
    Position parent(const Position &p) { return pos(idx(p) / 2); }
    bool hasLeft(const Position &p) const { return 2 * idx(p) <= size(); }
    bool hasRight(const Position &p) const { return 2 * idx(p) + 1 <= size(); }
    bool isRoot(const Position &p) const { return idx(p) == 1; }
    Position root() { return pos(1); }
    Position last() { return pos(size()); }
    void addLast(const E &e) { V.push_back(e); }
    void removeLast() { V.pop_back(); }
    void swap(const Position &p, const Position &q)
    {
        E e = *q;
        *q = *p;
        *p = e;
    }
};

template <typename E, typename C>
class HeapPriorityQueue
{
public:
    int size() const; //number of elements
    bool empty() const; //is the queue empty?
    void insert(const E &e); //insert element
    const E &min(); //minimum element
    void removeMin(); //remove element
private:
    VectorCompleteTree<E> T; //priority queue contents
    C isLess; //less-than comparator

    typedef typename VectorCompleteTree<E>::Position Position; //shortcut for tree position
};

template <typename E, typename C> //number of elements
int HeapPriorityQueue<E, C>::size() const
{
    return T.size();
}

template <typename E, typename C> //is the queue empty?
bool HeapPriorityQueue<E, C>::empty() const
{
    return size() == 0;
}

template <typename E, typename C> //minimum element
const E &HeapPriorityQueue<E, C>::min()
{
    return *(T.root());
} //return reference to root element

template <typename E, typename C> //insert element
void HeapPriorityQueue<E, C>::insert(const E &e)
{
    T.addLast(e); //add e to heap
    Position v = T.last(); //e's position

    while (!T.isRoot(v)) //upheap
    {
        Position u = T.parent(v);
        if (!isLess(*v, *u))
            break; //if v is in order break
        T.swap(v, u); //else swap with parent
        v = u;
    }
}

template <typename E, typename C> //remove minimum element
void HeapPriorityQueue<E, C>::removeMin()
{
    if (size() == 1) //if there is only one node remove it
        T.removeLast();

    else
    {
        Position u = T.root(); //root position
        T.swap(u, T.last()); //swap last with root
        T.removeLast(); //remove last

        while (T.hasLeft(u)) //down heap
        {
            Position v = T.left(u);

            if (T.hasRight(u) && isLess(*(T.right(u)), *v))
                v = T.right(u); //v is u's smaller child

            if (isLess(*v, *u))
            {
                T.swap(u, v); //swap u if out of order
                u = v;
            }
            else
                break;
        }
    }
}

struct cmp
{
    bool operator()(int a, int b)
    {
        return a < b;
    }
};


int main()
{
    vector<int> HSV;

    int VSize = 50;

    for (int i = 0; i < VSize; i++)
    {
        int temp = rand() % 11;
        HSV.push_back(temp);
    }

    cout << "Vector unsorted: \n";

    for (int i = 0; i < HSV.size(); i++)
        cout << HSV[i] << " ";

    HeapPriorityQueue<int, cmp> heap;
    for(int i=0; i<HSV.size(); i++){
        heap.insert(HSV[i]);
    }

    for(int i=0; i<HSV.size(); i++){
        HSV[i] = heap.min();
        heap.removeMin();
    }

    cout << endl << endl;
    cout << "Vector sorted\n";
    for (int i = 0; i < HSV.size(); i++)
        cout << HSV[i] << " ";

    cout << endl;

}