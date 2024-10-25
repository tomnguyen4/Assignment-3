package org.example.Trees;

import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AVLTree<T extends Comparable<T>> implements Tree<T>, Serializable {
    //Root node
    private Node root;
    //Number of nodes AKA tree size
    private int size;

    //AVL tree node
    private class Node implements TreeNode<T>, Serializable {
        T value;
        Node left, right;
        int height;

        //New Node
        Node(T value) {
            this.value = value;
            this.height = 1; 
        }

        //Overrides
        @Override
        public T getValue() { return value; }

        @Override
        public TreeNode<T> getLeft() { return left; }

        @Override
        public TreeNode<T> getRight() { return right; }

        @Override
        public String getColor() { return "BLACK"; } //Placeholder rn
    }

    @Override
    public Color color() { return Color.BLACK; }

    @Override
    public String type() {
        return "AVL Tree"; //type method
    }

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    @Override
    public int size() {  
        return size;
    }

    @Override
    public boolean contains(T key) { // Implement contains method
        return search(root, key) != null;
    }

    //Height of node
    private int height(Node N) {
        if (N == null)
            return 0;
        return N.height;
    }

    //Right rotate subtree
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        //Rotate
        x.right = y;
        y.left = T2;

        //Update height
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        //Return new root
        return x;
    }

    //Left rotate subtree
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        //Rotate
        y.left = x;
        x.right = T2;

        //Update height
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        //Return new root
        return y;
    }

    //Get balance factor
    private int getBalance(Node N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    //Insert
    public void insert(T key) {
        root = insert(root, key);
        size++; //Increment on insert
    }

    private Node insert(Node node, T key) {
        //BST insert
        if (node == null)
            return new Node(key);

        if (key.compareTo(node.value) < 0)
            node.left = insert(node.left, key);
        else if (key.compareTo(node.value) > 0)
            node.right = insert(node.right, key);
        else
            return node; //Duplicate keys reject

        //Update height
        node.height = 1 + Math.max(height(node.left), height(node.right));

        //Get the balance factor
        int balance = getBalance(node);

        //Left Left
        if (balance > 1 && key.compareTo(node.left.value) < 0)
            return rightRotate(node);

        //Right Right
        if (balance < -1 && key.compareTo(node.right.value) > 0)
            return leftRotate(node);

        //Left Right
        if (balance > 1 && key.compareTo(node.left.value) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        //Right Left
        if (balance < -1 && key.compareTo(node.right.value) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        //Return node pointer
        return node;
    }

    //Delete
    public boolean delete(T key) {  
        root = delete(root, key);
        size--; //Decrement on delete
        return true; //successful delete
    }

    private Node delete(Node root, T key) {
        //BST DELETE
        if (root == null)
            return root;

        if (key.compareTo(root.value) < 0)
            root.left = delete(root.left, key);
        else if (key.compareTo(root.value) > 0)
            root.right = delete(root.right, key);
        else {
            //Node with 1 or 0 child
            if ((root.left == null) || (root.right == null)) {
                Node temp = root.left != null ? root.left : root.right;

                //0 child
                if (temp == null) {
                    temp = root;
                    root = null;
                } else //1 child
                    root = temp; //Copy
            } else {
                //2 children
                Node temp = minValueNode(root.right);
                root.value = temp.value; // Copy
                root.right = delete(root.right, temp.value); //Delete
            }
        }

        //if 1 node, then return
        if (root == null)
            return root;

        //UPDATE HEIGHT
        root.height = 1 + Math.max(height(root.left), height(root.right));

        //GET THE BALANCE FACTOR
        int balance = getBalance(root);

        //Left Left 
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        //Left Right
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        //Right Right
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        //Right Left
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    //min value node
    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    //Search
    public Node search(T key) {
        return search(root, key);
    }

    private Node search(Node root, T key) {
        if (root == null || root.value.equals(key))
            return root;

        if (key.compareTo(root.value) < 0)
            return search(root.left, key);
        
        return search(root.right, key);
    }

    //Clear
    public void clear() {
        root = null;
        size = 0; //Reset size
    }

    //In-order traversal
    @Override
    public List<T> inorderTraversal() {
        List<T> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }

    private void inorderTraversal(Node node, List<T> result) {
        if (node != null) {
            inorderTraversal(node.left, result);
            result.add(node.value);
            inorderTraversal(node.right, result);
        }
    }

    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();

        //Insert
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);
        tree.insert(50);
        tree.insert(25);

        //Print in-order traversal
        System.out.println("In-order traversal of the AVL tree:");
        System.out.println(tree.inorderTraversal());

        //Deleting a node
        tree.delete(10);
        System.out.println("In-order traversal after deletion of 10:");
        System.out.println(tree.inorderTraversal());

        //Searching a node
        AVLTree<Integer>.Node foundNode = tree.search(20); 
        if (foundNode != null) {
            System.out.println("Node with key " + foundNode.value + " found.");
        } else {
            System.out.println("Node with key not found.");
        }

        //Clear tree
        tree.clear();
        System.out.println("In-order traversal after clearing the tree:");
        System.out.println(tree.inorderTraversal());
    }
}
