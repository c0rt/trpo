package lab1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Tree<T extends Comparable<T>> implements Iterable<T> {
    private ArrayList<T> binaryTree;

    Tree() {
        binaryTree = new ArrayList<>();
    }

    public T getValue(int index) {
        return binaryTree.get(index);
    }

    /**
     * Вставка через корень
     *
     * @param newValue - значение нового элемента
     */
    public void insert(T newValue) {
        insert(0, newValue);
    }

    /**
     * Вставка с сохранением порядка
     *
     * @param index    - индекс массива для вставки
     * @param newValue - новое значение
     */
    private void insert(int index, T newValue) {
        try {
            T val = binaryTree.get(index);
            if (val == null) {
                binaryTree.set(index, newValue);
                return;
            }

            if (val.compareTo(newValue) <= 0) {
                insert(getRightIndex(index), newValue);
            } else {
                insert(getLeftIndex(index), newValue);
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

                refreshCache(getRightIndex(index));
                cache.add(binaryTree.get(index));
                refreshCache(getLeftIndex(index));
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

    private boolean nodeExists(int index) {
        return binaryTree.size() > index && binaryTree.get(index) != null;
    }

    public void remove(int index) {
        if (!nodeExists(index)) return;

        boolean rightExists = nodeExists(getRightIndex(index));
        boolean leftExists = nodeExists(getLeftIndex(index));

        if (!rightExists || !leftExists) {
            join(index);
        }

        if (rightExists && leftExists) {
            int next = next(index);
            binaryTree.set(index, getValue(next));
            join(next(index));
        }
    }

    private int getRightIndex(int index) {
        return 2 * (index + 1);
    }

    private int getLeftIndex(int index) {
        return 2 * (index + 1) - 1;
    }

    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    public int minimum(int index) {
        int left = getLeftIndex(index);
        if (!nodeExists(left))
            return index;
        else return minimum(left);
    }

    /**
     * Получить следующий элемент по его значению
     */
    public int next(int index) {
        int right = getRightIndex(index);
        if (nodeExists(right))
            return minimum(right);

        int parent = getParentIndex(index);
        while (nodeExists(parent) && (index == getRightIndex(parent))) {
            index = parent;
            parent = getParentIndex(parent);
        }
        return parent;
    }

    public void join(int index) {
        int leftIndex = getLeftIndex(index);
        int rightIndex = getRightIndex(index);

        boolean leftExists = nodeExists(leftIndex);
        boolean rightExists = nodeExists(rightIndex);

        if (leftExists) {
            binaryTree.set(index, binaryTree.get(leftIndex));
            join(leftIndex);
        }

        if (rightExists) {
            binaryTree.set(index, binaryTree.get(rightIndex));
            join(rightIndex);
        }

        if (!leftExists && !rightExists) {
            binaryTree.remove(index);
        }
    }
}

class Main {
    public static void printTree(Tree<String> tree) {
        tree.forEach(node -> {
            if (node == null)
                System.out.println("| null");
            else
                System.out.println(node);
        });
    }

    public static void main(String[] args) {
        try {
            String symbols = "abcdefghijklmnopqrstuvwxyz";
            Tree<String> tree = new Tree<>();
            for (int i = 0; i < 10; i++) {
                tree.insert(new Random().ints(3, i, symbols.length())
                        .mapToObj(symbols::charAt)
                        .map(Object::toString)
                        .collect(Collectors.joining()));
            }

            System.out.println("Вывод сгенерированных эелементов массива посредством иетратора forEach:");
            printTree(tree);

            System.out.println("------------------------");
            System.out.println("Вывод сгенерированных эелементов массива посредством итераторов и спользованием цикла for:");
            for (String value : tree) {
                System.out.println(value);
            }

            System.out.println("------------------------");
            System.out.println("Добавление строки \"asd\":");
            tree.insert("asd");
            printTree(tree);

            System.out.println("------------------------");
            System.out.println("Полчение значения элемента по индексу 2:");
            System.out.println(tree.getValue(2));

            System.out.println("------------------------");
            System.out.println("Удаление элемента по индексу 2:");
            tree.remove(2);
            printTree(tree);

            System.out.println("------------------------");
            System.out.println("Балансировка дерева:");
            tree.balance();
            printTree(tree);

            System.out.println("------------------------");

        } catch (NullPointerException e) {
            System.out.println("Поймано исключение NullPointerException: " + e.toString());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Поймано исключение IndexOutOfBoundsException: " + e.toString());
        } catch (Exception e) {
            System.out.println("Поймано исключение: " + e.toString());
        }
    }
}