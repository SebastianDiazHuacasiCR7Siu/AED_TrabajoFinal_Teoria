package btreeplus;

public class Nodo<E> {
    private E valor; //Información del nodo
    private Nodo<E> siguiente; //Referencia al siguiente nodo o null(si la lista esta vacía)

    //Constructor que inicializa la información del nodo con su valor y la referencia del
    //siguiente apunta a null
    public Nodo(E valor){
        this.valor = valor;
        this.siguiente = null;
    }

    public E getValor(){
        return valor;
    } //Retorna valor o la información del nodo

    public void setValor(E valor){
        this.valor = valor;
    } //Modifica el valor del nodo

    public Nodo<E> getSiguiente(){
        return siguiente;
    } //Retorna la referencia del siguiente del nodo

    public void setSiguiente(Nodo<E> siguiente){
        this.siguiente = siguiente;
    } //Modifica la referencia del siguiente al nodo
}
