import java.util.*;

public class Grafo {
    final Map<String, Ubicacion> ubicaciones;

    public Grafo() {
        this.ubicaciones = new HashMap<>();
    }

    //==========================OPERACIONES BÁSICAS==========================

    //agrega una nueva ubicación si no existe ya
    public void agregarUbicacion(String nombre) {
        ubicaciones.putIfAbsent(nombre, new Ubicacion(nombre));
    }

    //elimina una ubicación y todas las rutas que lleven a ella
    public void eliminarUbicacion(String nombre) {
        ubicaciones.remove(nombre);
        for (Ubicacion u : ubicaciones.values()) {
            u.rutas.removeIf(r -> r.destino.nombre.equals(nombre));
        }
    }

    //agrega una ruta desde origen a destino con peso si ambos existen
    public void agregarRuta(String origen, String destino, double peso) {
        Ubicacion uOrigen = ubicaciones.get(origen);
        Ubicacion uDestino = ubicaciones.get(destino);
        if (uOrigen != null && uDestino != null && peso > 0) {
            uOrigen.rutas.add(new Ruta(uDestino, peso));
        }
    }

    //elimina una ruta específica desde el nodo origen
    public void eliminarRuta(String origen, String destino) {
        Ubicacion uOrigen = ubicaciones.get(origen);
        if (uOrigen != null) {
            uOrigen.rutas.removeIf(r -> r.destino.nombre.equals(destino));
        }
    }

    //devuelve una ubicación por su nombre
    public Ubicacion getUbicacion(String nombre) {
        return ubicaciones.get(nombre);
    }

    //devuelve todos los nombres de ubicaciones del grafo
    public Set<String> getNombresUbicaciones() {
        return ubicaciones.keySet();
    }

    //==========================VISUALIZACIÓN==========================

    //devuelve una representación de texto del grafo
    public String mostrarGrafoComoTexto() {
        StringBuilder sb = new StringBuilder();
        for (Ubicacion u : ubicaciones.values()) {
            sb.append(u.nombre).append(" -> ");
            for (Ruta r : u.rutas) {
                sb.append(r.destino.nombre).append(" (").append(r.peso).append(") ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    //==========================RECORRIDOS==========================

    //búsqueda en anchura desde una ubicación dada
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
                if (visitado.add(vecino)) {
                    cola.add(vecino);
                }
            }
        }
        System.out.println();
    }

    //búsqueda en profundidad desde una ubicación dada
    public void dfs(String inicio) {
        Set<String> visitado = new HashSet<>();
        System.out.println("🔍 DFS desde: " + inicio);
        dfsRec(inicio, visitado);
        System.out.println();
    }

    //recorrido recursivo para DFS
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

    //==========================ANÁLISIS==========================

    //verifica si hay ciclos en el grafo
    public boolean hayCiclo() {
        Set<String> visitado = new HashSet<>();
        Set<String> enRecursion = new HashSet<>();
        for (String nodo : ubicaciones.keySet()) {
            if (hayCicloDFS(nodo, visitado, enRecursion)) return true;
        }
        return false;
    }

    //función auxiliar para detectar ciclos usando DFS
    private boolean hayCicloDFS(String actual, Set<String> visitado, Set<String> enRecursion) {
        if (enRecursion.contains(actual)) return true;
        if (visitado.contains(actual)) return false;

        visitado.add(actual);
        enRecursion.add(actual);

        for (Ruta r : ubicaciones.get(actual).rutas) {
            if (hayCicloDFS(r.destino.nombre, visitado, enRecursion)) return true;
        }

        enRecursion.remove(actual);
        return false;
    }

    //encuentra todas las componentes conexas del grafo
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

    //función auxiliar para componentes conexas
    private void componenteDFS(String actual, Set<String> componente, Set<String> visitado) {
        visitado.add(actual);
        componente.add(actual);
        for (Ruta r : ubicaciones.get(actual).rutas) {
            if (!visitado.contains(r.destino.nombre)) {
                componenteDFS(r.destino.nombre, componente, visitado);
            }
        }
    }

    //devuelve una lista de zonas sin conexiones
    public List<String> zonasAisladas() {
        List<String> aisladas = new ArrayList<>();
        Map<String, Integer> entradas = new HashMap<>();

        //inicializa el número de entradas en 0
        for (String nombre : ubicaciones.keySet()) {
            entradas.put(nombre, 0);
        }

        //cuenta cuántas entradas tiene cada nodo
        for (Ubicacion u : ubicaciones.values()) {
            for (Ruta r : u.rutas) {
                entradas.put(r.destino.nombre, entradas.getOrDefault(r.destino.nombre, 0) + 1);
            }
        }

        //verifica quiénes no tienen entradas ni salidas
        for (String nombre : ubicaciones.keySet()) {
            Ubicacion u = ubicaciones.get(nombre);
            if (u.rutas.isEmpty() && entradas.get(nombre) == 0) {
                aisladas.add(nombre);
            }
        }

        return aisladas;
    }
}
