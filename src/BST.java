/**
 * @file: BST.java
 * @description: This class defines a binary search tree with different methods such as insert, search, remove, and iteration.
 * In-order traversal is used for iteration.
 * @author: Chris Cha {@literal <chah22@wfu.edu>}
 * @date: September 17, 2025
 */
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BST<E extends Comparable<? super E>> implements Iterable<E> {
    private Node<E> root;
    private int size = 0;

    /* constructs an empty binary search tree */
    BST() {
        root = null;
        size = 0;
    }

    /* clears the binary search tree */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Inserts a new node to the binary search tree if it does not already exist
     *
     * @param element the element that the new inserted node will have.
     */
    public void insert(E element) {
        Node<E> current = root;
        if (root == null) {
            root = new Node<>(element);
            size++;
        }
        else {
            if (search(element) != null) {
                return;
            }
            boolean isPlaced = false;
            while (!isPlaced) {
                if (element.compareTo(current.getElement()) < 0) {
                    if (current.getLeft() == null) {
                        current.setLeft(new Node<>(element));
                        size++;
                        isPlaced = true;
                    }
                    current = current.getLeft();
                }
                else if (element.compareTo(current.getElement()) > 0) {
                    if (current.getRight() == null) {
                        current.setRight(new Node<>(element));
                        size++;
                        isPlaced = true;
                    }
                    current = current.getRight();
                }
                else {
                    isPlaced = true;
                }
            }
        }
    }

    /**
     * Searches for a node in the BST with the given element
     *
     * @param element the element to search for.
     * @return The node containing the given element (null if it does not exist).
     */
    public Node<E> search(E element) {
        if (root == null) {
            return null;
        }
        Node<E> current = root;
        while (current != null) {
            if (element.compareTo(current.getElement()) < 0) {
                current = current.getLeft();
            }
            else if (element.compareTo(current.getElement()) > 0) {
                current = current.getRight();
            }
            else {
                return current;
            }
        }
        return null;
    }

    /**
     * Removes the node in the binary search tree with the given element
     *
     * @param element the element that should be removed.
     * @return the removed node (null if it does not exist)
     */
    public Node<E> remove(E element) {
        Node<E> parent = null;
        Node<E> current = root;
            while (current != null) {
                if (element.compareTo(current.getElement()) < 0) {
                    parent = current;
                    current = current.getLeft();
                }
                else if (element.compareTo(current.getElement()) > 0) {
                    parent = current;
                    current = current.getRight();
                }
                else {
                    if (current.getLeft() != null && current.getRight() != null) {
                        Node<E> successorParent = current;
                        Node<E> successor = current.getRight();
                        while (successor.getLeft() != null) {
                            successorParent = successor;
                            successor = successor.getLeft();
                        }
                        current.setElement(successor.getElement());
                        parent = successorParent;
                        current = successor;
                    }

                    Node<E> child = null;
                    if (current.getLeft() != null) {
                        child = current.getLeft();
                    }
                    else if (current.getRight() != null) {
                        child = current.getRight();
                    }

                    if (parent == null) {
                        root = child;
                    }
                    else if (parent.getLeft() == current) {
                        parent.setLeft(child);
                    }
                    else {
                        parent.setRight(child);
                    }
                    size--;
                    return current;
                }
            }
        return null;
    }

    /**
     * Iterator that traverses in (in-order) order
     *
     * @return An iterator that visits each element in in-order
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Stack<Node<E>> stack = new Stack<>();
            private Node<E> current = root;

            @Override
            public boolean hasNext() {
                return (current != null || !stack.isEmpty());
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                while (current != null) {
                    stack.push(current);
                    current = current.getLeft();
                }

                Node<E> output = stack.pop();
                E value = output.getElement();
                current = output.getRight();
                return value;
            }
        };
    }
}
