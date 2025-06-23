import java.util.*;

public class Dijkstra {
    //calcula la distancia más corta desde el nodo origen a todos los demás
    public static Map<String, Double> calcularDistancias(Grafo grafo, String origen) {
        Map<String, Double> distancias = new HashMap<>();
        Set<String> visitados = new HashSet<>();
        PriorityQueue<NodoDistancia> cola = new PriorityQueue<>();

        //inicializa las distancias a infinito
        for (String nombre : grafo.getNombresUbicaciones()) {
            distancias.put(nombre, Double.POSITIVE_INFINITY);
        }

        //si el origen no está en el grafo retorno el mapa vacío
        if (!distancias.containsKey(origen)) return distancias;

        //el origen tiene distancia 0
        distancias.put(origen, 0.0);
        cola.add(new NodoDistancia(origen, 0.0));

        while (!cola.isEmpty()) {
            NodoDistancia actual = cola.poll();

            if (visitados.contains(actual.nombre)) continue;
            visitados.add(actual.nombre);

            Ubicacion uActual = grafo.getUbicacion(actual.nombre);
            if (uActual == null || uActual.rutas == null) continue;

            for (Ruta r : uActual.rutas) {
                String destino = r.destino.nombre;
                double nuevaDistancia = distancias.get(actual.nombre) + r.peso;

                if (nuevaDistancia < distancias.get(destino)) {
                    distancias.put(destino, nuevaDistancia);
                    cola.add(new NodoDistancia(destino, nuevaDistancia));
                }
            }
        }

        return distancias;
    }

    //estructura para la cola de prioridad
    private static class NodoDistancia implements Comparable<NodoDistancia> {
        String nombre;
        double distancia;

        NodoDistancia(String nombre, double distancia) {
            this.nombre = nombre;
            this.distancia = distancia;
        }

        //ordenar por menor distancia
        @Override
        public int compareTo(NodoDistancia otro) {
            return Double.compare(this.distancia, otro.distancia);
        }
    }
}
