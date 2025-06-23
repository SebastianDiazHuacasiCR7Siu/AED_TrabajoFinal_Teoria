class BNode {
    int t; // Grado mínimo
    int n; // Número de claves actuales
    boolean hoja;
    int[] claves;
    BNode[] hijos;

    BNode(int t, boolean hoja) {
        this.t = t;
        this.hoja = hoja;
        this.claves = new int[2 * t - 1];
        this.hijos = new BNode[2 * t];
        this.n = 0;
    }
}

public class BTree {
    private BNode raiz;
    private int t;

    public BTree() {
        this.t = 3;
        this.raiz = new BNode(t, true);
    }

    public void insertar(int clave) {
        BNode r = raiz;
        if (r.n == 2 * t - 1) {
            BNode s = new BNode(t, false);
            s.hijos[0] = r;
            dividir(s, 0);
            insertarNoLleno(s, clave);
            raiz = s;
        } else {
            insertarNoLleno(r, clave);
        }
    }

    private void insertarNoLleno(BNode x, int clave) {
        int i = x.n - 1;
        if (x.hoja) {
            while (i >= 0 && clave < x.claves[i]) {
                x.claves[i + 1] = x.claves[i];
                i--;
            }
            x.claves[i + 1] = clave;
            x.n++;
        } else {
            while (i >= 0 && clave < x.claves[i]) {
                i--;
            }
            i++;
            if (x.hijos[i].n == 2 * t - 1) {
                dividir(x, i);
                if (clave > x.claves[i]) {
                    i++;
                }
            }
            insertarNoLleno(x.hijos[i], clave);
        }
    }

    private void dividir(BNode x, int i) {
        BNode y = x.hijos[i];
        BNode z = new BNode(t, y.hoja);
        z.n = t - 1;

        for (int j = 0; j < t - 1; j++) {
            z.claves[j] = y.claves[j + t];
        }
        if (!y.hoja) {
            for (int j = 0; j < t; j++) {
                z.hijos[j] = y.hijos[j + t];
            }
        }
        y.n = t - 1;

        for (int j = x.n; j >= i + 1; j--) {
            x.hijos[j + 1] = x.hijos[j];
        }
        x.hijos[i + 1] = z;

        for (int j = x.n - 1; j >= i; j--) {
            x.claves[j + 1] = x.claves[j];
        }
        x.claves[i] = y.claves[t - 1];
        x.n++;
    }

    public void mostrar() {
        mostrarRec(raiz, "");
    }

    private void mostrarRec(BNode nodo, String indent) {
        System.out.print(indent + "[");
        for (int i = 0; i < nodo.n; i++) {
            System.out.print(nodo.claves[i]);
            if (i < nodo.n - 1) System.out.print(", ");
        }
        System.out.println("]");
        if (!nodo.hoja) {
            for (int i = 0; i <= nodo.n; i++) {
                mostrarRec(nodo.hijos[i], indent + "  ");
            }
        }
    }

    public boolean buscar(int clave) {
        return buscarRec(raiz, clave);
    }

    private boolean buscarRec(BNode nodo, int clave) {
        int i = 0;
        while (i < nodo.n && clave > nodo.claves[i]) i++;
        if (i < nodo.n && nodo.claves[i] == clave) return true;
        if (nodo.hoja) return false;
        return buscarRec(nodo.hijos[i], clave);
    }

    public void eliminar(int clave) {
        eliminarRec(raiz, clave);
        if (raiz.n == 0 && !raiz.hoja) {
            raiz = raiz.hijos[0];
        }
    }

    private void eliminarRec(BNode nodo, int clave) {
        int idx = 0;
        while (idx < nodo.n && clave > nodo.claves[idx]) idx++;

        if (idx < nodo.n && nodo.claves[idx] == clave) {
            if (nodo.hoja) {
                for (int i = idx; i < nodo.n - 1; i++) {
                    nodo.claves[i] = nodo.claves[i + 1];
                }
                nodo.n--;
            } else {
                if (nodo.hijos[idx].n >= t) {
                    int pred = getPredecesor(nodo.hijos[idx]);
                    nodo.claves[idx] = pred;
                    eliminarRec(nodo.hijos[idx], pred);
                } else if (nodo.hijos[idx + 1].n >= t) {
                    int succ = getSucesor(nodo.hijos[idx + 1]);
                    nodo.claves[idx] = succ;
                    eliminarRec(nodo.hijos[idx + 1], succ);
                } else {
                    fusionar(nodo, idx);
                    eliminarRec(nodo.hijos[idx], clave);
                }
            }
        } else {
            if (nodo.hoja) return;

            boolean alFinal = (idx == nodo.n);
            if (nodo.hijos[idx].n < t) llenar(nodo, idx);

            if (alFinal && idx > nodo.n) eliminarRec(nodo.hijos[idx - 1], clave);
            else eliminarRec(nodo.hijos[idx], clave);
        }
    }

    private int getPredecesor(BNode nodo) {
        while (!nodo.hoja) nodo = nodo.hijos[nodo.n];
        return nodo.claves[nodo.n - 1];
    }

    private int getSucesor(BNode nodo) {
        while (!nodo.hoja) nodo = nodo.hijos[0];
        return nodo.claves[0];
    }

    private void llenar(BNode nodo, int idx) {
        if (idx != 0 && nodo.hijos[idx - 1].n >= t) {
            tomarPrestadoIzquierda(nodo, idx);
        } else if (idx != nodo.n && nodo.hijos[idx + 1].n >= t) {
            tomarPrestadoDerecha(nodo, idx);
        } else {
            if (idx != nodo.n) fusionar(nodo, idx);
            else fusionar(nodo, idx - 1);
        }
    }

    private void tomarPrestadoIzquierda(BNode nodo, int idx) {
        BNode hijo = nodo.hijos[idx];
        BNode hermano = nodo.hijos[idx - 1];

        for (int i = hijo.n - 1; i >= 0; i--) hijo.claves[i + 1] = hijo.claves[i];
        if (!hijo.hoja) {
            for (int i = hijo.n; i >= 0; i--) hijo.hijos[i + 1] = hijo.hijos[i];
        }
        hijo.claves[0] = nodo.claves[idx - 1];
        if (!hijo.hoja) hijo.hijos[0] = hermano.hijos[hermano.n];
        nodo.claves[idx - 1] = hermano.claves[--hermano.n];
        hijo.n++;
    }

    private void tomarPrestadoDerecha(BNode nodo, int idx) {
        BNode hijo = nodo.hijos[idx];
        BNode hermano = nodo.hijos[idx + 1];

        hijo.claves[hijo.n++] = nodo.claves[idx];
        nodo.claves[idx] = hermano.claves[0];
        for (int i = 0; i < hermano.n - 1; i++) hermano.claves[i] = hermano.claves[i + 1];
        if (!hermano.hoja) {
            hijo.hijos[hijo.n] = hermano.hijos[0];
            for (int i = 0; i < hermano.n; i++) hermano.hijos[i] = hermano.hijos[i + 1];
        }
        hermano.n--;
    }

    private void fusionar(BNode nodo, int idx) {
        BNode hijo = nodo.hijos[idx];
        BNode hermano = nodo.hijos[idx + 1];

        hijo.claves[t - 1] = nodo.claves[idx];
        for (int i = 0; i < hermano.n; i++) hijo.claves[i + t] = hermano.claves[i];
        if (!hijo.hoja) {
            for (int i = 0; i <= hermano.n; i++) hijo.hijos[i + t] = hermano.hijos[i];
        }

        for (int i = idx + 1; i < nodo.n; i++) nodo.claves[i - 1] = nodo.claves[i];
        for (int i = idx + 2; i <= nodo.n; i++) nodo.hijos[i - 1] = nodo.hijos[i];

        hijo.n += hermano.n + 1;
        nodo.n--;
    }
}

