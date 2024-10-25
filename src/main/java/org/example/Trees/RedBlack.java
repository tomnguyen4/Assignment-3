package org.example.Trees;
//Auther: Abdelnasser Ouda
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RedBlack<T extends Comparable<T>> implements Tree<T>, Serializable {
    private Node root;
    private int size;
    private Node FINALNULL;

    private class Node implements TreeNode<T>, Serializable {
        T value;
        Node left, right;
        Node parent;
        boolean isRed;

        Node(T value) {
            this.value = value;
            this.left = this.right = this.parent = null;
            this.isRed = true; //New nodes are red by default
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public TreeNode<T> getLeft() {
            return left;
        }

        public TreeNode<T> getParent() {
            return parent;
        }

        @Override
        public TreeNode<T> getRight() {
            return right;
        }

        public String getColor() {
            return isRed ? "RED" : "BLACK";
        }

        public void setColor(boolean isRed) {
            this.isRed = isRed;
        }
    }

    @Override
    public Color color() { return Color.BLACK; }

    public void insert(T value) {
        System.out.println("____________________");
        Node newNode = new Node(value);
        root = bstInsert(root, newNode);

        if(newNode.parent == null)
        {
            newNode.setColor(false);
            return;
        }
        if(newNode.parent.parent == null)
        {
            return;
        }
        fixViolations(newNode);
        size++;
    }

    private Node bstInsert(Node root, Node newNode) {
        if (root == null) {
            return newNode; //Found a spot for the new node
        }

        if (newNode.value.compareTo(root.value) < 0) {
            root.left = bstInsert(root.left, newNode);
            root.left.parent = root; //Set parent
        } else {
            root.right = bstInsert(root.right, newNode);
            root.right.parent = root; //Set parent
        }

        return root;
    }

    private void fixViolations(Node node) {
        System.out.println("Starting fixViolations with node value: " + (node != null ? node.value : "null"));
        Node temp;
        while (node.parent.isRed) {
            Node parent = node.parent;
            Node grandparent = parent.parent;
    
            System.out.println("Current node value: " + node.value);
            System.out.println("Node color: " + node.isRed);

            System.out.println("Parent value: " + parent.value);
            System.out.println("Parent color: " + parent.isRed);


            System.out.println("Grandparent value: " + (grandparent != null ? grandparent.value : "null"));
            System.out.println("Grandparent color: " + grandparent.isRed);

            //Determine if the parent is a left or right child
            if (parent == grandparent.left) {
                Node uncle = grandparent.right;
                System.out.println("Parent is left child. Uncle value: " + (uncle != null ? uncle.value : "null"));
    
                //Case 1: Uncle is red
                if (uncle != null && uncle.isRed) {
                    System.out.println("Case 1: Uncle is red. Recoloring parent and uncle to black, grandparent to red.");
                    parent.setColor(false); //Recolor parent to black
                    uncle.setColor(false);   //Recolor uncle to black
                    grandparent.setColor(true); //Recolor grandparent to red
                    node = grandparent; //Move up the tree
                } else {
                    //Case 2: Uncle is black
                    if (node == parent.right) {
                        //Sub-case 2.1: Node is a right child
                        System.out.println("Sub-case 2.1: Node is right child. Performing left rotation on parent.");
                        node = node.parent;
                        leftRotate(node); //Left rotate the parent
                    }
                    //Sub-case 2.2: Node is a left child
                    System.out.println("Sub-case 2.2: Recoloring parent to black and grandparent to red.");
                    node.parent.setColor(false); //Recolor parent to black
                    node.parent.parent.setColor(true); //Recolor grandparent to red
                    rightRotate(node.parent.parent); //Right rotate grandparent
                }
            } else { //Symmetric to the above
                Node uncle = grandparent.left;
                System.out.println("Parent is right child. Uncle value: " + (uncle != null ? uncle.value : "null"));
    
                //Case 1: Uncle is red
                if (uncle != null && uncle.isRed) {
                    System.out.println("Case 1: Uncle is red. Recoloring parent and uncle to black, grandparent to red.");
                    parent.setColor(false);
                    uncle.setColor(false);
                    grandparent.setColor(true);
                    node = grandparent;
                } else {
                    //Case 2: Uncle is black
                    if (node == parent.left) {
                        System.out.println("Sub-case 2.1: Node is left child. Performing right rotation on parent.");
                        node = node.parent;
                        rightRotate(node); //Right rotate the parent
                    }
                    System.out.println("Sub-case 2.2: Recoloring parent to black and grandparent to red.");
                    node.parent.setColor(false);
                    node.parent.parent.setColor(true);
                    leftRotate(node.parent.parent); //Left rotate grandparent
                }
            }
            if(node == root) {
                break;
            }
        }
        
        System.out.println("Final step: Setting root color to black.");
        root.setColor(false); //Ensure the root is always black
    }
    

    private void leftRotate(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;

        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;

        if (node.parent == null) {
            root = rightChild; //Right child becomes new root
        } else if (node == node.parent.left) {
            node.parent.left = rightChild; //Link right child to left
        } else {
            node.parent.right = rightChild; //Link right child to right
        }

        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rightRotate(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;

        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;

        if (node.parent == null) {
            root = leftChild; //Left child becomes new root
        } else if (node == node.parent.right) {
            node.parent.right = leftChild; //Link left child to right
        } else {
            node.parent.left = leftChild; //Link left child to left
        }

        leftChild.right = node;
        node.parent = leftChild;
    }



   

    @Override
    public String type() {
         return "RBT";
    }
    /* 
    @Override   
    public boolean delete(T value) {
        int originalSize = size;
        root = delete(root, value);
        return size < originalSize;
    }

    private Node delete(Node node, T value) {
        if (node == null) {
            return null;
        }
        if (value.compareTo(node.value) < 0) {
            node.left = delete(node.left, value);
        } else if (value.compareTo(node.value) > 0) {
            node.right = delete(node.right, value);
        } else {
            if (node.left == null) {
                size--;
                return node.right;
            } else if (node.right == null) {
                size--;
                return node.left;
            }
            Node minRight = findMin(node.right);
            node.value = minRight.value;
            node.right = delete(node.right, minRight.value);
        }
        return node;
    }
    */
    /* 
    @Override
    public boolean delete(T value) {
        //Store the original size of the tree
        int originalSize = size;

        //Delete the node and get the potentially updated root
        Node nodeToFix = delete(root, value);

        //If the node was not found, return false; otherwise, return true
        return size < originalSize;
    }

    private Node delete(Node node, T value) {
        Node replacement;
        Node child;
        boolean originalColor = node.isRed;

        //Case 1: Node has two children
        if (node.left != null && node.right != null) {
            Node successor = findMin(node.right); //Get the in-order successor
            originalColor = successor.isRed; //Store the color of the successor
            node.value = successor.value; //Copy the successor's value to the node
            node = successor; //Move the node pointer to the successor
        }

        //Case 2: Node has at most one child
        if (node.left != null) {
            replacement = node.left; //Get the only child
        } else {
            replacement = node.right; //Get the only child
        }

        //Remove the node
        if (replacement != null) {
            //Link the child to the parent
            replacement.parent = node.parent;
        }

        if (node.parent == null) {
            root = replacement; //Node was root
        } else if (node == node.parent.left) {
            node.parent.left = replacement; //Link the child to the left
        } else {
            node.parent.right = replacement; //Link the child to the right
        }

        node = null; //Clear the node
        return originalColor ? null : replacement; //Return the replacement or null
    }


    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void fixDoubleBlack(Node node) {
        while (node != root && getColor(node) == "BLACK") {
            if (node == node.parent.left) {
                Node sibling = node.parent.right; //Sibling is the right child
                if (sibling.isRed) {
                    //Case 1: Sibling is RED
                    sibling.setColor(false);
                    node.parent.setColor(true);
                    leftRotate(node.parent);
                    sibling = node.parent.right; //Reassign sibling after rotation
                }
    
                //Case 2: Sibling is BLACK
                if (getColor(sibling) == "BLACK") {
                    if (getColor(sibling.left) == "BLACK" && getColor(sibling.right) == "BLACK") {
                        //Sub-case 2.1: Both children are BLACK
                        sibling.setColor(true);
                        node = node.parent; //Move up the tree
                    } else {
                        //Sub-case 2.2: At least one child is RED
                        if (sibling.right.isRed) {
                            //Sibling's far child is RED
                            sibling.setColor(node.parent.isRed);
                            node.parent.setColor(false);
                            leftRotate(node.parent);
                        } else {
                            //Sibling's near child is RED
                            sibling.left.setColor(false);
                            sibling.setColor(true);
                            rightRotate(sibling);
                        }
                        sibling = node.parent.right; //Reassign sibling after rotation
                    }
                }
            } else {
                //Mirror cases for the right child
                Node sibling = node.parent.left;
                if (sibling.isRed) {
                    sibling.setColor(false);
                    node.parent.setColor(true);
                    rightRotate(node.parent);
                    sibling = node.parent.left; //Reassign sibling after rotation
                }
    
                if (getColor(sibling) == "BLACK") {
                    if (getColor(sibling.left) == "BLACK" && getColor(sibling.right) == "BLACK") {
                        sibling.setColor(true);
                        node = node.parent; //Move up the tree
                    } else {
                        if (sibling.left.isRed) {
                            sibling.setColor(node.parent.isRed);
                            node.parent.setColor(false);
                            rightRotate(node.parent);
                        } else {
                            sibling.right.setColor(false);
                            sibling.setColor(true);
                            leftRotate(sibling);
                        }
                        sibling = node.parent.left; //Reassign sibling after rotation
                    }
                }
            }
        }
        node.setColor(false); //Ensure the root is always black
    }
    */

    @Override
    public boolean delete(T value) {
        //Store the original size of the tree
        int originalSize = size;

        //Delete the node
        Node nodeToDelete = findNode(root, value);
        if (nodeToDelete == null) {
            return false; //Node not found
        }

        root = delete(root, nodeToDelete); //Perform the deletion
        return size < originalSize; //Return true if size decreased
    }

    private Node delete(Node root, Node nodeToDelete) {
        Node replacement;
        Node child;
        boolean originalColor = nodeToDelete.isRed; //Store the color of the node to delete

        //Case 1: Node has two children
        if (nodeToDelete.left != null && nodeToDelete.right != null) {
            Node successor = findMin(nodeToDelete.right); //Get the in-order successor
            originalColor = successor.isRed; //Store the color of the successor
            nodeToDelete.value = successor.value; //Copy the successor's value to the node
            nodeToDelete = successor; //Move the node pointer to the successor
        }

        //Case 2: Node has at most one child
        if (nodeToDelete.left != null) {
            replacement = nodeToDelete.left; //Get the only child
        } else {
            replacement = nodeToDelete.right; //Get the only child
        }

        //Remove the node
        if (replacement != null) {
            //Link the child to the parent
            replacement.parent = nodeToDelete.parent;
        }

        if (nodeToDelete.parent == null) {
            root = replacement; //Node was root
        } else if (nodeToDelete == nodeToDelete.parent.left) {
            nodeToDelete.parent.left = replacement; //Link the child to the left
        } else {
            nodeToDelete.parent.right = replacement; //Link the child to the right
        }

        //Now we need to fix the double black situation
        if (!originalColor) {
            fixDoubleBlack(replacement);
        }

        //Clear the node
        nodeToDelete = null;
        size--; //Decrease the size of the tree
        return root; //Return the updated root
    }

    private void fixDoubleBlack(Node node) {
        //If the node is null, just return
        if (node == null) {
            return;
        }

        //If the node is the root, just return
        if (node == root) {
            return;
        }

        //If node is black and has no children
        if (node.isRed) {
            node.setColor(false); //Change it to black
            return;
        }

        Node sibling = getSibling(node);
        if (sibling == null) {
            fixDoubleBlack(node.parent); //Propagate the double black upwards
        } else {
            if (sibling.isRed) {
                //Case 1: Sibling is red
                sibling.setColor(false);
                node.parent.setColor(true);
                if (node == node.parent.left) {
                    leftRotate(node.parent);
                } else {
                    rightRotate(node.parent);
                }
                fixDoubleBlack(node); //Fix again after rotation
            } else {
                //Case 2: Sibling is black
                if (isBlack(sibling.left) && isBlack(sibling.right)) {
                    sibling.setColor(true); //Recolor sibling
                    fixDoubleBlack(node.parent); //Propagate double black upwards
                } else {
                    if (isRed(sibling.left)) {
                        if (node == node.parent.left) {
                            sibling.setColor(true);
                            sibling.left.setColor(false);
                            rightRotate(sibling);
                        } else {
                            sibling.setColor(true);
                            node.parent.setColor(false);
                            leftRotate(node.parent);
                        }
                    } else {
                        if (node == node.parent.left) {
                            sibling.setColor(true);
                            node.parent.setColor(false);
                            leftRotate(node.parent);
                        } else {
                            sibling.setColor(true);
                            sibling.right.setColor(false);
                            leftRotate(sibling);
                        }
                    }
                }
            }
        }
    }

    private boolean isBlack(Node node) {
        return node == null || !node.isRed; //A null node is considered black
    }

    private boolean isRed(Node node) {
        return node != null && node.isRed;
    }

    private Node getSibling(Node node) {
        if (node.parent == null) {
            return null; //Node is root, no sibling
        }
        return (node == node.parent.left) ? node.parent.right : node.parent.left; //Return sibling
    }

    //This method finds the minimum node in a subtree
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    //This method finds the node with the given value
    private Node findNode(Node node, T value) {
        if (node == null) {
            return null;
        }
        if (value.compareTo(node.value) < 0) {
            return findNode(node.left, value);
        } else if (value.compareTo(node.value) > 0) {
            return findNode(node.right, value);
        } else {
            return node; //Node found
        }
    }


    private String getColor(Node node) {
        return node == null ? "BLACK" : node.getColor();
    }

    @Override
    public boolean contains(T value) {
        return contains(root, value);
    }

    private boolean contains(Node node, T value) {
        if (node == null) {
            return false;
        }
        if (value.compareTo(node.value) == 0) {
            return true;
        } else if (value.compareTo(node.value) < 0) {
            return contains(node.left, value);
        } else {
            return contains(node.right, value);
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

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

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }
}