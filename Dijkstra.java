
import java.util.*;

public class Dijkstra {
    public static Map<String, Double> calcularDistancias(Grafo grafo, String origen) {
        Map<String, Double> distancias = new HashMap<>();
        Set<String> visitados = new HashSet<>();
        PriorityQueue<NodoDistancia> cola = new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.distancia));

        for (String nombre : grafo.getNombresUbicaciones()) {
            distancias.put(nombre, Double.POSITIVE_INFINITY);
        }

        distancias.put(origen, 0.0);
        cola.add(new NodoDistancia(origen, 0.0));

        while (!cola.isEmpty()) {
            NodoDistancia actual = cola.poll();
            if (visitados.contains(actual.nombre)) continue;
            visitados.add(actual.nombre);

            Ubicacion uActual = grafo.getUbicacion(actual.nombre);
            for (Ruta r : uActual.rutas) {
                double nuevaDistancia = distancias.get(actual.nombre) + r.peso;
                String destino = r.destino.nombre;
                if (nuevaDistancia < distancias.get(destino)) {
                    distancias.put(destino, nuevaDistancia);
                    cola.add(new NodoDistancia(destino, nuevaDistancia));
                }
            }
        }

        return distancias;
    }

    private static class NodoDistancia {
        String nombre;
        double distancia;

        NodoDistancia(String nombre, double distancia) {
            this.nombre = nombre;
            this.distancia = distancia;
        }
    }
}
