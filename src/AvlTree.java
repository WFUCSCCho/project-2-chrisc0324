// AvlTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// boolean contains( x )  --> Return true if x is present
// boolean remove( x )    --> Return true if x was present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 */
public class AvlTree<AnyType extends Comparable<? super AnyType>> {
    /**
     * Construct the tree.
     */
    public AvlTree( ) {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * @param x the item to insert.
     */
    public void insert( AnyType x ) {
        root = insert( x, root );
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     * @param x the item to remove.
     */
    public void remove( AnyType x ) {
        root = remove( x, root );
    }


    /**
     * Internal method to remove from a subtree.
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> remove( AnyType x, AvlNode<AnyType> t ) {
	    if (t == null) {                                // Base case (empty subtree)
            return null;
        }
        int compareValue = x.compareTo(t.element);      // Compare nodes keys
        if (compareValue < 0) {
            t.left = remove(x, t.left);                 // Recursive call on the left subtree
        }
        else if (compareValue > 0) {
            t.right = remove(x, t.right);               // Recursive call on the right subtree
        }
        else if (t.left != null && t.right != null) {   // Node with 2 children
            AvlNode<AnyType> min = findMin(t.right);    // Get inorder successor (smallest in right subtree)
            t.element = min.element;                    // Replace current node's key with successor's key
            t.right = remove(t.element, t.right);       // Remove the successor node from right subtree
        }
        else {                                          // Only one or no child
            if (t.left == null) {
                t = t.right;                            // Assign right child as successor
            }
            else {
                t = t.left;                             // Assign left child as successor
            }
        }
        return balance(t);                              // Balance tree and return
    }

    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     */
    public AnyType findMin( ) {
        if( isEmpty( ) )
            throw new UnderflowException( );
        return findMin( root ).element;
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     */
    public AnyType findMax( ) {
        if( isEmpty( ) )
            throw new UnderflowException( );
        return findMax( root ).element;
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if x is found.
     */
    public boolean contains( AnyType x ) {
        return contains( x, root );
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty( ) {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( ) {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree( ) {
        if( isEmpty( ) )
            System.out.println( "Empty tree" );
        else
            printTree( root );
    }

    private static final int ALLOWED_IMBALANCE = 1;

    /**
     * Restores balance for the subtree rooted at t.
     * @param t the node that roots the subtree.
     * @return new root of the subtree
     */
    private AvlNode<AnyType> balance( AvlNode<AnyType> t ) {
        if (t == null) {                                                // Empty tree
            return t;
        }
        if (height(t.left) - height(t.right) > ALLOWED_IMBALANCE) {     // Unbalance on the left
            if (height(t.left.left) >= height(t.left.right)) {          // Left-Left case
                t = rotateWithLeftChild(t);                             // Single left rotation
            } else {                                                    // Left-Right case
                t = doubleWithLeftChild(t);                             // Double rotation
            }
        } else if (height(t.right) - height(t.left) > ALLOWED_IMBALANCE) { // Unbalance on the right
            if (height(t.right.right) >= height(t.right.left)) {           // Right-Right case
                t = rotateWithRightChild(t);                               // Single right rotation
            } else {                                                       // Right-Left case
                t = doubleWithRightChild(t);                               // Double rotation
            }
        }
        t.height = Math.max(height(t.left), height(t.right)) + 1;          // Compute height again
        return t;                                                          // Return balanced subtree root
    }

    public void checkBalance( ) {
        checkBalance( root );
    }

    private int checkBalance( AvlNode<AnyType> t ) {
        if( t == null )
            return -1;

        if( t != null ) {
            int hl = checkBalance( t.left );
            int hr = checkBalance( t.right );
            if( Math.abs( height( t.left ) - height( t.right ) ) > 1 ||
                    height( t.left ) != hl || height( t.right ) != hr )
                System.out.println( "OOPS!!" );
        }

        return height( t );
    }


    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> insert( AnyType x, AvlNode<AnyType> t ) {
        if (t == null) {                                    // Root is empty
            return new AvlNode<>(x, null, null);     // Add new node (root)
        }
        int compareValue = x.compareTo(t.element);         // Compare nodes keys
        if (compareValue < 0) {                            // Go left if x is smaller
            t.left = insert(x, t.left);
        } else if (compareValue > 0) {                     // Go right if x is greater
            t.right = insert(x, t.right);
        } else {
            return t;                                      // Ignore duplicates
        }
        return balance(t);                                 // Return root of the balanced subtree
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode<AnyType> findMin( AvlNode<AnyType> t ) {
        if (t == null) {                            // Empty root case
            return null;
        }
        while (t.left != null) {                    // Go far left as possible
            t = t.left;
        }
        return t;                                   // Return the left-most element
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AvlNode<AnyType> findMax( AvlNode<AnyType> t ) {
        if (t == null) {                            // Empty root case
            return null;
        }
        while ( t.right != null ) {                 // Go far right as possible
            t = t.right;
        }
        return t;                                   // Return the right-most element
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return true if x is found in subtree.
     */
    private boolean contains( AnyType x, AvlNode<AnyType> t ) {
        while (t != null) {
            int compareValue = x.compareTo(t.element);      // Compares the keys of the nodes
            if (compareValue < 0) {                         // Go left if x is smaller
                t = t.left;
            } else if (compareValue > 0) {                  // Go right if x is bigger
                t = t.right;
            } else {
                return true;                                // Node found, return true
            }
        }
        return false;                                       // Node not found, return false
    }

    /**
     * Internal method to print a subtree in (sorted) order.
     * @param t the node that roots the tree.
     */
    private void printTree( AvlNode<AnyType> t ) {
        if (t == null) {
            return;
        }
        printTree(t.left);
        System.out.println(t.element);                    // In order print of tree
        printTree(t.right);
    }

    /**
     * Return the height of node t, or -1, if null.
     */
    private int height( AvlNode<AnyType> t ) {
        return t == null ? -1 : t.height;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithLeftChild( AvlNode<AnyType> k2 ) {
        AvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height( k2.left ),height( k2.right )) + 1;
        k1.height = Math.max(height( k1.left ),k2.height) + 1;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithRightChild( AvlNode<AnyType> k1 ) {
        AvlNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(k1.height, height(k2.right)) + 1;
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithLeftChild( AvlNode<AnyType> k3 ) {
	    k3.left = rotateWithRightChild( k3.left );              // Initial rotation with right child of left subtree
        return rotateWithLeftChild( k3 );                       // Second rotation with left child
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithRightChild( AvlNode<AnyType> k1 ) {
	    k1.right = rotateWithLeftChild( k1.right );            // Initial rotation with left child of right subtree
        return rotateWithRightChild( k1 );                     // Second rotation with right child
    }

    private static class AvlNode<AnyType> {
        // Constructors
        AvlNode( AnyType theElement ) {
            this( theElement, null, null );
        }

        AvlNode( AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt ) {
            element  = theElement;
            left     = lt;
            right    = rt;
            height   = 0;
        }

        AnyType           element;      // The data in the node
        AvlNode<AnyType>  left;         // Left child
        AvlNode<AnyType>  right;        // Right child
        int               height;       // Height
    }

    /** The tree root. */
    private AvlNode<AnyType> root;
}
