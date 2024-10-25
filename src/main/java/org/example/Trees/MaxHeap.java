package org.example.Trees;

import javafx.scene.paint.Color;
import java.io.Serializable;

public class MaxHeap<T extends Comparable<T>> extends Heap<T> implements Serializable {

    // Inner class representing a node in the heap, with references to its value, parent, left, and right children
    private class Node implements TreeNode<T>, Serializable {
        T value;
        Node parent;
        Node left, right;

        // Constructor initializes a node with a specified value
        Node(T value) {
            this.value = value;
            this.left = this.right = this.parent = null;
        }

        @Override
        public T getValue() {
            return value;
        }

        // Returns the left child node, if it exists
        @Override
        public TreeNode<T> getLeft() {
            int leftIndex = getLeftChildIndex(heap.indexOf(value));
            return leftIndex < heap.size() ? new Node(heap.get(leftIndex)) : null;
        }

        // Returns the right child node, if it exists
        @Override
        public TreeNode<T> getRight() {
            int rightIndex = getRightChildIndex(heap.indexOf(value));
            return rightIndex < heap.size() ? new Node(heap.get(rightIndex)) : null;
        }

        // Returns the parent node, if it exists
        public TreeNode<T> getParent() {
            int parentIndex = getParentIndex(heap.indexOf(value));
            return parentIndex >= 0 ? new Node(heap.get(parentIndex)) : null;
        }

        // Custom color for visualization
        @Override
        public String getColor() {
            return "BLUE";
        }
    }

    // Restores the max-heap property by moving a node up the heap if necessary
    protected void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = getParentIndex(index);
            // Swap if the current node is greater than its parent
            if (heap.get(index).compareTo(heap.get(parentIndex)) > 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    // Restores the max-heap property by moving a node down the heap if necessary
    protected void heapifyDown(int index) {
        int leftChildIndex = getLeftChildIndex(index);
        int rightChildIndex = getRightChildIndex(index);
        int largestIndex = index;

        // Find the largest node among the current node and its children
        if (leftChildIndex < heap.size() && heap.get(leftChildIndex).compareTo(heap.get(largestIndex)) > 0) {
            largestIndex = leftChildIndex;
        }

        if (rightChildIndex < heap.size() && heap.get(rightChildIndex).compareTo(heap.get(largestIndex)) > 0) {
            largestIndex = rightChildIndex;
        }

        // If the largest node is not the current node, swap and continue heapifying down
        if (largestIndex != index) {
            swap(index, largestIndex);
            heapifyDown(largestIndex);
        }
    }

    // Returns the color used for visualizing nodes in the MaxHeap
    public Color color() {
        return Color.BLUE;
    }

    // Returns the root of the heap, if it exists
    public TreeNode<T> getRoot() {
        return heap.isEmpty() ? null : new Node(heap.get(0));
    }

    // Returns the type of heap as a string
    public String type() {
        return "MaxHeap";
    }
}
