package btreeplus;

public class BTreePlusTest {
    public static void main(String[] args) {
        // Crear un árbol B+ de orden 4
        BTreePlus<Integer> btree = new BTreePlus<>(4);

        // Insertar elementos
        int[] elementos = {10, 20, 5, 6, 12, 30, 7, 17, 3, 8, 25, 15, 1};
        System.out.println("==== INSERTANDO ELEMENTOS ====");
        for (int e : elementos) {
            btree.insert(e);
        }

        // Imprimir recorrido secuencial (de menor a mayor)
        System.out.println("\n==== RECORRIDO IN ORDER ====");
        btree.printInOrder();

        // Imprimir estructura del árbol
        System.out.println("\n==== ESTRUCTURA DEL ÁRBOL ====");
        btree.printTree();

        // Buscar claves existentes y no existentes
        System.out.println("\n==== BÚSQUEDA DE CLAVES ====");
        int[] buscar = {6, 15, 100};
        for (int clave : buscar) {
            boolean encontrado = btree.search(clave);
            System.out.println("Buscar " + clave + ": " + (encontrado ? "Encontrado" : "No encontrado"));
        }

        // Eliminar algunas claves
        System.out.println("\n==== ELIMINANDO CLAVES ====");
        int[] eliminar = {6, 10, 20, 1, 3};
        for (int clave : eliminar) {
            System.out.println("Eliminando " + clave + "...");
            btree.remove(clave);
        }

        // Recorrido y estructura después de eliminar
        System.out.println("\n==== RECORRIDO POST-ELIMINACIÓN ====");
        btree.printInOrder();

        System.out.println("\n==== ESTRUCTURA POST-ELIMINACIÓN ====");
        btree.printTree();
    }
}
