import java.util.ArrayList;
import java.util.List;

public class Cola {
    private List<Object> elementos;

    public Cola() {
        elementos = new ArrayList<>();
    }

    public void enqueue(Object elemento) {
        elementos.add(elemento);
    }

    public Object dequeue() {
        if (isEmpty()) {
            return null; // Manejar cola vacía si es necesario
        }
        return elementos.remove(0);
    }

    public boolean isEmpty() {
        return elementos.isEmpty();
    }

    @Override
    public String toString() {
        return elementos.toString(); // Devuelve una cadena que representa los elementos de la cola
    }
}
