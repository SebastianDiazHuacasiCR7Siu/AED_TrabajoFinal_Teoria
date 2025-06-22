package btreeplus;

import java.util.ArrayList;

/**
 * B+ Tree híbrido implementado con:
 * - ArrayList para nodos internos (índices)
 * - ListaEnlazada para nodos hoja (datos)
 * - Enlaces entre nodos hoja para recorrido secuencial eficiente
 */
public class BTreePlus<E extends Comparable<E>> {
    private BNodePlus<E> root; //Esta es la raíz de nuestro árbol
    private int orden; //El orden del árbol, que define el número máximo y mínimo de claves por nodo
    private BNodePlus<E> firstLeaf; //Referencia al primer nodo hoja

    public BTreePlus(int orden) {
        if (orden < 3) {
            throw new IllegalArgumentException("El orden debe ser al menos 3"); //El orden debe ser al menos 3 para que el árbol funcione bien
        }
        this.orden = orden;
        this.root = null; //Al inicio, el árbol está vacío
        this.firstLeaf = null; //No hay hojas todavía
    }

    /**
     * Verifica si el árbol está vacío
     */
    public boolean isEmpty() {
        return this.root == null; //Si la raíz es nula, el árbol está vacío
    }

    /**
     * Inserta una clave en el B+ Tree
     */
    public void insert(E key) {
        if (key == null) {
            System.out.println("No se puede insertar una clave nula"); //No permitimos claves nulas
            return;
        }

        if (isEmpty()) {
            //Crear el primer nodo hoja si el árbol está vacío
            this.root = new BNodePlus<>(this.orden, true); //La raíz es el primer nodo hoja
            this.root.insertInLeaf(key); //Insertamos la clave directamente en él
            this.firstLeaf = this.root; //Este es nuestro primer nodo hoja
            System.out.println("Insertada clave " + key + " en nuevo árbol");
            return;
        }

        //Buscar el nodo hoja apropiado donde debería ir la clave
        BNodePlus<E> leaf = findLeaf(key);

        //Si el nodo hoja no está lleno, se inserta directamente
        if (!leaf.isFull(this.orden)) {
            if (leaf.insertInLeaf(key)) { //Intentamos insertar en la hoja
                System.out.println("Insertada clave " + key + " en nodo hoja " + leaf.getIdNode());
            } else {
                System.out.println("Clave " + key + " ya existe o error al insertar"); //La clave ya podría existir
            }
            return;
        }

        //Si el nodo hoja está lleno, hay que dividir
        splitLeafAndPropagate(leaf, key);
    }

    /**
     * Encuentra el nodo hoja donde debería insertarse/buscarse una clave
     */
    private BNodePlus<E> findLeaf(E key) {
        BNodePlus<E> current = this.root; //Empezamos desde la raíz

        while (!current.isLeaf()) { //Recorremos hasta encontrar un nodo hoja
            int[] pos = new int[1]; //Array para almacenar la posición de la clave
            current.searchNode(key, pos); //Buscamos la posición para saber qué hijo seguir

            //En nodos internos, pos[0] indica el índice del hijo a seguir
            if (pos[0] >= current.getCount()) {
                pos[0] = current.getCount(); //Si la clave es mayor que todas en el nodo, vamos al último hijo
            }

            current = current.getChild(pos[0]); //Nos movemos al hijo correspondiente
        }

        return current; //Devolvemos el nodo hoja encontrado
    }

    /**
     * Divide un nodo hoja lleno y propaga los cambios hacia arriba
     */
    private void splitLeafAndPropagate(BNodePlus<E> leaf, E key) {
        //Crear lista temporal con todas las claves incluyendo la nueva
        ArrayList<E> allKeys = new ArrayList<>();
        boolean inserted = false;

        //Copiamos las claves existentes y la nueva clave en orden
        for (int i = 0; i < leaf.getCount(); i++) {
            E currentKey = leaf.getKey(i);
            if (!inserted && key.compareTo(currentKey) < 0) {
                allKeys.add(key); //Insertamos la nueva clave en su posición correcta
                inserted = true;
            }
            allKeys.add(currentKey); //Añadimos las claves ya existentes
        }
        if (!inserted) {
            allKeys.add(key); //Si la nueva clave es la más grande, la añadimos al final
        }

        //Calcular punto de división es decir la mitad de las claves
        int mid = allKeys.size() / 2;

        //Crear nuevo nodo hoja derecho
        BNodePlus<E> newLeaf = new BNodePlus<>(this.orden, true);

        //Redistribuir claves entre los dos nodos hoja
        //Nodo izquierdo, nodo original,  mantiene la primera mitad
        leaf.setCount(0); //Reiniciamos el conteo de claves del nodo original
        leaf.getData().destroyList(); //Limpiamos la lista enlazada del nodo original

        for (int i = 0; i < mid; i++) {
            leaf.insertInLeaf(allKeys.get(i)); //Movemos las primeras claves al nodo original, es decir el izquierdo
        }

        //Nodo derecho recibe la segunda mitad
        for (int i = mid; i < allKeys.size(); i++) {
            newLeaf.insertInLeaf(allKeys.get(i)); //Movemos las claves restantes al nuevo nodo es decir el derecho
        }

        //Actualizar enlaces entre nodos hoja para mantener el recorrido secuencial
        newLeaf.setNext(leaf.getNext()); //El nuevo nodo apunta a lo que apuntaba el nodo original
        leaf.setNext(newLeaf); //El nodo original ahora apunta al nuevo nodo

        //La clave que sube es la primera clave del nodo derecho
        E keyToPromote = newLeaf.getKey(0);

        System.out.println("Dividiendo nodo hoja " + leaf.getIdNode() +
                ", nueva clave a promover: " + keyToPromote);

        //Si la hoja dividida era la raíz, crear nueva raíz
        if (leaf == this.root) {
            BNodePlus<E> newRoot = new BNodePlus<>(this.orden, false); //La nueva raíz será un nodo interno
            newRoot.insertInInternal(keyToPromote, newLeaf, 0); //Insertamos la clave promovida y el nuevo nodo como hijo
            newRoot.setChild(0, leaf); //El nodo original es el primer hijo
            this.root = newRoot; //Actualizamos la raíz del árbol
            System.out.println("Nueva raíz creada con clave " + keyToPromote);
        } else {
            //Propagar la división hacia arriba, insertando en el padre
            insertInParent(leaf, keyToPromote, newLeaf);
        }
    }

    /**
     * Inserta una clave en el nodo padre apropiado, manejando divisiones si es necesario
     */
    private void insertInParent(BNodePlus<E> leftChild, E key, BNodePlus<E> rightChild) {
        BNodePlus<E> parent = findParent(this.root, leftChild); //Encontramos al padre del nodo que se dividió

        if (parent == null) {
            //Si no hay padre, significa que el nodo divido era la raíz, y ya se manejó en splitLeafAndPropagate
            //O, si el árbol tiene solo un nodo y es hoja y se divide, se crea una nueva raíz
            BNodePlus<E> newRoot = new BNodePlus<>(this.orden, false);
            newRoot.insertInInternal(key, rightChild, 0);
            newRoot.setChild(0, leftChild);
            this.root = newRoot;
            return;
        }

        //Encontrar posición correcta en el padre para insertar la nueva clave y su hijo derecho
        int pos = 0;
        while (pos < parent.getCount() && parent.getKey(pos).compareTo(key) < 0) {
            pos++;
        }

        //Si el padre no está lleno, insertar directamente
        if (!parent.isFull(this.orden)) {
            parent.insertInInternal(key, rightChild, pos); //Insertamos la clave y el nuevo hijo en el padre
            System.out.println("Insertada clave " + key + " en nodo interno " + parent.getIdNode());
        } else {
            //Si el padre está lleno, ¡también hay que dividirlo!
            splitInternalNode(parent, key, rightChild, pos);
        }
    }

    /**
     * Divide un nodo interno lleno
     */
    private void splitInternalNode(BNodePlus<E> node, E key, BNodePlus<E> rightChild, int pos) {
        //Crear listas temporales para todas las claves e hijos, incluyendo la nueva clave y el nuevo hijo
        ArrayList<E> allKeys = new ArrayList<>();
        ArrayList<BNodePlus<E>> allChildren = new ArrayList<>();

        //Copiar claves existentes e insertar nueva clave en su posición correcta
        boolean insertedKey = false;
        for (int i = 0; i < node.getCount(); i++) {
            if (!insertedKey && i == pos) {
                allKeys.add(key);
                insertedKey = true;
            }
            allKeys.add(node.getKey(i));
        }
        if (!insertedKey) {
            allKeys.add(key); //Si la nueva clave es la más grande
        }

        //Copiar hijos existentes e insertar nuevo hijo en su posición correcta
        for (int i = 0; i <= node.getCount(); i++) {
            if (i == pos + 1) {
                allChildren.add(rightChild);
            }
            allChildren.add(node.getChild(i));
        }
        //Asegurarse de que el último hijo se añada si la inserción fue al final
        if (pos + 1 == node.getCount() + 1 && !allChildren.contains(rightChild)) {
            allChildren.add(rightChild);
        }


        //Calcular punto de división, mitad de las claves
        int mid = allKeys.size() / 2; //Esta clave subirá al padre
        E keyToPromote = allKeys.get(mid);

        //Crear nuevo nodo interno derecho
        BNodePlus<E> newInternal = new BNodePlus<>(this.orden, false);

        //Redistribuir claves e hijos entre los dos nodos internos
        node.setCount(0); //Reiniciamos el conteo de claves del nodo original

        //Nodo izquierdo (original) recibe la primera mitad de las claves y sus hijos
        for (int i = 0; i < mid; i++) {
            node.setKey(i, allKeys.get(i));
            node.setChild(i, allChildren.get(i));
            node.setCount(node.getCount() + 1);
        }
        node.setChild(mid, allChildren.get(mid)); //El hijo correspondiente a la última clave del nodo izquierdo

        //Nodo derecho (el nuevo) recibe la segunda mitad de las claves y sus hijos
        for (int i = mid + 1; i < allKeys.size(); i++) {
            newInternal.setKey(newInternal.getCount(), allKeys.get(i));
            newInternal.setChild(newInternal.getCount(), allChildren.get(i));
            newInternal.setCount(newInternal.getCount() + 1);
        }
        newInternal.setChild(newInternal.getCount(), allChildren.get(allChildren.size() - 1)); //El último hijo

        System.out.println("Dividiendo nodo interno " + node.getIdNode() +
                ", clave a promover: " + keyToPromote);

        //Propagar hacia arriba, insertando en el padre
        if (node == this.root) {
            BNodePlus<E> newRoot = new BNodePlus<>(this.orden, false); //Si el nodo dividido era la raíz, creamos una nueva raíz
            newRoot.insertInInternal(keyToPromote, newInternal, 0); //Insertamos la clave promovida y el nuevo nodo como hijo
            newRoot.setChild(0, node); //El nodo original es el primer hijo
            this.root = newRoot; //Actualizamos la raíz del árbol
        } else {
            insertInParent(node, keyToPromote, newInternal); //Insertamos la clave promovida en el padre del nodo original
        }
    }

    /**
     * Encuentra el nodo padre de un nodo dado
     */
    private BNodePlus<E> findParent(BNodePlus<E> current, BNodePlus<E> child) {
        if (current.isLeaf() || current == child) {
            return null; //Si el nodo actual es una hoja o es el propio hijo, no tiene padre en este contexto
        }

        for (int i = 0; i <= current.getCount(); i++) { //Iteramos sobre los hijos del nodo actual
            if (current.getChild(i) == child) {
                return current; //Si encontramos el hijo, el nodo actual es su padre
            }

            //Si el hijo no es hoja, seguimos buscando recursivamente en el subárbol
            if (current.getChild(i) != null && !current.getChild(i).isLeaf()) {
                BNodePlus<E> parent = findParent(current.getChild(i), child);
                if (parent != null) {
                    return parent; //Si encontramos el padre en el subárbol, lo devolvemos
                }
            }
        }

        return null; //Si no encontramos el padre en este subárbol
    }

    /**
     * Busca una clave en el árbol
     */
    public boolean search(E key) {
        if (isEmpty() || key == null) {
            return false; //No podemos buscar si el árbol está vacío o la clave es nula
        }

        BNodePlus<E> leaf = findLeaf(key); //Encuentra el nodo hoja donde debería estar la clave
        int[] pos = new int[1]; //Array para almacenar la posición de la clave en la hoja
        boolean found = leaf.searchNode(key, pos); //Busca la clave en ese nodo hoja

        if (found) {
            System.out.println("Clave " + key + " encontrada en nodo hoja " + leaf.getIdNode());
        } else {
            System.out.println("Clave " + key + " no encontrada");
        }

        return found;
    }

    /**
     * Elimina una clave del B+ Tree
     */
    public void remove(E key) {
        if (isEmpty() || key == null) {
            System.out.println("El árbol está vacío o la clave es nula. No se puede eliminar.");
            return;
        }

        boolean success = removeKey(this.root, key); //Llamamos al método recursivo para eliminar
        if (success) {
            System.out.println("Se eliminó la clave " + key + " del árbol.");

            //Si la raíz queda vacía después de la eliminación (solo si la raíz no es hoja y no tiene claves)
            if (this.root.getCount() == 0 && !this.root.isLeaf()) {
                this.root = this.root.getChild(0); //La nueva raíz es su único hijo
                //Actualizar firstLeaf si es necesario
                if (this.root != null && this.root.isLeaf()) {
                    this.firstLeaf = this.root; //Si la nueva raíz es una hoja, actualizamos firstLeaf
                }
            }
        } else {
            System.out.println("La clave " + key + " no se encontró en el árbol.");
        }
    }

    /**
     * Método recursivo para eliminar una clave
     */
    private boolean removeKey(BNodePlus<E> current, E key) {
        if (current.isLeaf()) {
            //Si es nodo hoja, eliminar directamente la clave
            return current.removeFromLeaf(key);
        } else {
            //Nodo interno: encontrar el hijo apropiado donde debería estar la clave
            int pos = 0;
            while (pos < current.getCount() && current.getKey(pos).compareTo(key) <= 0) {
                pos++; //Avanzamos hasta encontrar el puntero correcto
            }

            BNodePlus<E> child = current.getChild(pos); //Obtenemos el hijo a visitar
            boolean success = removeKey(child, key); //Recursivamente, intentamos eliminar la clave en el hijo

            //Verificar underflow en el hijo después de la eliminación
            if (success && child.hasUnderflow(this.orden)) {
                fixUnderflow(current, pos); //Si hay underflow, lo corregimos
            }

            return success;
        }
    }

    /**
     * Corrige el underflow en un nodo hijo
     */
    private void fixUnderflow(BNodePlus<E> parent, int childIndex) {
        BNodePlus<E> child = parent.getChild(childIndex); //El nodo con underflow
        BNodePlus<E> leftSibling = (childIndex > 0) ? parent.getChild(childIndex - 1) : null; //Hermano izquierdo (si existe)
        BNodePlus<E> rightSibling = (childIndex < parent.getCount()) ? parent.getChild(childIndex + 1) : null; //Hermano derecho (si existe)

        int minKeys = (this.orden - 1) / 2; //Número mínimo de claves permitido

        //1. Intentar préstamo del hermano derecho si tiene suficientes claves
        if (rightSibling != null && rightSibling.getCount() > minKeys) {
            borrowFromRightSibling(parent, child, rightSibling, childIndex);
        }
        //2. Intentar préstamo del hermano izquierdo si tiene suficientes claves
        else if (leftSibling != null && leftSibling.getCount() > minKeys) {
            borrowFromLeftSibling(parent, child, leftSibling, childIndex);
        }
        //3. Fusión con hermano si no se puede prestar
        else {
            if (rightSibling != null) {
                mergeWithRightSibling(parent, child, rightSibling, childIndex);
            } else if (leftSibling != null) {
                mergeWithLeftSibling(parent, child, leftSibling, childIndex);
            }
        }
    }

    /**
     * Toma prestada una clave del hermano derecho
     */
    private void borrowFromRightSibling(BNodePlus<E> parent, BNodePlus<E> child,
                                        BNodePlus<E> rightSibling, int childIndex) {
        if (child.isLeaf()) {
            //Para nodos hoja: mover la primera clave del hermano derecho al hijo
            E keyToBorrow = rightSibling.getKey(0);
            child.insertInLeaf(keyToBorrow); //Insertamos la clave en el nodo hijo
            rightSibling.removeFromLeaf(keyToBorrow); //Eliminamos la clave del hermano derecho

            //Actualizar la clave en el padre (ahora es la primera clave del hermano derecho)
            parent.setKey(childIndex, rightSibling.getKey(0));
        } else {
            //Para nodos internos: bajar clave del padre, subir clave del hermano
            E parentKey = parent.getKey(childIndex); //La clave que baja del padre
            child.setKey(child.getCount(), parentKey); //La agregamos al final del hijo
            child.setChild(child.getCount() + 1, rightSibling.getChild(0)); //Movemos el primer hijo del hermano al hijo
            child.setCount(child.getCount() + 1);

            //Subir primera clave del hermano derecho al padre
            parent.setKey(childIndex, rightSibling.getKey(0));

            //Eliminar primera clave y hijo del hermano derecho
            for (int i = 0; i < rightSibling.getCount() - 1; i++) {
                rightSibling.setKey(i, rightSibling.getKey(i + 1));
            }
            for (int i = 0; i < rightSibling.getCount(); i++) {
                rightSibling.setChild(i, rightSibling.getChild(i + 1));
            }
            rightSibling.setCount(rightSibling.getCount() - 1); //Decrementamos el conteo del hermano
        }
    }

    /**
     * Toma prestada una clave del hermano izquierdo
     */
    private void borrowFromLeftSibling(BNodePlus<E> parent, BNodePlus<E> child,
                                       BNodePlus<E> leftSibling, int childIndex) {
        if (child.isLeaf()) {
            //Para nodos hoja: mover la última clave del hermano izquierdo
            E keyToBorrow = leftSibling.getKey(leftSibling.getCount() - 1);

            //Hacer espacio en el hijo para la nueva clave al inicio
            for (int i = child.getCount(); i > 0; i--) {
                child.setKey(i, child.getKey(i - 1));
            }
            child.setKey(0, keyToBorrow); //Insertamos la clave prestada al inicio del hijo
            child.setCount(child.getCount() + 1);

            //Agregar a la lista enlazada (esto es específico de tu implementación de ListaEnlazada)
            //Manejo de la excepción de ListaEnlazada
            child.getData().insertFirst(keyToBorrow);

            //Eliminar del hermano izquierdo
            leftSibling.removeFromLeaf(keyToBorrow);

            //Actualizar clave en el padre (ahora es la primera clave del hijo)
            parent.setKey(childIndex - 1, child.getKey(0));
        } else {
            //Para nodos internos
            E parentKey = parent.getKey(childIndex - 1); //La clave que baja del padre

            //Hacer espacio en el hijo para la nueva clave y su hijo al inicio
            for (int i = child.getCount(); i > 0; i--) {
                child.setKey(i, child.getKey(i - 1));
            }
            for (int i = child.getCount() + 1; i > 0; i--) {
                child.setChild(i, child.getChild(i - 1));
            }

            //Bajar clave del padre e hijo del hermano izquierdo
            child.setKey(0, parentKey);
            child.setChild(0, leftSibling.getChild(leftSibling.getCount())); //El último hijo del hermano izquierdo
            child.setCount(child.getCount() + 1);

            //Subir última clave del hermano izquierdo al padre
            parent.setKey(childIndex - 1, leftSibling.getKey(leftSibling.getCount() - 1));
            leftSibling.setCount(leftSibling.getCount() - 1); //Decrementamos el conteo del hermano
        }
    }

    /**
     * Fusiona el nodo con su hermano derecho
     */
    private void mergeWithRightSibling(BNodePlus<E> parent, BNodePlus<E> child,
                                       BNodePlus<E> rightSibling, int childIndex) {
        if (child.isLeaf()) {
            //Fusión de nodos hoja: mover todas las claves del hermano derecho al hijo
            for (int i = 0; i < rightSibling.getCount(); i++) {
                child.insertInLeaf(rightSibling.getKey(i)); //Añadimos las claves del hermano
            }

            //Actualizar enlaces entre nodos hoja para saltar el hermano derecho
            child.setNext(rightSibling.getNext());
        } else {
            //Fusión de nodos internos: bajar clave del padre
            E parentKey = parent.getKey(childIndex);
            child.setKey(child.getCount(), parentKey); //La clave del padre baja al hijo
            child.setChild(child.getCount() + 1, rightSibling.getChild(0)); //El primer hijo del hermano
            child.setCount(child.getCount() + 1);

            //Mover todas las claves e hijos del hermano derecho al hijo
            for (int i = 0; i < rightSibling.getCount(); i++) {
                child.setKey(child.getCount(), rightSibling.getKey(i));
                child.setChild(child.getCount() + 1, rightSibling.getChild(i + 1));
                child.setCount(child.getCount() + 1);
            }
        }

        //Eliminar la clave del padre y el puntero al hermano derecho
        for (int i = childIndex; i < parent.getCount() - 1; i++) {
            parent.setKey(i, parent.getKey(i + 1)); //Movemos claves a la izquierda
        }
        for (int i = childIndex + 1; i < parent.getCount(); i++) {
            parent.setChild(i, parent.getChild(i + 1)); //Movemos punteros a la izquierda
        }
        parent.setCount(parent.getCount() - 1); //Decrementamos el conteo del padre
    }

    /**
     * Fusiona el nodo con su hermano izquierdo
     */
    private void mergeWithLeftSibling(BNodePlus<E> parent, BNodePlus<E> child,
                                      BNodePlus<E> leftSibling, int childIndex) {
        if (child.isLeaf()) {
            //Fusión de nodos hoja: mover todas las claves del hijo al hermano izquierdo
            for (int i = 0; i < child.getCount(); i++) {
                leftSibling.insertInLeaf(child.getKey(i)); //Añadimos las claves del hijo al hermano izquierdo
            }

            //Actualizar enlaces entre nodos hoja para saltar el hijo
            leftSibling.setNext(child.getNext());
        } else {
            //Fusión de nodos internos: bajar clave del padre al hermano izquierdo
            E parentKey = parent.getKey(childIndex - 1);
            leftSibling.setKey(leftSibling.getCount(), parentKey); //La clave del padre baja al hermano izquierdo
            leftSibling.setChild(leftSibling.getCount() + 1, child.getChild(0)); //El primer hijo del nodo actual
            leftSibling.setCount(leftSibling.getCount() + 1);

            //Mover todas las claves e hijos del hijo al hermano izquierdo
            for (int i = 0; i < child.getCount(); i++) {
                leftSibling.setKey(leftSibling.getCount(), child.getKey(i));
                leftSibling.setChild(leftSibling.getCount() + 1, child.getChild(i + 1));
                leftSibling.setCount(leftSibling.getCount() + 1);
            }
        }

        //Eliminar la clave del padre y el puntero al hijo
        for (int i = childIndex - 1; i < parent.getCount() - 1; i++) {
            parent.setKey(i, parent.getKey(i + 1)); //Movemos claves a la izquierda
        }
        for (int i = childIndex; i < parent.getCount(); i++) {
            parent.setChild(i, parent.getChild(i + 1)); //Movemos punteros a la izquierda
        }
        parent.setCount(parent.getCount() - 1); //Decrementamos el conteo del padre
    }

    /**
     * Imprime todas las claves en orden secuencial (recorrido por nodos hoja)
     */
    public void printInOrder() {
        if (isEmpty()) {
            System.out.println("Árbol vacío");
            return;
        }

        System.out.print("Recorrido secuencial: ");
        BNodePlus<E> current = this.firstLeaf; //Empezamos desde el primer nodo hoja

        while (current != null) { //Recorremos todos los nodos hoja a través de sus enlaces 'next'
            for (int i = 0; i < current.getCount(); i++) {
                System.out.print(current.getKey(i) + " "); //Imprimimos cada clave de la hoja
            }
            current = current.getNext(); //Pasamos al siguiente nodo hoja
        }
        System.out.println();
    }

    /**
     * Imprime la estructura del árbol
     */
    public void printTree() {
        if (isEmpty()) {
            System.out.println("Árbol vacío");
            return;
        }

        System.out.println("Estructura del B+ Tree:");
        System.out.println("========================");
        printTreeRecursive(this.root, 0); //Iniciamos el recorrido recursivo desde la raíz
    }

    private void printTreeRecursive(BNodePlus<E> node, int level) {
        if (node != null) {
            //Imprimir indentación según el nivel para una visualización jerárquica
            for (int i = 0; i < level; i++) {
                System.out.print("  ");
            }

            System.out.println(node.toString()); //Imprimimos la representación del nodo

            //Recursivamente imprimir hijos si es nodo interno
            if (!node.isLeaf()) {
                for (int i = 0; i <= node.getCount(); i++) {
                    if (node.getChild(i) != null) {
                        printTreeRecursive(node.getChild(i), level + 1); //Llamada recursiva para cada hijo, aumentando el nivel
                    }
                }
            }
        }
    }
}
