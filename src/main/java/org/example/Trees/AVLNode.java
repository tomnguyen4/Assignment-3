package org.example.Trees;

class AVLNode {
    int key;
    int height;
    AVLNode left, right;

    AVLNode(int d) {
        key = d;
        height = 1;
    }
}