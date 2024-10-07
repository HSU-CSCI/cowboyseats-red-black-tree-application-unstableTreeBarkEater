package edu.hsutx;

/**
 * @author Javidan Aghayev
 * @version 1.0
 * Starting Code for the CSCI-3323 Red-Black Tree assignment
 * Students must complete the TODOs and get the tests to pass
 */

/**
 * A Red-Black Tree that takes int key and String value for each node.
 * Follows the properties of a Red-Black Tree:
 * 1. Every node is either red or black.
 * 2. The root is always black.
 * 3. Every leaf (NIL node) is black.
 * 4. If a node is red, then both its children are black.
 * 5. For each node, all simple paths from the node to descendant leaves have the same number of black nodes.
 */
public class RedBlackTree<E> {
    Node root;
    int size;

    protected class Node {
        public String key;
        public E value;
        public Node left;
        public Node right;
        public Node parent;
        public boolean color; // true = red, false = black

        public Node(String key, E value, Node parent, boolean color) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.left = null;
            this.right = null;
            this.color = color;
        }

        /**
         * returns the depth of the node
         *
         * @return depth
         */
        public int getDepth() {
            // TODO - calculate the depth of the node and return an int value.
            // Hint: follow parent pointers up to the root and count steps
            int depth = 1;
            Node current = this;
            while(current.parent != null){
                current = current.parent;
                depth++;
            }
            return depth;
        }

        public int getBlackDepth() {
            // TODO - calculate the depth of the node counting only black nodes and return an int value
            int depth = 1;
            Node current = this;
            while(current.parent != null){
                if(color == false){
                    current = current.parent;
                    depth++;
                }
            }
            return depth;
        }
    }

    public RedBlackTree() {
        root = null; // Start with an empty tree.  This is the one time we can have a null ptr instead of a null key node
        size = 0;
    }

    public void insert(String key, E value) {
        // TODO - Insert a new node into the tree with key and value
        // You must handle rebalancing the tree after inserting
        // 1. Insert the node as you would in a regular BST.
        // 2. Recolor and rotate to restore Red-Black Tree properties.
        // Make sure to add 1 to size if node is successfully added
        Node z = new Node(key, value, null, true);
        Node y = null;
        Node x = root;

        while (x!= null){
            y = x;
            if (z.key.compareTo(x.key) < 0) x = x.left;
            else x = x.right;
        }
        z.parent = y;
        if (y == null) root = z;
        else if (z.key.compareTo(y.key) < 0) y.left = z;
        else y.right = z;

        z.color = true;
        size++;

        fixInsertion(z);
    }

    public void delete(String key) {
        // TODO - Implement deletion for a Red-Black Tree
        // Will need to handle three cases similar to the Binary Search Tree
        // 1. Node to be deleted has no children
        // 2. Node to be deleted has one child
        // 3. Node to be deleted has two children
        // Additionally, you must handle rebalancing after deletion to restore Red-Black Tree properties
        // make sure to subtract one from size if node is successfully added
        Node z = find(key);

        if(z == null){
            return;
        }

        Node x;
        Node y = z;
        boolean yColor = y.color;

        if(z.left == null){
            x = z.right;
            rbTransplant(z, z.right);
        }

        else if(z.right == null){
            x = z.left;
            rbTransplant(z, z.left);
        }else{
            y = treeMinimum(z.right);
            yColor = y.color;
            x = y.right;
            if(y.parent == z){
                x.parent = y;
            }else{
                rbTransplant(y, y.right);
                y.right = z.right;
                if(y.right != null){
                    y.right.parent = y;
                }
            }
            rbTransplant(z, y);
            y.left = z.left;
            if(y.left != null){
                y.left.parent = y;
            }
            y.color = z.color;
        }
        if(yColor == false){
            fixDeletion(x);
        }

    }

    private Node treeMinimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    private void rbTransplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        }else if (u == u.parent.left) {
            u.parent.left = v;
        }else{
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private void fixInsertion(Node z) {
        // TODO - Implement the fix-up procedure after insertion
        // Ensure that Red-Black Tree properties are maintained (recoloring and rotations).

        while (z.parent.color == true){
            if (z.parent == z.parent.parent.left){
                Node y = z.parent.parent.right; //right uncle;
                y = z.parent.parent.right;
                if (y != null && y.color == true){ //red uncle
                    z.parent.color = false;
                    y.color = false;
                    z.parent.parent.color = true;
                    z = z.parent.parent;      //move up the tree
                }else{
                    if (z == z.parent.right){  // zigzag/triangle
                        z = z.parent;
                        rotateLeft(z);
                    }

                    z.parent.color = false;
                    z.parent.parent.color = true;
                    rotateRight(z.parent.parent);
                }
            }else{
                Node y = z.parent.parent.left; //left uncle
                if (y != null && y.color == true) { //uncle is red
                    //recolor parent and uncle to black and grandpa to red
                    z.parent.color = false;
                    y.color = false;
                    z.parent.parent.color = true;
                    z = z.parent.parent;
                }else{
                    if(z == z.parent.left){ //zigzag/triangle
                        z = z.parent;
                        rotateRight(z);
                    }
                    //straight line
                    z.parent.color = false;
                    z.parent.parent.color = true;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = false;
    }

    private void fixDeletion(Node x) {
        // TODO - Implement the fix-up procedure after deletion
        // Ensure that Red-Black Tree properties are maintained (recoloring and rotations).
        while (x != root && x.color == false){
            if(x == x.parent.left){
                Node w = x.parent.right;
                if(w.color == true){
                    w.color = false;
                    x.parent.color = true;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == false && w.right.color == false){
                    w.color = true;
                    x = x.parent;
                }else{
                    if(w.right.color == false){
                        w.left.color = false;
                        w.color = true;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.right.color = false;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
                Node w = x.parent.left;
                if(w.color == true){
                    w.color = false;
                    x.parent.color = true;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == false && w.left.color == false){
                    w.color = true;
                    x = x.parent;
                }else{
                    if(w.left.color == false){
                        w.right.color = false;
                        w.color = true;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.left.color = false;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = false;
    }



    private void rotateLeft(Node x) { //T, x x is gonna be 'node' in this case
        // TODO - Implement left rotation
        // Left rotation is used to restore balance after insertion or deletion
        Node y = x.right;        //set y
        x.right = y.left;        // turn y's left subtree into x's right subtree
        if (y.left != null){
            y.left.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == null){
            root = y;
        }else if(x == x.parent.left){
            x.parent.left = y;
        }else{
            x.parent.right = y;
        }

        x.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        // TODO - Implement right rotation
        // Right rotation is used to restore balance after insertion or deletion
        Node y = x.left;
        x.left = y.right;
        if(y.right != null){
            y.right.parent = x;
        }

        y.parent = x.parent;

        if(x.parent == null){
            root = y;
        }else if(x == x.parent.right){
            x.parent.right = y;
        }else{
            x.parent.left = y;
        }

        y.right = x;
        x.parent = y;
    }

    Node find(String key) {
        // TODO - Search for the node with the given key
        Node current = root;
        while (current != null && current.value != null ){
            int cmp = key.compareTo(current.key);
            if (cmp == 0) return current;
            else if (cmp < 0) current = current.left;
            else current = current.right;
        }
        return null;
    }

    public E getValue(String key) {
        // TODO - Use find() to locate the node with the given key and return its value
        // If the key does not exist, return null
        Node node = find(key);
        return (node != null) ? node.value : null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    // returns the depth of the node with key, or 0 if it doesn't exist
    public int getDepth(String key) {
        Node node = find(key);
        if (node != null) return node.getDepth();
        return 0;
    }

    // Helper methods to check the color of a node
    private boolean isRed(Node node) {
        return node != null && node.color == true; // Red is true
    }

    private boolean isBlack(Node node) {
        return node == null || node.color == false; // Black is false, and null nodes are black
    }
    public int getSize() {
        return size;
    }

    // Do not alter this method
    public boolean validateRedBlackTree() {
        // Rule 2: Root must be black
        if (root == null) {
            return true; // An empty tree is trivially a valid Red-Black Tree
        }
        if (isRed(root)) {
            return false; // Root must be black
        }

        // Start recursive check from the root
        return validateNode(root, 0, -1);
    }

    // Do not alter this method
    // Helper method to check if the current node maintains Red-Black properties
    private boolean validateNode(Node node, int blackCount, int expectedBlackCount) {
        // Rule 3: Null nodes (leaves) are black
        if (node == null) {
            if (expectedBlackCount == -1) {
                expectedBlackCount = blackCount; // Set the black count for the first path
            }
            return blackCount == expectedBlackCount; // Ensure every path has the same black count
        }

        // Rule 1: Node is either red or black (implicit since we use a boolean color field)

        // Rule 4: If a node is red, its children must be black
        if (isRed(node)) {
            if (isRed(node.left) || isRed(node.right)) {
                return false; // Red node cannot have red children
            }
        } else {
            blackCount++; // Increment black node count on this path
        }

        // Recurse on left and right subtrees, ensuring they maintain the Red-Black properties
        return validateNode(node.left, blackCount, expectedBlackCount) &&
                validateNode(node.right, blackCount, expectedBlackCount);
    }
}
