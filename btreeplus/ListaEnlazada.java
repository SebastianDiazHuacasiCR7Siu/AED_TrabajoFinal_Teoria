package btreeplus;
//Lista enlazada usando un nodo cabecera

import LinkedList.ListaTDA;
import LinkedList.MensajeException;
import LinkedList.Nodo;

public class ListaEnlazada<E> implements ListaTDA<E> {
    private Nodo<E> cabecera; //Se crea la variable cabecera de tipo Nodo

    public ListaEnlazada(){ //La lista comienza vacía
        cabecera = new Nodo<E>(null);
        cabecera.setSiguiente(null);
    }

    //Get de la cabecera de la lista
    public Nodo<E> getCabecera() {
        return cabecera;
    }

    //Set de la cabecera de la lista
    public void setCabecera(E valorNuevo) {
        cabecera.setValor(valorNuevo);
    }

    //Get del sigueinte de la cabecera
    public Nodo<E> getSiguiente() {
        return cabecera.getSiguiente();
    }

    //Set del sigueinte de la cabecera
    public void setSiguiente(Nodo<E> siguiente) {
        cabecera.setSiguiente(siguiente);
    }

    //Determina si la lista esta vacía
    public boolean isEmpty(){
        return cabecera.getSiguiente() == null; //Retorna true si esta vacía
    }

    //Determina la longitud de elementos de la lista (el tamaño)
    public int length(){
        if (isEmpty()){ return 0;}
        else {
            int contador = 0; //Se crea una variable contador que cuente la cantidad de nodos
            Nodo<E> nodoTemporal = cabecera.getSiguiente(); //Se asigna el valor del primer nodo a la variable nodoTemporal
            while (nodoTemporal != null) { //Ciclo que verifica que no se termino de recorrer la lista
                nodoTemporal = nodoTemporal.getSiguiente(); //NodoTemporal ahora tiene el valor del siguiente nodo
                contador = contador + 1; //Y el contador se incrementa en uno
            }
            return contador; //Cuando se llega al ultimo nodo de la lista imprime el total
        }
    }

    //Elimina los elementos de la lista dejandola vacía
    public void destroyList(){
        cabecera.setSiguiente(null);//La cabecera apunta a null
    }

    //Verifica si el elemento x está en la lista y retorna su posición
    public int search(E valor) throws MensajeException {
        if (isEmpty()){ //Verifica si la lista esta vacía
            //Si esta vacía lanza una excepción
            throw new MensajeException("Lista enlazada vacía, no hay elementos.");
        }
        int posicion = 0; //Se crea una variable posicion que cuente la posicion del nodo
        Nodo<E> nodoTemporal = cabecera.getSiguiente(); //Se asigna el valor del primer nodo a la variable nodoTemporal
        while(nodoTemporal != null){ //Ciclo que verifica que no se termino de recorrer la lista
            if (nodoTemporal.getValor().equals(valor)){ //Si el valor del nodoTemporal es igual al valor que se busca
                return posicion; //Retorna la posicion del nodo si son iguales
            }
            nodoTemporal = nodoTemporal.getSiguiente(); //NodoTemporal ahora tiene el valor del siguiente nodo
            posicion = posicion + 1; //La posicion aumenta en uno
        }
        //throw new MensajeException("No se encontró el valor en la lista enlazada.");
        return -1;//Caso contrario retorna -1, no se encontro el elemento o  el nodo
    }

    //Busca el k-ésimo elemento por su posición y retorna el nodo (valor)
    public E searchK(int k) throws MensajeException{
        if (isEmpty()){ //Verifica si la lista esta vacía
            //Si esta vacía lanza una excepción
            throw new MensajeException("Lista enlazada vacía, no hay elementos.");
        }
        else if (k < 0 || k >= length()) {
            throw new MensajeException("Posición fuera de rango.");
        }
        int posicionK = 0; //Se crea una variable posicion que cuente la cantidad de nodos
        Nodo<E> nodoTemporal = cabecera.getSiguiente(); //Se asigna el valor del primer nodo a la variable nodoTemporal
        while(nodoTemporal != null){ //Ciclo que verifica que no se termino de recorrer la lista
            if (posicionK == k){ //Compara la posicion actual del nodoTemporal con k
                return nodoTemporal.getValor(); //Retorna el valor del nodoTemporal o del nodo en la posicion K
            }
            nodoTemporal = nodoTemporal.getSiguiente(); //NodoTemporal ahora tiene el valor del siguiente nodo
            posicionK = posicionK + 1; //Y  posicion se incrementa en uno
        }
        throw new MensajeException("No se encontró el valor en la lista enlazada.");
        //return null;
    }

    /*
    Método creado para poder trabajar con la misma lógica del searchk pero para usar en nodos
    y que no salga error en los métodos insert o remove,
    en vez de retornar el valor del nodo, retorna el nodo si lo encuentra
     */
    public Nodo<E> searchNodoK(int k) throws MensajeException {
        if (isEmpty()) { //Verifica si la lista esta vacía
            throw new MensajeException("Lista enlazada vacía."); //Si esta vacía lanza una excepción
        } else if (k < 0 || k >= length()) {
            throw new MensajeException("Posición fuera de rango.");
        }
        int posicionK = 0; //Se crea una variable posicion que cuente la cantidad de nodos
        Nodo<E> nodoTemporal = cabecera.getSiguiente(); //Se asigna el valor del primer nodo a la variable nodoTemporal
        while (nodoTemporal != null) { //Ciclo que verifica que no se termino de recorrer la lista
            if (posicionK == k) { //Compara la posicion actual del nodoTemporal con k
                return nodoTemporal; //Retorna el nodo y no el valor
            }
            nodoTemporal = nodoTemporal.getSiguiente(); //NodoTemporal ahora tiene el valor del siguiente nodo
            posicionK = posicionK + 1; //Y  posicion se incrementa en uno
        }
        throw new MensajeException("No se encontró el nodo en la lista enlazada.");
    }

    //Inserta el nuevo nodo al inicio de la lista
    public void insertFirst(E nuevo){
        Nodo<E> nuevoNodo = new Nodo<E> (nuevo); //Se crea un nuevo nodo con el valor del nuevo elemento
        nuevoNodo.setSiguiente(cabecera.getSiguiente()); // El nuevo nodo apunta al siguiente de cabecera
        cabecera.setSiguiente(nuevoNodo); //Y la cabecera apunta al nuevo nodo
    }

    //Inserta el nuevo nodo a una posicion x en la lista
    public void insertPosicionK(E nuevo, int posicionK) throws MensajeException{
        Nodo<E> nuevoNodo = new Nodo<E> (nuevo); //Se crea un nuevo nodo con el valor del nuevo elemento
        if (isEmpty() || posicionK == 0){ //Si la lista esta vacía
            insertFirst(nuevo); //Se inserta el nuevo nodo al inicio
        }
        else if (posicionK < 0 || posicionK > length()) {
            throw new MensajeException("Posición fuera de rango.");
        }
        else{ //Caso contrario
            Nodo<E> nodoAnterior = searchNodoK(posicionK-1); //El nodo anterior es el nodo anterior al nodo del medio
            Nodo<E> nodoMedio = searchNodoK(posicionK); //El nodo del medio se refiere a el nodo el cual se desplazara de su posición
            nuevoNodo.setSiguiente(nodoMedio); //El nuevo nodo apuntara al nodo medio (nodo desplazado)
            nodoAnterior.setSiguiente(nuevoNodo); //El nodo anterior apuntara al nuevo nodo
        }
    }

    //Inserta el nuevo nodo al final de la lista
    public void insertLast(E nuevo) throws MensajeException{
        Nodo<E> nuevoNodo = new Nodo<E> (nuevo); //Se crea un nuevo nodo con el nuevo elemento
        nuevoNodo.setSiguiente(null); //El siguiente del nuevo nodo apunta a null
        if (isEmpty()){ //Si la lista esta vacía
            //nuevoNodo.setSiguiente(null);
            cabecera.setSiguiente(nuevoNodo); //La cabecera apunta al nuevo nodo
        }
        else{ //Caso contrario la lista no este vacía
            //nuevoNodo.setSiguiente(null);
            Nodo<E> nodoAnterior = searchNodoK(length()-1);//El nodo anterior sera el nodo que tenga la ultima posicion de la lista
            nodoAnterior.setSiguiente(nuevoNodo); //Y el anterior nodo ultimo apunta al nuevo nodo
        }
    }

    //Elimina un nodo de la lista enlazada por contenido (valor)
    public void removeNode(E nodo) throws MensajeException{
        if (isEmpty()){ //Verifica si la lista esta vacía
            //Si esta vacía lanza una excepción
            throw new MensajeException("Lista enlazada vacía, no hay elementos que eliminar.");
        }
        int posicionNodo = search(nodo); //La variable posicionNodo sera la posicion del nodo a eliminar en la lista
        removeNodeK(posicionNodo);
    }

    //Elimina un nodo de la lista enlazada por posición
    public void removeNodeK(int posicionK) throws MensajeException{
        if (isEmpty()){ //Verifica si la lista esta vacía
            //Si esta vacía lanza una excepción
            throw new MensajeException("Lista enlazada vacía, no hay elementos que eliminar.");
        }
        else if (posicionK < 0 || posicionK >= length()) {
            throw new MensajeException("Posición fuera de rango.");
        } else if (posicionK == 0) {
            cabecera.setSiguiente(cabecera.getSiguiente().getSiguiente());
            return;
        }
        Nodo<E> nodoEliminado = searchNodoK(posicionK); //nodoEliminado es el nodo en la posicion del nodo que se quiere eliminar
        if (nodoEliminado != null){
            Nodo<E> nodoAnterior = searchNodoK(posicionK-1); //El nodo anterior es el nodo anterior que apunta al nodo que se quiere eliminar
            nodoAnterior.setSiguiente(nodoEliminado.getSiguiente()); //El nodo anterior al nodo eliminado apunta al siguiente del nodo eliminado
        }
    }

    //Recorrido de la lista enlazada
    public void recorridoLista() {
        Nodo<E> nodoTemporal = cabecera.getSiguiente(); //Se asigna el valor del primer nodo a la variable nodoTemporal
        while(nodoTemporal != null){ //Ciclo que verifica que no se termino de recorrer la lista
            System.out.println(nodoTemporal.getValor()); //Imprime el valor de cada elemento de la lista
            nodoTemporal = nodoTemporal.getSiguiente(); //Y el nodo actual pasa a tener el valor del siguiente del nodo actual
        }
    }

    //Imprime la lista enlazada
    public void print() throws MensajeException{
        if (isEmpty()){ //Si la lista esta vacía
            throw new MensajeException("Lista vacía, no se puede imprimir nada."); //Lanza un mensaje
        }
        System.out.println(toString());
    }

    public String toString() {
        Nodo<E> nodoTemporal = cabecera.getSiguiente(); //Se asigna el valor del primer nodo a la variable nodoTemporal
        StringBuilder lista = new StringBuilder();
        lista.append("[");
        while(nodoTemporal != null){ //Ciclo que verifica que no se termino de recorrer la lista
            lista.append(nodoTemporal.getValor());
            if (nodoTemporal.getSiguiente() != null) {
                lista.append(", ");
            }
            nodoTemporal = nodoTemporal.getSiguiente(); //Y el nodo actual pasa a tener el valor del siguiente del nodo actual
        }
        lista.append("]");
        return lista.toString();
    }
}
