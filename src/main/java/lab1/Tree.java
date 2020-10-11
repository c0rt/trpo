package lab1;

public class Tree<T extends Comparable<T>> {
    Node<T> root;
    T value;

    public void add(T value) {
        Node<T> node = new Node<>(value);
        root = add(root, node);
    }

    private Node<T> add(Node<T> top, Node<T> node) {
        if (top == null) {
            return node;
        }
        if (top.compareTo(node) <= 0) {
            top.right = add(top.right, node);
        } else {
            top.left = add(top.left, node);
        }
        return top;
    }
}

class Main {
    public static void main(String[] args) {
        Tree<Integer> tree = new Tree<>();
        tree.add(3);
        tree.add(3);
        tree.add(2);
        tree.add(1);
        tree.add(4);
        tree.add(5);
        tree.add(6);
    }
}