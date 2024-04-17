public class Pila {
    private Node top;

    public Pila() {
        top = null;
    }

    public void push(Object data) {
        Node newNode = new Node(data);
        newNode.next = top;
        top = newNode;
    }

    public Object pop() {
        if (isEmpty())
            throw new IllegalStateException("La pila está vacía");
        Object data = top.data;
        top = top.next;
        return data;
    }

    public Object top() {
        if (isEmpty())
            throw new IllegalStateException("La pila está vacía");
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void Mostrar() {
        Node temp = top;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    static class Node {
        Object data;
        Node next;

        Node(Object data) {
            this.data = data;
            next = null;
        }
    }
}
