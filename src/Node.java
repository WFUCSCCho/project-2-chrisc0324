/**
 * @file: Node.java
 * @description: This class defines the node used inside the binary search tree. A node stores a generic element that
 * is comparable and references to its left and right child. It also contains method to modify and access node components.
 * @author: Chris Cha {@literal <chah22@wfu.edu>}
 * @date: September 17, 2025
 */
public class Node<E extends Comparable<? super E>> implements Comparable<Node<E>> {
    private E element;
    private Node<E> left;
    private Node<E> right;

    /* Constructor (creates node with given element) */
    public Node(E element) {
        this.element = element;
        left = null;
        right = null;
    }

    /* Constructor (creates node with given element and left and right references) */
    public Node(E element, Node<E> left, Node<E> right) {
        this.element = element;
        this.left = left;
        this.right = right;
    }

    /* returns element stored in the node */
    public E getElement() {
        return element;
    }

    /* sets the element of the node with the given input */
    public void setElement(E element) {
        this.element = element;
    }

    /* returns the left child of the node */
    public Node<E> getLeft() {
        return left;
    }

    /* sets the left child of the node with the input node */
    public void setLeft(Node<E> left) {
        this.left = left;
    }

    /* returns the right child of the node */
    public Node<E> getRight() {
        return right;
    }

    /* sets the right child of the node with the input node */
    public void setRight(Node<E> right) {
        this.right = right;
    }

    /* checks whether the node has a left child */
    public boolean hasLeft() {
        return left != null;
    }

    /* checks whether the node has a right child */
    public boolean hasRight() {
        return right != null;
    }

    /* checks whether the node has no children */
    public boolean isLeaf() {
        return left == null && right == null;
    }

    /* compares the node according to their element */
    @Override
    public int compareTo(Node<E> other) {
        return this.element.compareTo(other.element);
    }
}
