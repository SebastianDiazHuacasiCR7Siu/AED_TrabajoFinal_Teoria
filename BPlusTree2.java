// Clase que representa un nodo del B+ Tree

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class BPlusNode {
    int t;
    List<Integer> claves;
    List<BPlusNode> hijos;
    BPlusNode siguiente;
    boolean hoja;

    BPlusNode(int t, boolean hoja) {
        this.t = t;
        this.hoja = hoja;
        this.claves = new ArrayList<>();
        this.hijos = new ArrayList<>();
        this.siguiente = null;
    }
}

public class BPlusTree2 {
    private BPlusNode raiz;
    private int t;

    public BPlusTree2(int t) {
        this.t = t;
        this.raiz = new BPlusNode(t, true);
    }

    // Buscar una clave
    public boolean buscar(int clave) {
        BPlusNode actual = raiz;
        while (!actual.hoja) {
            int i = 0;
            while (i < actual.claves.size() && clave >= actual.claves.get(i)) i++;
            actual = actual.hijos.get(i);
        }
        return actual.claves.contains(clave);
    }

    // Insertar una clave
    public void insertar(int clave) {
        BPlusNode r = raiz;
        if (r.claves.size() == 2 * t - 1) {
            BPlusNode nuevaRaiz = new BPlusNode(t, false);
            nuevaRaiz.hijos.add(r);
            dividirHijo(nuevaRaiz, 0);
            raiz = nuevaRaiz;
        }
        insertarNoLleno(raiz, clave);
    }

    private void insertarNoLleno(BPlusNode nodo, int clave) {
        if (nodo.hoja) {
            int i = 0;
            while (i < nodo.claves.size() && clave > nodo.claves.get(i)) i++;
            nodo.claves.add(i, clave);
        } else {
            int i = 0;
            while (i < nodo.claves.size() && clave >= nodo.claves.get(i)) i++;
            BPlusNode hijo = nodo.hijos.get(i);
            if (hijo.claves.size() == 2 * t - 1) {
                dividirHijo(nodo, i);
                if (clave >= nodo.claves.get(i)) i++;
            }
            insertarNoLleno(nodo.hijos.get(i), clave);
        }
    }

    private void dividirHijo(BPlusNode padre, int i) {
        BPlusNode y = padre.hijos.get(i);
        BPlusNode z = new BPlusNode(t, y.hoja);

        int medio = t;
        z.claves.addAll(y.claves.subList(medio, y.claves.size()));
        y.claves = new ArrayList<>(y.claves.subList(0, medio));

        if (!y.hoja) {
            z.hijos.addAll(y.hijos.subList(medio, y.hijos.size()));
            y.hijos = new ArrayList<>(y.hijos.subList(0, medio));
        } else {
            z.siguiente = y.siguiente;
            y.siguiente = z;
        }

        padre.claves.add(i, z.claves.get(0));
        padre.hijos.add(i + 1, z);
    }

    // Mostrar el árbol por niveles
    public void mostrar() {
        Queue<BPlusNode> cola = new LinkedList<>();
        cola.add(raiz);
        while (!cola.isEmpty()) {
            int nivel = cola.size();
            while (nivel-- > 0) {
                BPlusNode actual = cola.poll();
                System.out.print(actual.claves + " ");
                if (!actual.hoja) cola.addAll(actual.hijos);
            }
            System.out.println();
        }
    }

    // Recorrer hojas (rango)
    public void recorridoHojas() {
        BPlusNode actual = raiz;
        while (!actual.hoja) actual = actual.hijos.get(0);
        while (actual != null) {
            System.out.print(actual.claves + " -> ");
            actual = actual.siguiente;
        }
        System.out.println("null");
    }

    // Eliminar una clave (sólo para hojas por simplicidad)
    public void eliminar(int clave) {
        BPlusNode actual = raiz;
        while (!actual.hoja) {
            int i = 0;
            while (i < actual.claves.size() && clave >= actual.claves.get(i)) i++;
            actual = actual.hijos.get(i);
        }
        actual.claves.remove((Integer) clave);
    }
}
