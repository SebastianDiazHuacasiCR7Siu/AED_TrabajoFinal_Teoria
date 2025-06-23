import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

public class GrafoVisualizador {

    public static void mostrar(Grafo grafo) {
        System.setProperty("org.graphstream.ui", "swing"); // Usar backend swing

        Graph g = new SingleGraph("Mapa del Almacén");

        for (String nombre : grafo.getNombresUbicaciones()) {
            g.addNode(nombre).setAttribute("ui.label", nombre);
        }

        for (String origen : grafo.getNombresUbicaciones()) {
            for (Ruta r : grafo.getUbicacion(origen).rutas) {
                String destino = r.destino.nombre;
                String id = origen + "-" + destino;
                if (g.getEdge(id) == null) {
                    g.addEdge(id, origen, destino, true)
                     .setAttribute("ui.label", String.valueOf(r.peso));
                }
            }
        }

        g.setAttribute("ui.stylesheet", 
            "node { fill-color: #42a5f5; size: 20px; text-size: 16px; }" +
            "edge { text-size: 14px; }"
        );
        g.setAttribute("ui.quality");
        g.setAttribute("ui.antialias");

        Viewer viewer = g.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }
}
