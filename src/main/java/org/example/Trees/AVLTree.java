package org.example.Trees;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AVLTree implements Serializable {
    private AVLNode root;

    //height of the node
    private int height(AVLNode N) {
        if (N == null)
            return 0;
        return N.height;
    }

    //right rotate subtree rooted with y
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        //rotation
        x.right = y;
        y.left = T2;

        //Update height
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        //Return new root
        return x;
    }

    //left rotate subtree rooted with x
    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        //rotation
        y.left = x;
        x.right = T2;

        //Update height
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        //new root
        return y;
    }

    //Get balance factor of node N
    private int getBalance(AVLNode N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    //Insert
    public void insert(int key) {
        root = insert(root, key);
    }

    private AVLNode insert(AVLNode node, int key) {
        //BST insert
        if (node == null)
            return (new AVLNode(key));

        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else
            return node;

        //Update height
        node.height = 1 + Math.max(height(node.left), height(node.right));

        //Get the balance factor
        int balance = getBalance(node);


        //Left Left
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        //Right Right
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        //Left Right
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        //Right Left
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        // Return node pointer
        return node;
    }

    //Delete
    public void delete(int key) {
        root = delete(root, key);
    }

    private AVLNode delete(AVLNode root, int key) {
        //BST DELETE
        if (root == null)
            return root;

        if (key < root.key)
            root.left = delete(root.left, key);
        else if (key > root.key)
            root.right = delete(root.right, key);
        else {
            //Node with only one child or no child
            if ((root.left == null) || (root.right == null)) {
                AVLNode temp = root.left != null ? root.left : root.right;

                //No child
                if (temp == null) {
                    temp = root;
                    root = null;
                } else // One child
                    root = temp; // Copy
            } else {
                //Node with two children
                AVLNode temp = minValueNode(root.right);
                root.key = temp.key; //Copy
                root.right = delete(root.right, temp.key); //Delete
            }
        }

        //If the tree had only one node then return
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

    //find the node with the minimum key value
    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    //Search
    public AVLNode search(int key) {
        return search(root, key);
    }

    private AVLNode search(AVLNode root, int key) {
        if (root == null || root.key == key)
            return root;

        if (root.key > key)
            return search(root.left, key);
        
        return search(root.right, key);
    }

    //Clear
    public void clear() {
        root = null;
    }

    //In-order traversal
    public List<Integer> inorderTraversal() {
        List<Integer> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }

    private void inorderTraversal(AVLNode node, List<Integer> result) {
        if (node != null) {
            inorderTraversal(node.left, result);
            result.add(node.key);
            inorderTraversal(node.right, result);
        }
    }

    public static void main(String[] args) {
        AVLTree tree = new AVLTree();

        //InserT
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
        AVLNode foundNode = tree.search(20);
        if (foundNode != null) {
            System.out.println("Node with key " + foundNode.key + " found.");
        } else {
            System.out.println("Node with key not found.");
        }

        //Clear tree
        tree.clear();
        System.out.println("In-order traversal after clearing the tree:");
        System.out.println(tree.inorderTraversal());
    }
}
