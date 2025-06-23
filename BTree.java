class BNode {
    int t;
    int n;
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

    public String mostrarComoTexto() {
        return mostrarComoTextoRec(raiz, "");
    }

    private String mostrarComoTextoRec(BNode nodo, String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("[");
        for (int i = 0; i < nodo.n; i++) {
            sb.append(nodo.claves[i]);
            if (i < nodo.n - 1) sb.append(", ");
        }
        sb.append("]\n");
        if (!nodo.hoja) {
            for (int i = 0; i <= nodo.n; i++) {
                sb.append(mostrarComoTextoRec(nodo.hijos[i], indent + "  "));
            }
        }
        return sb.toString();
    }
}
