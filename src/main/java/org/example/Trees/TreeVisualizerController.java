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
    private VBox view;  // Main container for UI components
    private ComboBox<String> treeTypeComboBox;  // Dropdown to select tree type
    private TextField inputField;
    private Button insertButton, deleteButton, searchButton, clearButton, inorderTraversalButton; // Action buttons
    private Canvas treeCanvas;  // Canvas for tree visualization
    private TextArea outputArea;  // Displays outputs and messages
    private List<Integer> keys = new ArrayList<>();

    private Tree<Integer> currentTree;  // Reference to the currently selected tree
    private Map<String, Tree<Integer>> trees;  // Map to hold different tree instances

    private Stage stage; // Stage required for file chooser dialogs

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Constructor: Initializes trees, UI components, and event handlers
    public TreeVisualizerController() {
        initializeTrees();  // Setup tree types
        initializeView();   // Setup UI components
        setupEventHandlers();  // Assign actions to buttons
    }

    private void initializeTrees() {
        trees = new HashMap<>();
        trees.put("Binary Search Tree", new BinarySearchTree<>());  // Initialize tree types
        trees.put("Red Black Tree", new RedBlack<>());
        trees.put("Min Heap", new MinHeap<>());
        trees.put("Max Heap", new MaxHeap<>());
        currentTree = trees.get("Binary Search Tree");  // Set default tree
    }

    private void initializeView() {
        view = new VBox(10);
        view.setPadding(new Insets(10));

        // Set up ComboBox for tree type selection
        treeTypeComboBox = new ComboBox<>();
        treeTypeComboBox.getItems().addAll(trees.keySet());
        treeTypeComboBox.setValue("Binary Search Tree");

        // Initialize input field and buttons
        inputField = new TextField();
        insertButton = new Button("Insert");
        deleteButton = new Button("Delete");
        searchButton = new Button("Search");
        clearButton = new Button("Clear");
        inorderTraversalButton = new Button("In-Order Traversal");  // New button for in-order traversal

        // Arrange buttons in an HBox
        HBox buttonBox = new HBox(10, insertButton, deleteButton, searchButton, clearButton, inorderTraversalButton);

        // Canvas for drawing the tree
        treeCanvas = new Canvas(1000, 675);

        // Output area for displaying messages and traversal results
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setMinHeight(100);
        outputArea.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        // Add all components to the view
        view.getChildren().addAll(
                new HBox(10, new Label("Tree Type:"), treeTypeComboBox),
                new HBox(10, new Label("Value:"), inputField),
                buttonBox,
                treeCanvas,
                outputArea
        );
    }

    private void setupEventHandlers() {
        // Assign actions to buttons
        insertButton.setOnAction(e -> handleInsert());  // Action for insert
        deleteButton.setOnAction(e -> handleDelete());  // Action for delete
        searchButton.setOnAction(e -> handleSearch());  // Action for search
        clearButton.setOnAction(e -> handleClear());    // Action for clear
        inorderTraversalButton.setOnAction(e -> handleInorderTraversal());  // Action for in-order traversal
        treeTypeComboBox.setOnAction(e -> handleTreeTypeChange());  // Action for changing tree type
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
                updateTreeVisualization();  // Refresh tree view after insert
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
            updateTreeVisualization();  // Refresh tree view after delete
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
        gc.clearRect(0, 0, treeCanvas.getWidth(), treeCanvas.getHeight());  // Clear previous drawing

        if (currentTree.getRoot() != null) {
            int depth = getTreeDepth(currentTree.getRoot());
            int width = getTreeWidth(currentTree.getRoot());

            // Calculate spacing based on tree size and depth
            double verticalSpacing = (treeCanvas.getHeight()) / (depth + currentTree.size() / 3);
            double horizontalSpacing = treeCanvas.getWidth() / (width + currentTree.size() / 2);

            drawTree(gc, currentTree.getRoot(), treeCanvas.getWidth() / 2, 40, horizontalSpacing, verticalSpacing, width);
        } else {
            outputArea.appendText("Tree is empty or null.\n");
        }
    }

    private void drawTree(GraphicsContext gc, TreeNode<Integer> node, double x, double y, double hSpacing, double vSpacing, int width) {
        if (node == null) return;
        drawNormalTree(gc, currentTree.getRoot(), treeCanvas.getWidth() / 2, 40, hSpacing, vSpacing, width);
    }

    private void drawNormalTree(GraphicsContext gc, TreeNode<Integer> node, double x, double y, double hSpacing, double vSpacing, int width) {
        // Set color based on tree type
        if (currentTree.type() == "RBT")
            if (node.getColor() == "RED")
                gc.setFill(Color.RED);
            else
                gc.setFill(Color.BLACK);
        else
            gc.setFill(currentTree.color());

        gc.fillOval(x - 15, y - 15, 40, 40);
        gc.setFill(Color.GHOSTWHITE);
        gc.fillText(node.getValue().toString(), x - 10, y + 10);

        // Draw left subtree
        if (node.getLeft() != null) {
            int leftWidth = getTreeWidth(node.getLeft());
            double newX = x - (width - leftWidth / 3) * hSpacing / 3;
            double newY = y + vSpacing;
            gc.strokeLine(x + 5, y + 24, newX, newY);
            drawNormalTree(gc, node.getLeft(), newX, newY, hSpacing, vSpacing, leftWidth);
        }

        // Draw right subtree
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
                outputArea.appendText("Tree before saving: " + currentTree.inorderTraversal() + "\n");
                out.writeObject(currentTree);  // Serialize the current tree
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
                currentTree = loadedTree;  // Set the loaded tree as the current tree
                updateTreeVisualization();
                outputArea.appendText("Tree loaded successfully.\n");
            } catch (IOException | ClassNotFoundException e) {
                outputArea.appendText("Error loading tree: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }
    }
}
