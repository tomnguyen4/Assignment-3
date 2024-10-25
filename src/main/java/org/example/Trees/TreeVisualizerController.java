package org.example.Trees;

// Author: Abdelnasser Ouda
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TreeVisualizerController {
    private VBox view;
    private ComboBox<String> treeTypeComboBox;
    private TextField inputField;
    private Button insertButton, deleteButton, searchButton, clearButton, inorderTraversalButton; // Added inorderTraversalButton
    private Canvas treeCanvas;
    private TextArea outputArea;
    private List<Integer> keys = new ArrayList<>();

    private Tree<Integer> currentTree;
    private Map<String, Tree<Integer>> trees;

    private Stage stage; // You'll need to set this when creating the controller

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TreeVisualizerController() {
        initializeTrees();
        initializeView();
        setupEventHandlers();
    }

    private void initializeTrees() {
        trees = new HashMap<>();
        trees.put("Binary Search Tree", new BinarySearchTree<>());
        trees.put("Min Heap", new MinHeap<>());
        trees.put("Max Heap", new MaxHeap<>());
        trees.put("Tree2-4", new Tree24<>());
        trees.put("Red-Black", new RedBlack<>());
        currentTree = trees.get("Binary Search Tree");
    }

    private void initializeView() {
        view = new VBox(10);
        view.setPadding(new Insets(10));

        treeTypeComboBox = new ComboBox<>();
        treeTypeComboBox.getItems().addAll(trees.keySet());
        treeTypeComboBox.setValue("Binary Search Tree");

        inputField = new TextField();
        insertButton = new Button("Insert");
        deleteButton = new Button("Delete");
        searchButton = new Button("Search");
        clearButton = new Button("Clear");
        inorderTraversalButton = new Button("In-Order Traversal");  // New button for in-order traversal

        HBox buttonBox = new HBox(10, insertButton, deleteButton, searchButton, clearButton, inorderTraversalButton);

        treeCanvas = new Canvas(1000, 675);

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setMinHeight(100);
        outputArea.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        view.getChildren().addAll(
                new HBox(10, new Label("Tree Type:"), treeTypeComboBox),
                new HBox(10, new Label("Value:"), inputField),
                buttonBox,
                treeCanvas,
                outputArea
        );
    }

    private void setupEventHandlers() {
        insertButton.setOnAction(e -> handleInsert());
        deleteButton.setOnAction(e -> handleDelete());
        searchButton.setOnAction(e -> handleSearch());
        clearButton.setOnAction(e -> handleClear());
        inorderTraversalButton.setOnAction(e -> handleInorderTraversal());
        treeTypeComboBox.setOnAction(e -> handleTreeTypeChange());
    }

    private void handleInorderTraversal() {
        if (currentTree != null) {
            List<Integer> traversalResult = currentTree.inorderTraversal();
            outputArea.appendText("In-Order Traversal: " + traversalResult + "\n");
        } else {
            outputArea.appendText("No tree selected.\n");
        }
    }

    private void handleInsert() {
        try {
            int value = Integer.parseInt(inputField.getText());
            if (!currentTree.contains(value)) {
                currentTree.insert(value);
                updateTreeVisualization();
                outputArea.appendText("Inserted: " + value + "\n");
            } else
                outputArea.appendText("The value " + value + " already in the tree.\n");

        } catch (NumberFormatException ex) {
            outputArea.appendText("Invalid input. Please enter an integer.\n");
        }
    }

    private void handleDelete() {
        try {
            int value = Integer.parseInt(inputField.getText());
            boolean deleted = currentTree.delete(value);
            updateTreeVisualization();
            outputArea.appendText(deleted ? "Deleted: " + value + "\n" : "Value not found: " + value + "\n");
        } catch (NumberFormatException ex) {
            outputArea.appendText("Invalid input. Please enter an integer.\n");
        }
    }

    private void handleSearch() {
        try {
            int value = Integer.parseInt(inputField.getText());
            boolean found = currentTree.contains(value);
            outputArea.appendText(found ? "Found: " + value + "\n" : "Not found: " + value + "\n");
        } catch (NumberFormatException ex) {
            outputArea.appendText("Invalid input. Please enter an integer.\n");
        }
    }

    private void handleClear() {
        currentTree.clear();
        updateTreeVisualization();
        outputArea.appendText("Tree cleared.\n");
    }

    private void handleTreeTypeChange() {
        String selectedType = treeTypeComboBox.getValue();
        currentTree = trees.get(selectedType);
        updateTreeVisualization();
        outputArea.appendText("Switched to " + selectedType + "\n");
    }

    private void updateTreeVisualization() {
        GraphicsContext gc = treeCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, treeCanvas.getWidth(), treeCanvas.getHeight());

        if (currentTree.getRoot() != null) {
            int depth = getTreeDepth(currentTree.getRoot());
            int width = getTreeWidth(currentTree.getRoot());

            double verticalSpacing = (treeCanvas.getHeight()) / (depth + currentTree.size() / 3);
            double horizontalSpacing = treeCanvas.getWidth() / (width + currentTree.size() / 2);

            drawTree(gc, currentTree.getRoot(), treeCanvas.getWidth() / 2, 40, horizontalSpacing, verticalSpacing, width);
        } else {
            outputArea.appendText("Tree is empty or null.\n");
        }
    }

    private void drawTree(GraphicsContext gc, TreeNode<Integer> node, double x, double y, double hSpacing, double vSpacing, int width) {
        if (node == null) return;

        if (currentTree instanceof Tree24) {
            draw24TreeNode(gc, (Tree24<Integer>.Node) node, x, y, hSpacing, vSpacing);  // Pass vSpacing
        } else {
            drawNormalTree(gc, node, x, y, hSpacing, vSpacing, width);
        }
    }


    private void draw24TreeNode(GraphicsContext gc, Tree24<Integer>.Node node, double x, double y, double hSpacing, double vSpacing) {
        double nodeWidth = 40 * node.getValues().size();
        gc.setFill(Color.BROWN);
        gc.fillRect(x - nodeWidth / 2, y - 20, nodeWidth, 40);
        gc.setFill(Color.WHITE);

        List<Integer> values = node.getValues();
        double valueX = x - nodeWidth / 2 + 10;
        for (Integer value : values) {
            gc.fillText(String.valueOf(value), valueX, y + 5);
            valueX += nodeWidth / values.size();
        }

        if (!node.isLeaf()) {
            List<TreeNode<Integer>> children = node.getChildren();
            double childX = x - hSpacing * (children.size() - 1) / 2.0;

            for (int i = 0; i < children.size(); i++) {
                TreeNode<Integer> child = children.get(i);
                gc.strokeLine(x, y + 20, childX, y + 20 + vSpacing);
                drawTree(gc, child, childX, y + vSpacing, hSpacing / 1.5, vSpacing, (int) (nodeWidth / 2));
                childX += hSpacing;
            }
        }
    }




    private void drawNormalTree(GraphicsContext gc, TreeNode<Integer> node, double x, double y, double hSpacing, double vSpacing, int width) {
        if (currentTree.type().equals("RBT")) {
            gc.setFill(node.getColor().equals("RED") ? Color.RED : Color.BLACK);
        } else {
            gc.setFill(currentTree.color());
        }

        gc.fillOval(x - 15, y - 15, 40, 40);
        gc.setFill(Color.GHOSTWHITE);
        gc.fillText(node.getValue().toString(), x - 10, y + 10);

        if (node.getLeft() != null) {
            int leftWidth = getTreeWidth(node.getLeft());
            double newX = x - (width - leftWidth / 3) * hSpacing / 3;
            double newY = y + vSpacing;
            gc.strokeLine(x + 5, y + 24, newX, newY);
            drawNormalTree(gc, node.getLeft(), newX, newY, hSpacing, vSpacing, leftWidth);
        }

        if (node.getRight() != null) {
            int rightWidth = getTreeWidth(node.getRight());
            double newX = x + (width - rightWidth / 3) * hSpacing / 3;
            double newY = y + vSpacing;
            gc.strokeLine(x + 5, y + 24, newX, newY);
            drawNormalTree(gc, node.getRight(), newX, newY, hSpacing, vSpacing, rightWidth);
        }
    }

    private int getTreeDepth(TreeNode<Integer> node) {
        if (node == null) return 0;
        return 1 + Math.max(getTreeDepth(node.getLeft()), getTreeDepth(node.getRight()));
    }

    private int getTreeWidth(TreeNode<Integer> node) {
        if (node == null) return 0;
        if (node.getLeft() == null && node.getRight() == null) return 1;
        return getTreeWidth(node.getLeft()) + getTreeWidth(node.getRight());
    }

    public VBox getView() {
        return view;
    }

    public void saveTree() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Tree");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tree Files", "*.tree"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                outputArea.appendText("Tree before saving" + currentTree.inorderTraversal() + ".\n");
                out.writeObject(currentTree);
                outputArea.appendText("Tree saved successfully.\n");
            } catch (IOException e) {
                outputArea.appendText("Error saving tree: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }

    public void loadTree() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Tree");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tree Files", "*.tree"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                Object loadedObject = in.readObject();

                if (!(loadedObject instanceof Tree)) {
                    outputArea.appendText("Error: Loaded object is not a valid Tree.\n");
                    return;
                }

                @SuppressWarnings("unchecked")
                Tree<Integer> loadedTree = (Tree<Integer>) loadedObject;

                String treeType = determineTreeType(loadedTree);
                if (treeType == null) {
                    outputArea.appendText("Error: Unknown tree type.\n");
                    return;
                }

                treeTypeComboBox.setValue(treeType);
                currentTree = loadedTree;

                outputArea.appendText("Tree loaded successfully.\n");
                outputArea.appendText("Tree type: " + currentTree.type() + "\n");
                outputArea.appendText("Tree size: " + currentTree.size() + "\n");
                outputArea.appendText("Tree contents: " + currentTree.inorderTraversal() + "\n");

                updateTreeVisualization();
            } catch (IOException | ClassNotFoundException e) {
                outputArea.appendText("Error loading tree: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }

    private String determineTreeType(Tree<?> tree) {
        return currentTree.type();
    }
}
