class BPlusNode {
    boolean hoja;
    int n;
    int[] claves;
    BPlusNode[] hijos;
    BPlusNode siguiente;

    BPlusNode(int t, boolean hoja) {
        this.hoja = hoja;
        this.claves = new int[2 * t - 1];
        this.hijos = new BPlusNode[2 * t];
        this.n = 0;
        this.siguiente = null;
    }
}

public class BPlusTree {
    private BPlusNode raiz;
    private int t;

    public BPlusTree() {
        this.t = 3;
        this.raiz = new BPlusNode(t, true);
    }

    public void insertar(int clave) {
        BPlusNode r = raiz;
        if (r.n == 2 * t - 1) {
            BPlusNode s = new BPlusNode(t, false);
            s.hijos[0] = r;
            dividir(s, 0);
            raiz = s;
        }
        insertarNoLleno(raiz, clave);
    }

    private void insertarNoLleno(BPlusNode nodo, int clave) {
        int i = nodo.n - 1;

        if (nodo.hoja) {
            while (i >= 0 && clave < nodo.claves[i]) {
                nodo.claves[i + 1] = nodo.claves[i];
                i--;
            }
            nodo.claves[i + 1] = clave;
            nodo.n++;
        } else {
            while (i >= 0 && clave < nodo.claves[i]) {
                i--;
            }
            i++;
            if (nodo.hijos[i].n == 2 * t - 1) {
                dividir(nodo, i);
                if (clave > nodo.claves[i]) i++;
            }
            insertarNoLleno(nodo.hijos[i], clave);
        }
    }

    private void dividir(BPlusNode padre, int i) {
        BPlusNode y = padre.hijos[i];
        BPlusNode z = new BPlusNode(t, y.hoja);
        z.n = t - 1;

        for (int j = 0; j < t - 1; j++) {
            z.claves[j] = y.claves[j + t];
        }

        if (!y.hoja) {
            for (int j = 0; j < t; j++) {
                z.hijos[j] = y.hijos[j + t];
            }
        } else {
            z.siguiente = y.siguiente;
            y.siguiente = z;
        }

        y.n = t - 1;

        for (int j = padre.n; j >= i + 1; j--) {
            padre.hijos[j + 1] = padre.hijos[j];
        }
        padre.hijos[i + 1] = z;

        for (int j = padre.n - 1; j >= i; j--) {
            padre.claves[j + 1] = padre.claves[j];
        }
        padre.claves[i] = y.claves[t - 1];
        padre.n++;
    }

    public void mostrar() {
        mostrarRec(raiz, "");
    }

    private void mostrarRec(BPlusNode nodo, String indent) {
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
}
