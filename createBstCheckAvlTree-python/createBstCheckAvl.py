import os
import sys


class Node:
    def __init__(self, key):
        self.left = None
        self.right = None
        self.val = key

    # A utility function to insert a new node with the given key

class Height:
    def __init__(self):
        self.height = 0

def insert(root, node):
    if root is None:
        root = node
    else:
        if root.val < node.val:
            if root.right is None:
                root.right = node
            else:
                insert(root.right, node)
        else:
            if root.left is None:
                root.left = node
            else:
                insert(root.left, node)

            # A utility function to do inorder tree traversal


# helper function to check if binary
# tree is height balanced
def isBalanced(root, height):
    # lh and rh to store height of
    # left and right subtree
    lh = Height()
    rh = Height()

    # Base condition when tree is
    # empty return true
    if root is None:
        return [0, True]

    # l and r are used to check if left
    # and right subtree are balanced
    l = isBalanced(root.left, lh)
    r = isBalanced(root.right, rh)

    # height of tree is maximum of
    # left subtree height and
    # right subtree height plus 1
    height.height = max(lh.height, rh.height) + 1

    if abs(lh.height - rh.height) <= 1:
        return [rh.height - lh.height, l[1] and r[1]]

        # if we reach here then the tree
    # is not balanced
    return [rh.height - lh.height, False]

def inorder(root):
    if root:
        inorder(root.left)
        print(root.val)
        inorder(root.right)


def createBst(nums):
    bst = Node(int(nums[0]))
    for i in range(1,len(nums)):
        insert(bst, Node(int(nums[i])))

    return bst


def runForFile(param):
    with open(param, "r") as file:
        for line in file:
            nums = line.split(',')
            bst = createBst(nums)
            res = isBalanced(bst, Height())
            if res[1] == True:
                print(res[0],",AVL")
            else:
                print(res[0],",NOTAVL")


def main():
    # Driver program to test the above functions

    # Let us create the following BST
    #      50
    #    /      \
    #   30     70
    #   / \    / \
    #  20 40  60 80
    # r = Node(50)
    # insert(r, Node(30))
    # insert(r, Node(20))
    # insert(r, Node(40))
    # insert(r, Node(70))
    # insert(r, Node(60))
    # insert(r, Node(80))
    #
    # # Print inoder traversal of the BST
    # inorder(r)
    # print(isBalanced(r, Height()), "\n\n")
    #
    # r = Node(-16)
    # insert(r,Node(2))
    # insert(r,Node(-33))
    # insert(r, Node(1))
    # insert(r, Node(-8))
    # insert(r, Node(-49))
    # insert(r, Node(4))
    # insert(r, Node(-37))
    # print(isBalanced(r, Height()), "\n\n")

    runForFile("inputs.txt")

    # for line in sys.stdin:
    #
    #     if '' == line.rstrip():
    #         break
    #
    #     line = line.split(",")
    #
    #     r = Node(int(line[0]))
    #
    #     for i in range(1, len(line)):
    #         insert(r, Node(int(line[i])))
    #
    #     res = isBalanced(r, Height())
    #     if res[1] == True:
    #         sys.stdout.write(str(res[0]))
    #         sys.stdout.flush()
    #         sys.stdout.write(",AVL\n")
    #         sys.stdout.flush()
    #     else:
    #         sys.stdout.write(str(res[0]))
    #         sys.stdout.flush()
    #         sys.stdout.write(",NOTAVL\n")
    #         sys.stdout.flush()

if __name__ == "__main__":
    main()