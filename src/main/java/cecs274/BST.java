package cecs274;

import java.util.*;

/**
 * A binary search tree (BST) that contains <code>Comparable</code> objects.
 */
public class BST {

    private Node root;

    /**
     * The class for nodes in the tree. Although its instance variables are
     * private, they can still be read by methods in the containing class.
     */
    private static class Node {
        private Comparable data;
        private Node left, right;

        /**
         * The general constructor that builds a Node with the specified
         * data and left and right children.
         *
         * @param data  The data value stored in the new Node.
         * @param left  The new Node's left child.
         * @param right The new Node's right child.
         */
        public Node(Comparable data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        /**
         * The constructor that simply provides the data to be stored, making
         * the left and right children null.
         *
         * @param data The data value stored in the new Node.
         */
        public Node(Comparable data) {
            this(data, null, null);
        }
    }


    /**
     * Create a new BST that is empty. Data are only put into a BST by
     * calling insert().
     */
    public BST() {
        root = null;
    }

    /**
     * Insert data into this BST. Silently ignore duplicate data.
     *
     * @param data The value to be inserted.
     */
    public void insert(Comparable data) {
        root = insertHelper(root, data);
    }

    /**
     * Return the BST rooted at root, modified by the insertion of data.
     *
     * @param root The root of the tree to receive data.
     * @param data The value to be inserted.
     * @return The root of the modified BST.
     */


    private static Node insertHelper(Node root, Comparable data) {
        if (root == null) {
            return new Node(data);
        } else {
            int comparisonResult = data.compareTo(root.data);
            if (comparisonResult < 0) {
                root.left = insertHelper(root.left, data);
                return root;
            } else {
                root.right = insertHelper(root.right, data);
                return root;
            }
        }
    }

    /**
     * Print a representation of this BST on System.out.
     */
    public void print() {
        printHelper(root, "");
    }

    /**
     * Print the BST rooted at root, with indent preceding all output lines.
     * The nodes are printed in in-order.
     *
     * @param root   The root of the tree to be printed.
     * @param indent The string to go before output lines.
     */
    private static void printHelper(Node root, String indent) {
        if (root == null) {
            System.out.println(indent + "null");
            return;
        }

        // Pick a pretty indent.
        String newIndent;
        if (indent.equals("")) {
            newIndent = ".. ";
        } else {
            newIndent = "..." + indent;
        }

        printHelper(root.left, newIndent);
        System.out.println(indent + root.data);
        printHelper(root.right, newIndent);
    }

    /**
     * return height of BST
     * @return height of BST
     */
    public int height() {
        return height(root);
    }

    /**
     * Helper function for height()
     * @param node root of the tree to count for height
     * @return height ot curren subtree
     */
    private static int height(Node node) {
        if (node == null) {
            return 0;
        }
        else {
            return 1 + Math.max(height(node.left), height(node.right));
        }
    }

    /**
     * check whether this BST is balanced
     * @return boolean value for balance
     */
    public boolean isBalanced() {
        return isBalanced(root);
    }

    /**
     * Helper recursive function for isBalance()
     * @param node root of the tree to check
     * @return
     */
    private static boolean isBalanced(Node node) {
        if (node == null) {
            return true;
        }
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);

        return isBalanced(node.left) && isBalanced(node.right) && Math.abs(leftHeight - rightHeight) <= 1;
    }

    /**
     * Get the list of BST, according to breadth first traversil
     * @return list of nodes of BST
     */
    public List<Object> breadthFirstTraversal() {
        List<Object> result = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        if (root != null) {
            queue.add(root);
        }
        while (!queue.isEmpty()) {
            Node current = queue.remove();
            result.add(current.data);
            if (current.left != null) {
                queue.add(current.left);
            }
            if (current.right != null) {
                queue.add(current.right);
            }
        }
        return result;
    }

    /**
     * recreate balanced BST from unbalanced one
     */
    public void buildBalancedBST() {
        List<Node> sortedNodes = sortedNodesHelper(root);
        root = buildTreeHelper(sortedNodes, root, 0, sortedNodes.size() - 1);
    }

    /**
     * Inorder traversal to get nodes of BST
     * @param node root of tree to start adding
     * @return list of node in a in-order traversal
     */
    private static List<Node> sortedNodesHelper(Node node) {
        ArrayList<Node> result = new ArrayList<>();
        if (node == null) {
            return null;
        }
        if (node.left != null) {
            result.addAll(sortedNodesHelper(node.left));
        }
        result.add(node);
        if (node.right != null) {
            result.addAll(sortedNodesHelper(node.right));
        }
        return result;
    }

    /**
     * Build balanced tree from a sorted list
     * @param nodes list of sorted nodes
     * @param node root of tree to start recreating
     * @param start start position in list
     * @param end end position in list
     * @return
     */
    private Node buildTreeHelper(List<Node> nodes, Node node, int start, int end) {
        if (start > end) {
            return null;
        }
        int mid = (start + end) / 2;
        node = nodes.get(mid);
        node.left = buildTreeHelper(nodes, node.left, start, mid - 1);
        node.right = buildTreeHelper(nodes, node.right, mid + 1, end);
        return node;
    }

    /**
     * Return a list from a BST within [lowerBound, upperBound]
     * @param lowerBound lower bound of range
     * @param upperBound upper bound of range
     * @return
     */
    public List<Comparable> getRange(Comparable lowerBound, Comparable upperBound) {
        List<Comparable> result;
        if (lowerBound.compareTo(upperBound) >= 0) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            throw new NoSuchElementException();
        }
        else {
            result = getRangeHelper(root, lowerBound, upperBound);
        }
        return result;
    }

    /**
     * Helper function for getRange()
     * @param node root of tree to start getting range
     * @param lowerBound lower bound of range
     * @param upperBound upper bound of range
     * @return
     */
    private static List<Comparable> getRangeHelper(Node node, Comparable lowerBound, Comparable upperBound) {
        List<Comparable> result = new ArrayList<>();
        if (node.data.compareTo(lowerBound) > 0 && node.left != null) {
            result.addAll(getRangeHelper(node.left, lowerBound, upperBound));
        }
        if (node.data.compareTo(lowerBound) >= 0 && node.data.compareTo(upperBound) <= 0) {
            result.add(node.data);
        }
        if (node.data.compareTo(upperBound) < 0 && node.right != null) {
            result.addAll(getRangeHelper(node.right, lowerBound, upperBound));
        }
        return result;
    }

    /**
     * search for specific node in the tree
     * @param data data to searched for
     * @return Comparable that needed to find
     */
    public Comparable search(Comparable data) {
        if (root == null) {
            return null;
        }
        else {
            return searchHelper(root, data);
        }
    }

    /**
     * Helper function for search()
     * @param node root of tree to start search
     * @param data data to be searched for
     * @return Comparable
     */
    private static Comparable searchHelper(Node node, Comparable data) {
        Comparable result = null;
        int comparisonResult = node.data.compareTo(data);
        if (comparisonResult == 0) {
            result = node.data;
        }
        else if (comparisonResult > 0 && node.left != null) {
            result = searchHelper(node.left, data);
        }
        else if (comparisonResult < 0 && node.right != null) {
            result = searchHelper(node.right, data);
        }
        return result;
    }

    /**
     * Remove a specific Comparable in BST
     * @param obj Comparable to be removed
     */
    public void remove(Comparable obj) {
        Node toBeRemoved = root;
        Node parent = null;
        boolean found = false;
        while (!found && toBeRemoved != null) {
            int d = toBeRemoved.data.compareTo(obj);
            if (d == 0) {
                found = true;
            }
            else {
                parent = toBeRemoved;
                if (d > 0) {
                    toBeRemoved = toBeRemoved.left;
                }
                else {
                    toBeRemoved = toBeRemoved.right;
                }
            }
        }
        if (!found) {
            return;
        }

        if (toBeRemoved.left == null || toBeRemoved.right == null) {
            Node newChild;
            if (toBeRemoved.left == null) {
                newChild = toBeRemoved.right;
            }
            else {
                newChild = toBeRemoved.left;
            }
            if (parent == null) {
                root = newChild;
            }
            else if (parent.left == toBeRemoved) {
                parent.left = newChild;
            }
            else {
                parent.right = newChild;
            }
            return;

        }
        Node smallestParent = toBeRemoved;
        Node smallest = toBeRemoved.right;
        while (smallest.left != null) {
            smallestParent = smallest;
            smallest = smallest.left;
        }


        toBeRemoved.data = smallest.data;
        if (smallestParent == toBeRemoved) {
            smallestParent.right = smallest.right;
        }
        else {
            smallestParent.left = smallest.right;
        }
    }


}