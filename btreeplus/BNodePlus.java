package btreeplus;

import java.util.ArrayList;

/**
 * Nodo para B+ Tree híbrido
 * - Nodos internos usan ArrayList para claves e hijos
 * - Nodos hoja usan ListaEnlazada para almacenar datos
 */
public class BNodePlus<E extends Comparable<E>> {
    protected ArrayList<E> keys;           // Claves del nodo
    protected ArrayList<BNodePlus<E>> children; // Hijos (solo para nodos internos)
    protected ListaEnlazada<E> data;       // Datos (solo para nodos hoja)
    protected BNodePlus<E> next;           // Puntero al siguiente nodo hoja
    protected int count;                   // Número de claves actuales
    protected boolean isLeaf;              // Indica si es nodo hoja
    protected int idNode;                  // ID único del nodo
    private static int nextId = 1;

    /**
     * Constructor para nodo interno
     */
    public BNodePlus(int orden, boolean isLeaf) {
        this.isLeaf = isLeaf;
        this.count = 0;
        this.idNode = nextId++;
        this.next = null;

        if (isLeaf) {
            // Nodo hoja: solo claves y datos
            this.keys = new ArrayList<>(orden - 1);
            this.children = null;
            this.data = new ListaEnlazada<>();

            // Inicializar claves para nodo hoja
            for (int i = 0; i < orden - 1; i++) {
                this.keys.add(null);
            }
        } else {
            // Nodo interno: claves e hijos
            this.keys = new ArrayList<>(orden - 1);
            this.children = new ArrayList<>(orden);
            this.data = null;

            // Inicializar claves
            for (int i = 0; i < orden - 1; i++) {
                this.keys.add(null);
            }

            // Inicializar hijos
            for (int i = 0; i < orden; i++) {
                this.children.add(null);
            }
        }
    }

    /**
     * Verifica si el nodo está lleno
     */
    public boolean isFull(int orden) {
        return this.count == orden - 1;
    }

    /**
     * Verifica si el nodo tiene underflow
     */
    public boolean hasUnderflow(int orden) {
        if (isLeaf) {
            return this.count < (orden - 1) / 2;
        } else {
            return this.count < (orden - 1) / 2;
        }
    }

    /**
     * Busca una clave en el nodo y devuelve su posición
     */
    public boolean searchNode(E key, int[] pos) {
        if (key == null) {
            pos[0] = -1;
            return false;
        }

        pos[0] = 0;
        while (pos[0] < this.count && this.keys.get(pos[0]).compareTo(key) < 0) {
            pos[0]++;
        }

        // En nodos hoja, verificamos igualdad exacta
        if (isLeaf && pos[0] < this.count) {
            return this.keys.get(pos[0]).equals(key);
        }

        return false; // Para nodos internos, no hay búsqueda exacta
    }

    /**
     * Inserta una clave en un nodo hoja
     */
    public boolean insertInLeaf(E key) {
        if (!isLeaf || key == null || isFull(this.keys.size() + 1)) {
            return false;
        }

        int[] pos = new int[1];
        if (searchNode(key, pos)) {
            return false; // Clave duplicada
        }

        // Desplazar claves hacia la derecha
        for (int i = this.count; i > pos[0]; i--) {
            this.keys.set(i, this.keys.get(i - 1));
        }

        // Insertar nueva clave
        this.keys.set(pos[0], key);
        this.count++;

        // Agregar a la lista enlazada de datos
        try {
            this.data.insertLast(key);
        } catch (LinkedList.MensajeException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    /**
     * Inserta una clave en un nodo interno
     */
    public boolean insertInInternal(E key, BNodePlus<E> rightChild, int position) {
        if (isLeaf || isFull(this.keys.size() + 1)) {
            return false;
        }

        // Desplazar claves e hijos hacia la derecha
        for (int i = this.count; i > position; i--) {
            this.keys.set(i, this.keys.get(i - 1));
            this.children.set(i + 1, this.children.get(i));
        }

        // Insertar nueva clave e hijo
        this.keys.set(position, key);
        this.children.set(position + 1, rightChild);
        this.count++;

        return true;
    }

    /**
     * Elimina una clave de un nodo hoja
     */
    public boolean removeFromLeaf(E key) {
        if (!isLeaf) {
            return false;
        }

        int[] pos = new int[1];
        if (!searchNode(key, pos)) {
            return false; // Clave no encontrada
        }

        // Desplazar claves hacia la izquierda
        for (int i = pos[0]; i < this.count - 1; i++) {
            this.keys.set(i, this.keys.get(i + 1));
        }

        // Limpiar última posición
        this.keys.set(this.count - 1, null);
        this.count--;

        // Eliminar de la lista enlazada
        try {
            this.data.removeNode(key);
        } catch (LinkedList.MensajeException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    /**
     * Obtiene todas las claves del nodo hoja en orden
     */
    public ArrayList<E> getAllLeafKeys() {
        ArrayList<E> result = new ArrayList<>();
        if (isLeaf) {
            for (int i = 0; i < this.count; i++) {
                result.add(this.keys.get(i));
            }
        }
        return result;
    }

    // Getters y Setters
    public E getKey(int index) {
        if (index >= 0 && index < this.count) {
            return this.keys.get(index);
        }
        return null;
    }

    public void setKey(int index, E key) {
        if (index >= 0 && index < this.keys.size()) {
            this.keys.set(index, key);
        }
    }

    public BNodePlus<E> getChild(int index) {
        if (!isLeaf && index >= 0 && index < this.children.size()) {
            return this.children.get(index);
        }
        return null;
    }

    public void setChild(int index, BNodePlus<E> child) {
        if (!isLeaf && index >= 0 && index < this.children.size()) {
            this.children.set(index, child);
        }
    }

    public ListaEnlazada<E> getData() {
        return this.data;
    }

    public BNodePlus<E> getNext() {
        return this.next;
    }

    public void setNext(BNodePlus<E> next) {
        this.next = next;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isLeaf() {
        return this.isLeaf;
    }

    public int getIdNode() {
        return this.idNode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nodo[").append(idNode).append("] ");
        sb.append(isLeaf ? "(HOJA)" : "(INTERNO)").append(": (");

        for (int i = 0; i < this.count; i++) {
            sb.append(this.keys.get(i));
            if (i < this.count - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");

        if (isLeaf && data != null && !data.isEmpty()) {
            sb.append(" -> Datos: ").append(data.toString());
        }

        return sb.toString();
    }
}