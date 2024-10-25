package org.example.Trees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.paint.Color;
import java.io.Serializable;

public class Tree24<T extends Comparable<T>> implements Tree<T>, Serializable {

    public class Node implements TreeNode<T>, Serializable {
        List<T> values;       // Holds 1 to 3 values
        List<Node> children;  // Holds 0 to 4 children

        public Node() {
            values = new ArrayList<>(3);
            children = new ArrayList<>(4);
        }

        @Override
        public String getColor() {
            return "BLUE";
        }

        boolean isLeaf() {
            return children.isEmpty();
        }

        boolean isFull() {
            return values.size() == 3;
        }

        @Override
        public T getValue() {
            return values.isEmpty() ? null : values.get(0);
        }

        public List<T> getValues() {
            return values;
        }

        public List<TreeNode<T>> getChildren() {
            List<TreeNode<T>> childNodes = new ArrayList<>();
            for (Node child : children) {
                childNodes.add(child);
            }
            return childNodes;
        }

        @Override
        public TreeNode<T> getLeft() {
            return children.isEmpty() ? null : children.get(0);
        }

        @Override
        public TreeNode<T> getRight() {
            return children.size() < 2 ? null : children.get(1);
        }
    }

    private Node root;
    private int size;

    public Tree24() {
        root = new Node();
        size = 0;
    }

    public void insert(T value) {
        if (root.isFull()) {
            Node newRoot = new Node();
            newRoot.children.add(root);
            splitChild(newRoot, 0);
            root = newRoot;
        }
        insertNonFull(root, value);
        size++;
    }

    private void insertNonFull(Node node, T value) {
        int i = node.values.size() - 1;

        if (node.isLeaf()) {
            node.values.add(value);
            Collections.sort(node.values);
        } else {
            while (i >= 0 && value.compareTo(node.values.get(i)) < 0) {
                i--;
            }
            i++;
            if (i < node.children.size() && node.children.get(i).isFull()) {
                splitChild(node, i);
                if (value.compareTo(node.values.get(i)) > 0) {
                    i++;
                }
            }
            if (i < node.children.size()) {
                insertNonFull(node.children.get(i), value);
            }
        }
    }

    private void splitChild(Node parent, int index) {
        Node fullChild = parent.children.get(index);
        Node newNode = new Node();

        // Check if there are enough values to split
        if (fullChild.values.size() < 3) {
            return;
        }

        T middleValue = fullChild.values.get(1);
        parent.values.add(index, middleValue);
        Collections.sort(parent.values);

        newNode.values.add(fullChild.values.get(2));
        fullChild.values = new ArrayList<>(fullChild.values.subList(0, 1));

        if (!fullChild.isLeaf() && fullChild.children.size() >= 4) {
            newNode.children.add(fullChild.children.get(2));
            newNode.children.add(fullChild.children.get(3));
            fullChild.children = new ArrayList<>(fullChild.children.subList(0, 2));
        }

        parent.children.add(index + 1, newNode);
    }

    @Override
    public List<T> inorderTraversal() {
        List<T> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }

    private void inorderTraversal(Node node, List<T> result) {
        if (node == null) return;

        for (int i = 0; i < node.values.size(); i++) {
            if (!node.isLeaf() && i < node.children.size()) {
                inorderTraversal(node.children.get(i), result);
            }
            result.add(node.values.get(i));
        }
        if (!node.isLeaf() && node.children.size() > node.values.size()) {
            inorderTraversal(node.children.get(node.values.size()), result);
        }
    }

    public void printTree() {
        printTree(root, 0);
    }

    private void printTree(Node node, int level) {
        if (node == null) return;

        System.out.println("Level " + level + " " + node.values);
        for (Node child : node.children) {
            printTree(child, level + 1);
        }
    }

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    public Color color() {
        return Color.BROWN;
    }

    public String type() {
        return "2-4 Tree";
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = new Node();
        size = 0;
    }

    @Override
    public boolean contains(T value) {
        return contains(root, value);
    }

    private boolean contains(Node node, T value) {
        if (node == null) return false;

        for (T val : node.values) {
            if (val.compareTo(value) == 0) return true;
        }

        for (int i = 0; i < node.values.size(); i++) {
            if (value.compareTo(node.values.get(i)) < 0) {
                return i < node.children.size() && contains(node.children.get(i), value);
            }
        }
        return !node.isLeaf() && node.children.size() > node.values.size()
                && contains(node.children.get(node.values.size()), value);
    }

    public boolean delete(T value) {
        if (!contains(root, value)) return false; // Return if value not found
        delete(root, value);

        // If root has no values after deletion, adjust it
        if (root.values.isEmpty() && !root.isLeaf()) {
            root = root.children.get(0); // Promote the first child as the new root
        }
        size--;
        return true;
    }

    private void delete(Node node, T value) {
        int pos = node.values.indexOf(value);

        // Case 1: Value is found in this node
        if (pos != -1) {
            if (node.isLeaf()) {
                node.values.remove(pos); // Directly remove from leaf
            } else {
                // Replace value with predecessor (or successor) in internal nodes
                Node predecessor = node.children.get(pos);
                while (!predecessor.isLeaf()) {
                    predecessor = predecessor.children.get(predecessor.values.size());
                }
                T predecessorValue = predecessor.values.get(predecessor.values.size() - 1);
                node.values.set(pos, predecessorValue);
                delete(node.children.get(pos), predecessorValue); // Delete the predecessor value
            }
        } else {
            // Case 2: Value is not in this node, find the child to recurse on
            int i = 0;
            while (i < node.values.size() && value.compareTo(node.values.get(i)) > 0) {
                i++;
            }

            Node child = node.children.get(i);

            // Ensure the child has enough values
            if (child.values.size() == 1) {
                fixChild(node, i);
            }

            delete(node.children.get(i), value); // Recur on the proper child
        }
    }

    private void fixChild(Node parent, int index) {
        Node child = parent.children.get(index);

        // Check if left sibling can lend a value
        if (index > 0 && parent.children.get(index - 1).values.size() > 1) {
            Node leftSibling = parent.children.get(index - 1);
            child.values.add(0, parent.values.get(index - 1));
            parent.values.set(index - 1, leftSibling.values.remove(leftSibling.values.size() - 1));
            if (!leftSibling.isLeaf()) {
                child.children.add(0, leftSibling.children.remove(leftSibling.children.size() - 1));
            }
        }
        // Check if right sibling can lend a value
        else if (index < parent.children.size() - 1 && parent.children.get(index + 1).values.size() > 1) {
            Node rightSibling = parent.children.get(index + 1);
            child.values.add(parent.values.get(index));
            parent.values.set(index, rightSibling.values.remove(0));
            if (!rightSibling.isLeaf()) {
                child.children.add(rightSibling.children.remove(0));
            }
        }
        // Merge with a sibling if neither can lend a value
        else {
            if (index < parent.children.size() - 1) {
                mergeChildren(parent, index);
            } else {
                mergeChildren(parent, index - 1);
            }
        }
    }

    // Merge child nodes when both siblings have only one value
    private void mergeChildren(Node parent, int index) {
        Node leftChild = parent.children.get(index);
        Node rightChild = parent.children.remove(index + 1);
        T separator = parent.values.remove(index);

        // Merge separator and right child's values into left child
        leftChild.values.add(separator);
        leftChild.values.addAll(rightChild.values);

        // Merge children if not leaf nodes
        if (!rightChild.isLeaf()) {
            leftChild.children.addAll(rightChild.children);
        }
    }
}
