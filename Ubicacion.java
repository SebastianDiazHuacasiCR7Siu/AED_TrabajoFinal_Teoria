
import java.util.ArrayList;
import java.util.List;

public class Ubicacion {
    String nombre;
    List<Ruta> rutas;
    BTree productos; // Árbol B para productos en esta ubicación

    public Ubicacion(String nombre) {
        this.nombre = nombre;
        this.rutas = new ArrayList<>();
        this.productos = new BTree();
    }

    @Override
    public String toString() {
        return nombre;
    }
}
