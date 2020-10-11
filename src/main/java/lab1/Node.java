package lab1;

public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
    Node(T newValue) {
        value = newValue;
    }

    T value;
    Node<T> right;
    Node<T> left;

    @Override
    public int compareTo(Node<T> node) {
        return value.compareTo(node.value);
    }
}
