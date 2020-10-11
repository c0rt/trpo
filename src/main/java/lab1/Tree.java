package lab1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class Tree<T extends Comparable<T>> implements Iterable<T> {
    private final ArrayList<T> binaryTree;

    Tree() {
        binaryTree = new ArrayList<>();
    }

    public T getItem(int index) {
        return binaryTree.get(index);
    }

    public void insert(T newValue) {
        insert(0, newValue);
    }

    // Включение с сохранением порядка
    private void insert(int index, T newValue) {
        try {
            T val = binaryTree.get(index);
            if (val == null) {
                binaryTree.set(index, newValue);
                return;
            }

            if (val.compareTo(newValue) <= 0) {
                insert(2 * (index + 1), newValue);
            } else {
                insert(2 * (index + 1) - 1, newValue);
            }
        } catch (IndexOutOfBoundsException e) {
            while (index >= binaryTree.size()) {
                binaryTree.add(null);
            }
            insert(index, newValue);
        }
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>() {

            private ArrayDeque<T> cache;

            /**
             * Рекурсивный обход дерева слева направо
             * @param index - индекс в массиве
             */
            private void refreshCache(int index) {
                if (index >= binaryTree.size() || binaryTree.get(index) == null)
                    return;

                refreshCache(2 * (index + 1));
                cache.add(binaryTree.get(index));
                refreshCache(2 * (index + 1) - 1);
            }

            private void refreshCache() {
                cache = new ArrayDeque<>();
                refreshCache(0);
            }

            @Override
            public boolean hasNext() {
                if (cache == null) {
                    refreshCache();
                }
                return !cache.isEmpty();
            }

            @Override
            public T next() {
                if (cache == null) {
                    refreshCache();
                }
                T item = cache.getFirst();
                cache.pop();
                return item;
            }
        };
        return it;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (T node : binaryTree) {
            action.accept(node);
        }
    }

    private void balance(ArrayList<T> array, int left, int right) {
        if (left > right) return;

        int middle = (left + right) / 2;

        insert(array.get(middle));

        balance(array, left, middle - 1);
        balance(array, middle + 1, right);
    }

    public void balance() {
        ArrayDeque<T> queue = new ArrayDeque<>();

        for (T node : this) {
            queue.add(node);
        }
        binaryTree.clear();

        ArrayList<T> arr = new ArrayList<>(queue);

        balance(arr, 0, arr.size() - 1);
    }
}

class Main {
    public static void main(String[] args) {
        try {
            Tree<Integer> tree = new Tree<>();

            tree.insert(5);
            tree.insert(6);
            tree.insert(7);
            tree.insert(1);
            tree.insert(2);
            tree.insert(3);
            tree.insert(3);
            tree.insert(5);

            tree.forEach(node -> {
                if (node == null)
                    System.out.println("| null");
                else
                    System.out.println(node.toString());
            });

            System.out.println("------------------------");

            for (Integer integer : tree) {
                System.out.println(integer.toString());
            }

            tree.balance();

            System.out.println("------------------------");

            tree.forEach(node -> {
                if (node == null)
                    System.out.println("| null");
                else
                    System.out.println(node.toString());
            });

        } catch (Exception e) {
            System.out.println("Поймано исключение: " + e.toString());
        }
    }
}