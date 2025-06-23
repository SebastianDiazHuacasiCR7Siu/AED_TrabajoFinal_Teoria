import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoCanvas extends Canvas {
    private Grafo grafo;
    private Map<String, Double[]> posiciones; // Posiciones X,Y para cada nodo

    public GrafoCanvas(Grafo grafo) {
        super(800, 600);
        this.grafo = grafo;
        this.posiciones = new HashMap<>();
        generarPosiciones();
        dibujar();
    }

    private void generarPosiciones() {
        double x = 100;
        double y = 100;
        int i = 0;
        for (String nombre : grafo.getNombresUbicaciones()) {
            posiciones.put(nombre, new Double[]{x + (i % 4) * 150, y + (i / 4) * 150});
            i++;
        }
    }

    public void dibujar() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        // Dibujar aristas
        for (String origen : grafo.getNombresUbicaciones()) {
            Double[] posOrigen = posiciones.get(origen);
            List<Ruta> rutas = grafo.getUbicacion(origen).rutas;
            for (Ruta r : rutas) {
                Double[] posDestino = posiciones.get(r.destino.nombre);
                gc.setStroke(Color.GRAY);
                gc.setLineWidth(2);
                gc.strokeLine(posOrigen[0], posOrigen[1], posDestino[0], posDestino[1]);
                // Texto de distancia
                double mx = (posOrigen[0] + posDestino[0]) / 2;
                double my = (posOrigen[1] + posDestino[1]) / 2;
                gc.setFill(Color.BLUE);
                gc.fillText(String.valueOf(r.peso), mx, my);
            }
        }

        // Dibujar nodos
        for (String nombre : grafo.getNombresUbicaciones()) {
            Double[] pos = posiciones.get(nombre);
            gc.setFill(Color.LIGHTGREEN);
            gc.fillOval(pos[0] - 20, pos[1] - 20, 40, 40);
            gc.setStroke(Color.BLACK);
            gc.strokeOval(pos[0] - 20, pos[1] - 20, 40, 40);
            gc.setFill(Color.BLACK);
            gc.setFont(new Font(12));
            gc.fillText(nombre, pos[0] - 30, pos[1] + 35);
        }
    }

    public void actualizar() {
        generarPosiciones();
        dibujar();
    }
}
