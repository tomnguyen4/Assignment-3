package org.example.Trees;

import javafx.scene.paint.Color;
import java.io.Serializable;

public class MinHeap<T extends Comparable<T>> extends Heap<T> implements Serializable {

    // Inner class representing a node in the heap with value and references to parent, left, and right children
    private class Node implements TreeNode<T>, Serializable {
        T value;
        Node parent;
        Node left, right;

        // Constructor initializes a node with the specified value
        Node(T value) {
            this.value = value;
            this.left = this.right = this.parent = null;
        }

        @Override
        public T getValue() {
            return value;
        }

        // Returns the left child node, if it exists, by calculating the left child's index
        @Override
        public TreeNode<T> getLeft() {
            int leftIndex = getLeftChildIndex(heap.indexOf(value));
            return leftIndex < heap.size() ? new Node(heap.get(leftIndex)) : null;
        }

        // Returns the right child node, if it exists, by calculating the right child's index
        @Override
        public TreeNode<T> getRight() {
            int rightIndex = getRightChildIndex(heap.indexOf(value));
            return rightIndex < heap.size() ? new Node(heap.get(rightIndex)) : null;
        }

        // Returns the parent node, if it exists, by calculating the parent's index
        public TreeNode<T> getParent() {
            int parentIndex = getParentIndex(heap.indexOf(value));
            return parentIndex >= 0 ? new Node(heap.get(parentIndex)) : null;
        }

        // Custom color for visualizing nodes in the MinHeap
        @Override
        public String getColor() {
            return "GREEN";
        }
    }

    // Restores the min-heap property by moving a node up the heap if necessary
    protected void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = getParentIndex(index);
            // Swap if the current node is less than its parent
            if (heap.get(index).compareTo(heap.get(parentIndex)) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    // Restores the min-heap property by moving a node down the heap if necessary
    protected void heapifyDown(int index) {
        int leftChildIndex = getLeftChildIndex(index);
        int rightChildIndex = getRightChildIndex(index);
        int smallestIndex = index;

        // Find the smallest node among the current node and its children
        if (leftChildIndex < heap.size() && heap.get(leftChildIndex).compareTo(heap.get(smallestIndex)) < 0) {
            smallestIndex = leftChildIndex;
        }

        if (rightChildIndex < heap.size() && heap.get(rightChildIndex).compareTo(heap.get(smallestIndex)) < 0) {
            smallestIndex = rightChildIndex;
        }

        // If the smallest node is not the current node, swap and continue heapifying down
        if (smallestIndex != index) {
            swap(index, smallestIndex);
            heapifyDown(smallestIndex);
        }
    }

    // Returns the color used for visualizing nodes in the MinHeap
    public Color color() {
        return Color.GREEN;
    }

    // Returns the root of the heap, if it exists
    public TreeNode<T> getRoot() {
        return heap.isEmpty() ? null : new Node(heap.get(0));
    }

    // Returns the type of heap as a string
    public String type() {
        return "MinHeap";
    }
}
