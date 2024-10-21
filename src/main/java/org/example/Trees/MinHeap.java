package org.example.Trees;

import javafx.scene.paint.Color;

public class MinHeap<T extends Comparable<T>> extends Heap<T> {

    @Override
    protected void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = getParentIndex(index);
            if (heap.get(index).compareTo(heap.get(parentIndex)) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    @Override
    protected void heapifyDown(int index) {
        int leftChildIndex = getLeftChildIndex(index);
        int rightChildIndex = getRightChildIndex(index);
        int smallestIndex = index;

        if (leftChildIndex < heap.size() && heap.get(leftChildIndex).compareTo(heap.get(smallestIndex)) < 0) {
            smallestIndex = leftChildIndex;
        }

        if (rightChildIndex < heap.size() && heap.get(rightChildIndex).compareTo(heap.get(smallestIndex)) < 0) {
            smallestIndex = rightChildIndex;
        }

        if (smallestIndex != index) {
            swap(index, smallestIndex);
            heapifyDown(smallestIndex);
        }
    }

    @Override
    public String type() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color color() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
