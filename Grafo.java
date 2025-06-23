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

    public String mostrarGrafo() {
        StringBuilder sb = new StringBuilder();
        sb.append("📌 Mapa del Almacén:\n");
        for (Ubicacion u : ubicaciones.values()) {
            sb.append("📦 ").append(u.nombre).append(" ➝ ");
            if (u.rutas.isEmpty()) {
                sb.append("🚫 sin conexiones");
            } else {
                for (Ruta r : u.rutas) {
                    sb.append("➡ ").append(r.destino.nombre).append(" (").append(r.peso).append(")  ");
                }
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

    public String bfs(String inicio) {
        StringBuilder recorrido = new StringBuilder("🔍 BFS desde '" + inicio + "':\n");
        Set<String> visitado = new HashSet<>();
        Queue<String> cola = new LinkedList<>();

        cola.add(inicio);
        visitado.add(inicio);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            recorrido.append("📦 ").append(actual).append(" ➝ ");

            for (Ruta r : ubicaciones.get(actual).rutas) {
                String vecino = r.destino.nombre;
                if (!visitado.contains(vecino)) {
                    visitado.add(vecino);
                    cola.add(vecino);
                }
            }
            recorrido.append("\n");
        }
        return recorrido.toString();
    }

    public String dfs(String inicio) {
        StringBuilder recorrido = new StringBuilder("🔍 DFS desde '" + inicio + "':\n");
        Set<String> visitado = new HashSet<>();
        dfsRec(inicio, visitado, recorrido);
        return recorrido.toString();
    }

    private void dfsRec(String actual, Set<String> visitado, StringBuilder recorrido) {
        visitado.add(actual);
        recorrido.append("📦 ").append(actual).append(" ➝ ");

        for (Ruta r : ubicaciones.get(actual).rutas) {
            String vecino = r.destino.nombre;
            if (!visitado.contains(vecino)) {
                dfsRec(vecino, visitado, recorrido);
            }
        }
        recorrido.append("\n");
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
                componenteBidireccionalDFS(nodo, componente, visitado);
                componentes.add(componente);
            }
        }
        return componentes;
    }

    private void componenteBidireccionalDFS(String actual, Set<String> componente, Set<String> visitado) {
        visitado.add(actual);
        componente.add(actual);

        // Ver rutas salientes
        for (Ruta r : ubicaciones.get(actual).rutas) {
            String vecino = r.destino.nombre;
            if (!visitado.contains(vecino)) {
                componenteBidireccionalDFS(vecino, componente, visitado);
            }
        }

        // Ver rutas entrantes
        for (Map.Entry<String, Ubicacion> entry : ubicaciones.entrySet()) {
            String posibleOrigen = entry.getKey();
            Ubicacion u = entry.getValue();
            for (Ruta r : u.rutas) {
                if (r.destino.nombre.equals(actual) && !visitado.contains(posibleOrigen)) {
                    componenteBidireccionalDFS(posibleOrigen, componente, visitado);
                }
            }
        }
    }

    public List<String> zonasAisladas() {
        List<String> aisladas = new ArrayList<>();

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
        ubicaciones.remove(nombre);

        for (Ubicacion u : ubicaciones.values()) {
            u.rutas.removeIf(ruta -> ruta.destino.nombre.equals(nombre));
        }
    }

    public void modificarRuta(String origen, String destino, double nuevoPeso) {
        Ubicacion uOrigen = ubicaciones.get(origen);
        if (uOrigen != null) {
            for (Ruta r : uOrigen.rutas) {
                if (r.destino.nombre.equals(destino)) {
                    r.peso = nuevoPeso;
                    break;
                }
            }
        }
    }


    public void modificarUbicacion(String actual, String nuevo) {
        if (!ubicaciones.containsKey(actual) || ubicaciones.containsKey(nuevo)) return;

        Ubicacion u = ubicaciones.remove(actual);
        u.nombre = nuevo;
        ubicaciones.put(nuevo, u);

        // Actualizar referencias en rutas
        for (Ubicacion origen : ubicaciones.values()) {
            for (Ruta r : origen.rutas) {
                if (r.destino == u) {
                    r.destino = u;
                }
            }
        }
    }

}
