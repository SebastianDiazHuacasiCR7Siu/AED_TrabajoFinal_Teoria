import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

public class MainApp extends Application {

    private Grafo grafo = new Grafo();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Inventario de Almacén");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 15;");

        Label titulo = new Label("Gestión de Inventario con Grafo y Árbol B");
        titulo.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        TextArea output = new TextArea();
        output.setEditable(false);
        output.setPrefHeight(350);

        TextField txtUbicacion = new TextField();
        txtUbicacion.setPromptText("Nombre de la nueva ubicación");
        Button btnAgregarUbicacion = new Button("Agregar Ubicación");
        btnAgregarUbicacion.setOnAction(e -> {
            String nombre = txtUbicacion.getText().trim();
            if (!nombre.isEmpty()) {
                grafo.agregarUbicacion(nombre);
                output.appendText("✅ Ubicación agregada: " + nombre + "\n");
                txtUbicacion.clear();
            }
        });
        HBox filaAgregarUbicacion = new HBox(10, txtUbicacion, btnAgregarUbicacion);

        TextField txtOrigen = new TextField(); txtOrigen.setPromptText("Origen");
        TextField txtDestino = new TextField(); txtDestino.setPromptText("Destino");
        TextField txtPeso = new TextField(); txtPeso.setPromptText("Distancia");
        Button btnAgregarRuta = new Button("Agregar Ruta");
        btnAgregarRuta.setOnAction(e -> {
            try {
                String origen = txtOrigen.getText().trim();
                String destino = txtDestino.getText().trim();
                double peso = Double.parseDouble(txtPeso.getText().trim());
                grafo.agregarRuta(origen, destino, peso);
                output.appendText("🔗 Ruta agregada: " + origen + " -> " + destino + " (" + peso + ")\n");
                txtOrigen.clear(); txtDestino.clear(); txtPeso.clear();
            } catch (Exception ex) {
                output.appendText("⚠️ Error al agregar ruta. Verifica los datos.\n");
            }
        });
        HBox filaAgregarRuta = new HBox(10, txtOrigen, txtDestino, txtPeso, btnAgregarRuta);

        TextField txtUbicProducto = new TextField(); txtUbicProducto.setPromptText("Ubicación");
        TextField txtClaveProducto = new TextField(); txtClaveProducto.setPromptText("Clave Producto");
        Button btnInsertarProducto = new Button("Insertar Producto");
        btnInsertarProducto.setOnAction(e -> {
            String ubic = txtUbicProducto.getText().trim();
            try {
                int clave = Integer.parseInt(txtClaveProducto.getText().trim());
                Ubicacion u = grafo.getUbicacion(ubic);
                if (u != null) {
                    u.productos.insertar(clave);
                    output.appendText("📦 Producto " + clave + " insertado en " + ubic + "\n");
                } else {
                    output.appendText("⚠️ Ubicación no encontrada: " + ubic + "\n");
                }
                txtUbicProducto.clear(); txtClaveProducto.clear();
            } catch (Exception ex) {
                output.appendText("⚠️ Error al insertar producto.\n");
            }
        });
        HBox filaInsertarProducto = new HBox(10, txtUbicProducto, txtClaveProducto, btnInsertarProducto);

        TextField txtMostrarArbol = new TextField();
        txtMostrarArbol.setPromptText("Ubicación a mostrar productos");
        Button btnMostrarArbol = new Button("Ver productos en ubicación");
        btnMostrarArbol.setOnAction(e -> {
            String nombre = txtMostrarArbol.getText().trim();
            Ubicacion u = grafo.getUbicacion(nombre);
            if (u != null) {
                output.appendText("\n🌳 Productos en " + nombre + ":\n");
                u.productos.mostrar();
            } else {
                output.appendText("⚠️ Ubicación no encontrada: " + nombre + "\n");
            }
        });
        HBox filaMostrarArbol = new HBox(10, txtMostrarArbol, btnMostrarArbol);

        Button btnMostrarGrafo = new Button("Ver mapa del almacén");
        btnMostrarGrafo.setOnAction(e -> {
            output.appendText("\n🗺️ Mapa del Almacén:\n");
            output.appendText(grafo.mostrarGrafoComoTexto());
        });

        Button btnVisualizarGrafo = new Button("🖼 Ver grafo visual");
        btnVisualizarGrafo.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setTitle("Vista del Grafo del Almacén");

            GrafoCanvas canvas = new GrafoCanvas(grafo);
            Scene scene = new Scene(new StackPane(canvas), 850, 650);

            stage.setScene(scene);
            stage.show();
        });

        Button btnDijkstra = new Button("Calcular rutas más cortas");
        btnDijkstra.setOnAction(e -> {
            output.appendText("\n📍 Rutas más cortas desde 'Entrada':\n");
            Map<String, Double> dist = Dijkstra.calcularDistancias(grafo, "Entrada");
            for (String d : dist.keySet()) output.appendText("A " + d + ": " + dist.get(d) + "\n");
        });

        Button btnBFS = new Button("Explorar almacén (rápido)");
        btnBFS.setOnAction(e -> {
            output.appendText("\n🔎 Exploración rápida desde 'Entrada':\n");
            grafo.bfs("Entrada");
        });

        Button btnDFS = new Button("Explorar almacén (profundo)");
        btnDFS.setOnAction(e -> {
            output.appendText("\n🔎 Exploración profunda desde 'Entrada':\n");
            grafo.dfs("Entrada");
        });

        Button btnCiclos = new Button("Verificar rutas ineficientes");
        btnCiclos.setOnAction(e -> output.appendText("\n🔁 ¿Existen rutas ineficientes?: " + grafo.hayCiclo() + "\n"));

        Button btnComponentes = new Button("Zonas conectadas del almacén");
        btnComponentes.setOnAction(e -> {
            output.appendText("\n🔗 Zonas conectadas:\n");
            for (Set<String> comp : grafo.componentesConexas())
                output.appendText("Zona: " + comp + "\n");
        });

        Button btnAisladas = new Button("Ubicaciones sin conexión");
        btnAisladas.setOnAction(e -> {
            output.appendText("\n🚫 Ubicaciones aisladas:\n");
            for (String z : grafo.zonasAisladas())
                output.appendText("Ubicación: " + z + "\n");
        });

        VBox filaAnalisis = new VBox(5, btnMostrarGrafo, btnVisualizarGrafo, btnDijkstra, btnBFS, btnDFS, btnCiclos, btnComponentes, btnAisladas);

        layout.getChildren().addAll(titulo, filaAgregarUbicacion, filaAgregarRuta, filaInsertarProducto, filaMostrarArbol, filaAnalisis, output);

        Scene scene = new Scene(layout, 950, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


