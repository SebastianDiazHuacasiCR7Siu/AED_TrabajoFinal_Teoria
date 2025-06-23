
import java.util.*;

public class Grafo {
    private Map<String, Ubicacion> ubicaciones;

    public Grafo() {
        this.ubicaciones = new HashMap<>();
    }

    public void agregarUbicacion(String nombre) {
        if (!ubicaciones.containsKey(nombre)) {
            ubicaciones.put(nombre, new Ubicacion(nombre));
        }
    }

    public void agregarRuta(String origen, String destino, double peso) {
        Ubicacion uOrigen = ubicaciones.get(origen);
        Ubicacion uDestino = ubicaciones.get(destino);
        if (uOrigen != null && uDestino != null) {
            uOrigen.rutas.add(new Ruta(uDestino, peso));
        }
    }

    public String mostrarGrafoComoTexto() {
    StringBuilder sb = new StringBuilder();
    for (Ubicacion u : ubicaciones.values()) {
        sb.append(u.nombre).append(" -> ");
        for (Ruta r : u.rutas) {
            sb.append(r.destino.nombre).append("(").append(r.peso).append(") ");
        }
        sb.append("\n");
    }
    return sb.toString();
}

    public Ubicacion getUbicacion(String nombre) {
        return ubicaciones.get(nombre);
    }

    public Set<String> getNombresUbicaciones() {
        return ubicaciones.keySet();
    }

    public void bfs(String inicio) {
    Set<String> visitado = new HashSet<>();
    Queue<String> cola = new LinkedList<>();

    cola.add(inicio);
    visitado.add(inicio);

    System.out.println("🔍 BFS desde: " + inicio);
    while (!cola.isEmpty()) {
        String actual = cola.poll();
        System.out.print(actual + " ");

        for (Ruta r : ubicaciones.get(actual).rutas) {
            String vecino = r.destino.nombre;
            if (!visitado.contains(vecino)) {
                visitado.add(vecino);
                cola.add(vecino);
            }
        }
    }
    System.out.println();
}

    public void dfs(String inicio) {
    Set<String> visitado = new HashSet<>();
    System.out.println("🔍 DFS desde: " + inicio);
    dfsRec(inicio, visitado);
    System.out.println();
}

    private void dfsRec(String actual, Set<String> visitado) {
    visitado.add(actual);
    System.out.print(actual + " ");

    for (Ruta r : ubicaciones.get(actual).rutas) {
        String vecino = r.destino.nombre;
        if (!visitado.contains(vecino)) {
            dfsRec(vecino, visitado);
        }
    }
}

    public boolean hayCiclo() {
    Set<String> visitado = new HashSet<>();
    Set<String> enRecursion = new HashSet<>();

    for (String nodo : ubicaciones.keySet()) {
        if (hayCicloDFS(nodo, visitado, enRecursion)) {
            return true;
        }
    }
    return false;
}

    private boolean hayCicloDFS(String actual, Set<String> visitado, Set<String> enRecursion) {
    if (enRecursion.contains(actual)) return true;
    if (visitado.contains(actual)) return false;

    visitado.add(actual);
    enRecursion.add(actual);

    for (Ruta r : ubicaciones.get(actual).rutas) {
        if (hayCicloDFS(r.destino.nombre, visitado, enRecursion)) {
            return true;
        }
    }

    enRecursion.remove(actual);
    return false;
}

    public List<Set<String>> componentesConexas() {
    Set<String> visitado = new HashSet<>();
    List<Set<String>> componentes = new ArrayList<>();

    for (String nodo : ubicaciones.keySet()) {
        if (!visitado.contains(nodo)) {
            Set<String> componente = new HashSet<>();
            componenteDFS(nodo, componente, visitado);
            componentes.add(componente);
        }
    }
    return componentes;
}

    private void componenteDFS(String actual, Set<String> componente, Set<String> visitado) {
    visitado.add(actual);
    componente.add(actual);
    for (Ruta r : ubicaciones.get(actual).rutas) {
        if (!visitado.contains(r.destino.nombre)) {
            componenteDFS(r.destino.nombre, componente, visitado);
        }
    }
}

        public List<String> zonasAisladas() {
    List<String> aisladas = new ArrayList<>();

    // Calcular nodos con cero entradas y salidas
    Map<String, Integer> entradas = new HashMap<>();
    for (String nombre : ubicaciones.keySet()) {
        entradas.put(nombre, 0);
    }

    for (Ubicacion u : ubicaciones.values()) {
        for (Ruta r : u.rutas) {
            entradas.put(r.destino.nombre, entradas.getOrDefault(r.destino.nombre, 0) + 1);
        }
    }

    for (String nombre : ubicaciones.keySet()) {
        Ubicacion u = ubicaciones.get(nombre);
        if (u.rutas.isEmpty() && entradas.get(nombre) == 0) {
            aisladas.add(nombre);
        }
    }

    return aisladas;
}

public void eliminarRuta(String origen, String destino) {
    Ubicacion uOrigen = ubicaciones.get(origen);
    if (uOrigen != null) {
        uOrigen.rutas.removeIf(ruta -> ruta.destino.nombre.equals(destino));
    }
}

public void eliminarUbicacion(String nombre) {
    // Elimina la ubicación del mapa
    ubicaciones.remove(nombre);

    // Elimina todas las rutas entrantes a esa ubicación
    for (Ubicacion u : ubicaciones.values()) {
        u.rutas.removeIf(ruta -> ruta.destino.nombre.equals(nombre));
    }
}

}
