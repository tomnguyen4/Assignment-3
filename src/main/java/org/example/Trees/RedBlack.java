package org.example.Trees;
//Auther: Abdelnasser Ouda
import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RedBlack<T extends Comparable<T>> implements Tree<T>, Serializable {
    private Node root;
    private int size;
    private Node sentinel = new Node(null); //Create a sentinel node
    
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
        size++;
        System.out.println("New node added, size is now " + size);
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
    }

    private Node bstInsert(Node root, Node newNode) {
        if (root == null) {
            return newNode; //Found a spot for the new node
        }

        if (newNode.value.compareTo(root.value) < 0) {
            root.left = bstInsert(root.left, newNode);
            root.left.parent = root; //set parent
        } else {
            root.right = bstInsert(root.right, newNode);
            root.right.parent = root; //set parent
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
                        //sub-case 2.1: Node is a right child
                        System.out.println("Sub-case 2.1: Node is right child. Performing left rotation on parent.");
                        node = node.parent;
                        leftRotate(node); //Left rotate the parent
                    }
                    //sub-case 2.2: Node is a left child
                    System.out.println("Sub-case 2.2: Recoloring parent to black and grandparent to red.");
                    node.parent.setColor(false); //Recolor parent to black
                    node.parent.parent.setColor(true); //Recolor grandparent to red
                    rightRotate(node.parent.parent); //Right rotate grandparent
                }
            } else { //symmetric to the above
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
        System.out.println("Performing left rotation on node: " + node.value);
    
        Node rightChild = node.right;
        System.out.println("Right child before rotation: " + (rightChild != null ? rightChild.value : "null"));
    
        node.right = rightChild.left;
    
        if (rightChild.left != null) {
            rightChild.left.parent = node;
            System.out.println("Setting parent of " + rightChild.left.value + " to node " + node.value);
        }
    
        rightChild.parent = node.parent;
    
        if (node.parent == null) {
            root = rightChild; //Right child becomes new root
            System.out.println("New root after left rotation: " + root.value);
        } else if (node == node.parent.left) {
            node.parent.left = rightChild; //Link right child to left
        } else {
            node.parent.right = rightChild; //Link right child to right
        }
    
        rightChild.left = node;
        node.parent = rightChild;
    
        System.out.println("Left rotation complete. New left child of " + rightChild.value + ": " + node.value);
    }
    

    private void rightRotate(Node node) {
        System.out.println("Performing right rotation on node: " + node.value);
    
        Node leftChild = node.left;
        System.out.println("Left child before rotation: " + (leftChild != null ? leftChild.value : "null"));
    
        node.left = leftChild.right;
    
        if (leftChild.right != null) {
            leftChild.right.parent = node;
            System.out.println("Setting parent of " + leftChild.right.value + " to node " + node.value);
        }
    
        leftChild.parent = node.parent;
    
        if (node.parent == null) {
            root = leftChild; //Left child becomes new root
            System.out.println("New root after right rotation: " + root.value);
        } else if (node == node.parent.right) {
            node.parent.right = leftChild; //Link left child to right
        } else {
            node.parent.left = leftChild; //Link left child to left
        }
    
        leftChild.right = node;
        node.parent = leftChild;
    
        System.out.println("Right rotation complete. New right child of " + leftChild.value + ": " + node.value);
    }
    



   

    @Override
    public String type() {
         return "RBT";
    }
    
    @Override
    public boolean delete(T value) {
        //store the original size of the tree
        int originalSize = size;

        //Delete the node
        Node nodeToDelete = findNode(root, value);
        if (nodeToDelete == null) {
            return false; //Node not found
        }

        root = delete(root, nodeToDelete); //Perform the deletion
        return size < originalSize; //Return true if size decreased
    }

    private Node transplant(Node u, Node v) {
        if (u.parent == null) {
            System.out.println("Transplanting: Node " + u.value + " is the root, replacing it with Node " + (v != null ? v.value : "null"));
            v.parent = null;
            root = v;
            System.out.println("Root node is now: " + root.value);
        } else if (u == u.parent.left) {
            System.out.println("Transplanting: Node " + u.value + " is the left child of Node " + u.parent.value + ", replacing it with Node " + (v != null ? v.value : "null"));
            u.parent.left = v;
        } else {
            System.out.println("Transplanting: Node " + u.value + " is the right child of Node " + u.parent.value + ", replacing it with Node " + (v != null ? v.value : "null"));
            u.parent.right = v;
        }
        if (v != null) {
            System.out.println("Setting parent of Node " + v.value + " to Node " + (u.parent != null ? u.parent.value : "null"));
            v.parent = u.parent;
        }
        return root;
    }
    
    private Node delete(Node root, Node nodeToDelete) {
        sentinel.isRed = false;
        int updated = 0;
        Node replacement = null;
        boolean originalColor = nodeToDelete.isRed;
        System.out.println("Deleting Node " + nodeToDelete.value + " (Original color: " + (originalColor ? "Red" : "Black") + ")");
    
        if (nodeToDelete.left == null) { //Case: Node has at most one child (right child only or no children)
            System.out.println("Node " + nodeToDelete.value + " has at most one child, transplanting with right child");
            replacement = nodeToDelete.right;
            if(replacement == null)
            {
                System.out.println("Node replacement was null");

            }
            else {
                System.out.println("Node replacement was not null, with value: " + replacement.value);

            }
            root = transplant(nodeToDelete, replacement);
            System.out.println("xX_Root node is now: " + root.value);

        } else if (nodeToDelete.right == null) { //Case: Node has only left child
            System.out.println("Node " + nodeToDelete.value + " has only left child, transplanting with left child");
            replacement = nodeToDelete.left;
            root = transplant(nodeToDelete, replacement);
        } else { //Case: Node has two children
            System.out.println("Node " + nodeToDelete.value + " has two children, finding successor");
            Node successor = findMin(nodeToDelete.right); //Find in-order successor
            replacement = successor.right;
            if(replacement != null)
            {
                System.out.println("_xXReplacement not null, with value " + replacement.value);
            }
            else {
                System.out.println("Replacement still null somehow");
            }
            originalColor = successor.isRed;
            System.out.println("Successor found: Node " + successor.value + " (Color: " + (originalColor ? "Red" : "Black") + ")");
        
            if (successor.parent != nodeToDelete) {
                System.out.println("Successor " + successor.value + " is not the direct child of the node to delete");
                root = transplant(successor, successor.right);
                successor.right = nodeToDelete.right;
                if (successor.right != null) {
                    successor.right.parent = successor; //Update parent pointer
                }
                System.out.println("Successor " + successor.value + " is set as the new parent of Node " + successor.right.value);
            }
            
            root = transplant(nodeToDelete, successor); //Replace nodeToDelete with successor
            successor.left = nodeToDelete.left;
            if (successor.left != null) {
                successor.left.parent = successor; //Update left child's parent pointer
                System.out.println("Node " + successor.left.value + " has new parent " + successor.left.parent.value);
            }
            successor.isRed = nodeToDelete.isRed; //Preserve original color
            replacement = successor;
            System.out.println("Node " + nodeToDelete.value + " is replaced by its successor " + successor.value);
        }
        
        System.out.println("________Root node is now: " + root.value);

        
        

        //Fix double black if necessary
        if (!originalColor) {
            if(replacement == null)
            {
                
                    
                System.out.println("Replacement is null");
                sentinel.parent = nodeToDelete.parent;
                replacement = sentinel;
                if(replacement.parent != null)
                {   
                    if(replacement.parent.left != null)
                    {
                        System.out.println("Replacement parent left is " + replacement.parent.left);
                    }else {
                        System.out.println("Replacement parent left is null");
                    }
                    if(replacement.parent.right != null)
                    {
                        System.out.println("Replacement parent right is " + replacement.parent.right);
                    }else {
                        System.out.println("Replacement parent right is null");
                    }
                    if(replacement.parent.left == null)
                    {
                        replacement.parent.left = replacement;
                    }
                    else {
                        replacement.parent.right = replacement;
                    }
                }
                else{
                    System.out.println("Replacement has been set to sentinal with value: ");
                    if(replacement.value == null)
                    {
                        System.out.println("NULLIO");

                    }
                    else {
                        System.out.println("N0t Null?");

                    }
                    

                }
                System.out.println("Replacement parent is now " + replacement.parent.value);

            } 
            System.out.println("Fixing double black starting from Node " + (replacement != null ? replacement.value : "null"));
            root = fixDoubleBlack(replacement);
            if(replacement.value == null)
            {
                if(replacement.parent != null)
                {
                    if(replacement.parent.right == replacement)
                    {
                        replacement.parent.right = null;                    
                    }
                    else {
                        replacement.parent.left = null;
                    }
                    replacement.parent = null;
                }
            }
            
        }
    
        System.out.println("Deletion of Node " + nodeToDelete.value + " complete");
        size--;
        nodeToDelete = null;
        System.out.println("Size decreased, size is now: " + size);
        System.out.println("Node deleted, new tree has root with value " + root.value);
        if(root.parent == null)
        {
            System.out.println("Head or root has no parent");
        }
        else {
            System.out.println("root parent is " + root.parent);
        }
        root.isRed = false;
        return root;
    }
    

    private Node fixDoubleBlack(Node x) {
        while (x != root && (x == null || !x.isRed)) {
            if (x == x.parent.left) { //x is the left child
                Node sibling = x.parent.right;
                if (sibling.isRed) { //Case 1: x's sibling is red
                    System.out.println("Case 1: Sibling " + sibling.value + " is red.");
                    sibling.isRed = false;
                    x.parent.isRed = true;
                    leftRotate(x.parent);
                    sibling = x.parent.right;
                }
                if ((sibling.left == null || !sibling.left.isRed) && (sibling.right == null || !sibling.right.isRed)) { //Case 2: Both of sibling's children are black
                    System.out.println("Case 2: Sibling " + sibling.value + " has two black children.");
                    sibling.isRed = true;
                    x = x.parent;
                } else {
                    if (sibling.right == null || !sibling.right.isRed) { //Case 3: Sibling's right child is black, left child is red
                        System.out.println("Case 3: Sibling " + sibling.value + "'s right child is black and left child is red.");
                        sibling.left.isRed = false;
                        sibling.isRed = true;
                        rightRotate(sibling);
                        sibling = x.parent.right;
                    }
                    System.out.println("Case 4: Sibling " + sibling.value + "'s right child is red.");
                    sibling.isRed = x.parent.isRed;
                    x.parent.isRed = false;
                    sibling.right.isRed = false;
                    leftRotate(x.parent);
                    x = root;
                    root.parent = null;
                }
            } else { //Mirror cases: x is the right child
                Node sibling = x.parent.left;
                if (sibling.isRed) { //Case 1: x's sibling is red
                    System.out.println("Case 1 (mirror): Sibling " + sibling.value + " is red.");
                    sibling.isRed = false;
                    x.parent.isRed = true;
                    rightRotate(x.parent);
                    sibling = x.parent.left;
                }
                if ((sibling.left == null || !sibling.left.isRed) && (sibling.right == null || !sibling.right.isRed)) { //Case 2: Both of sibling's children are black
                    System.out.println("Case 2 (mirror): Sibling " + sibling.value + " has two black children.");
                    sibling.isRed = true;
                    x = x.parent;
                } else {
                    if (sibling.left == null || !sibling.left.isRed) { //Case 3: Sibling's left child is black, right child is red
                        System.out.println("Case 3 (mirror): Sibling " + sibling.value + "'s left child is black and right child is red.");
                        sibling.right.isRed = false;
                        sibling.isRed = true;
                        leftRotate(sibling);
                        sibling = x.parent.left;
                    }
                    System.out.println("Case 4 (mirror): Sibling " + sibling.value + "'s left child is red.");
                    sibling.isRed = x.parent.isRed;
                    x.parent.isRed = false;
                    sibling.left.isRed = false;
                    rightRotate(x.parent);
                    x = root;
                    root.parent = null;
                }
            }
        }
        if (x != null) {
            System.out.println("Setting node " + x.value + " to black.");
            x.isRed = false;
        }
        return root;
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