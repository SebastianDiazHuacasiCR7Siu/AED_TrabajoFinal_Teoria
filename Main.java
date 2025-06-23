// Main actualizado para cumplir requisitos adicionales del proyecto
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("📦 Proyecto: Modelado de Almacén como Grafo Dirigido y Árbol B");

        Grafo grafo = new Grafo(); // ✅ Representación basada en listas (NO matriz de adyacencia)

        // ✅ Representar ubicaciones del almacén como nodos
        grafo.agregarUbicacion("Entrada");
        grafo.agregarUbicacion("Pasillo 1");
        grafo.agregarUbicacion("Estante A");
        grafo.agregarUbicacion("Zona Carga");

        // ✅ Representar rutas como aristas dirigidas y ponderadas
        grafo.agregarRuta("Entrada", "Pasillo 1", 5);
        grafo.agregarRuta("Pasillo 1", "Estante A", 3);
        grafo.agregarRuta("Estante A", "Zona Carga", 7);
        grafo.agregarRuta("Pasillo 1", "Zona Carga", 4);

        System.out.println("\n🗺️  Mapa del Almacén:");
        grafo.mostrarGrafoComoTexto();

        // ✅ Distancias mínimas con Dijkstra
        System.out.println("\n📍 Distancias mínimas desde 'Entrada':");
        Map<String, Double> distancias = Dijkstra.calcularDistancias(grafo, "Entrada");
        for (String ubic : distancias.keySet()) {
            System.out.println("A " + ubic + ": " + distancias.get(ubic));
        }

        // ✅ Inventario en Estante A usando Árbol B (clasificación por lote/producto)
        System.out.println("\n🌳 Inventario en Estante A (Árbol B):");
        BTree inventarioEstanteA = grafo.getUbicacion("Estante A").productos;
        inventarioEstanteA.insertar(25); // Ejemplo: Lote 25
        inventarioEstanteA.insertar(10);
        inventarioEstanteA.insertar(15);
        inventarioEstanteA.insertar(30);
        inventarioEstanteA.insertar(5);
        inventarioEstanteA.mostrar();

        // ✅ Exploración del almacén con BFS y DFS
        System.out.println("\n🔎 BFS desde 'Entrada':");
        grafo.bfs("Entrada");

        System.out.println("\n🔎 DFS desde 'Entrada':");
        grafo.dfs("Entrada");

        // ✅ Detección de ciclos en rutas
        System.out.println("\n🔁 ¿Existen ciclos (rutas ineficientes)?: " + grafo.hayCiclo());

        // ✅ Componentes conexas (zonas conectadas)
        System.out.println("\n🔗 Componentes conexas:");
        List<Set<String>> componentes = grafo.componentesConexas();
        for (Set<String> comp : componentes) {
            System.out.println("Componente: " + comp);
        }

        // ✅ Zonas aisladas (sin rutas de entrada/salida)
        System.out.println("\n🚫 Zonas aisladas:");
        List<String> aisladas = grafo.zonasAisladas();
        for (String aislada : aisladas) {
            System.out.println("Ubicación sin conexiones: " + aislada);
        }

        // 🔄 Pruebas de modificación de nodos y aristas
        grafo.eliminarRuta("Pasillo 1", "Estante A");
        grafo.eliminarUbicacion("Zona Carga");

        System.out.println("\n📉 Grafo tras eliminar una ruta y una ubicación:");
        grafo.mostrarGrafoComoTexto();
    }
}

