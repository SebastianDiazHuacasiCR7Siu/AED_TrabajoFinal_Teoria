import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class MainApp extends Application {
    private Grafo grafo = new Grafo();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sistema de Inventario de Almacén");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titulo = new Label("📦 Gestión de Inventario con Grafo y Árbol B");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        titulo.setAlignment(Pos.CENTER);
        titulo.setMaxWidth(Double.MAX_VALUE);

        TextArea output = new TextArea();
        output.setPrefRowCount(15);
        output.setEditable(false);

        // 📍 Sección: Ubicaciones y Rutas
        TitledPane paneUbicaciones = new TitledPane();
        paneUbicaciones.setText("📍 Ubicaciones y Rutas");

        TextField txtUbic = new TextField(); txtUbic.setPromptText("Nombre de ubicación");
        Button btnAgregarUbic = new Button("Agregar Ubicación");
        btnAgregarUbic.setOnAction(e -> {
            String nombre = txtUbic.getText().trim();
            if (!nombre.isEmpty()) {
                grafo.agregarUbicacion(nombre);
                output.appendText("✅ La ubicación '" + nombre + "' fue creada correctamente.\n");
                txtUbic.clear();
            }
        });

        HBox filaUbicacion = new HBox(10, txtUbic, btnAgregarUbic);

        TextField txtOrigen = new TextField(); txtOrigen.setPromptText("Origen");
        TextField txtDestino = new TextField(); txtDestino.setPromptText("Destino");
        TextField txtDist = new TextField(); txtDist.setPromptText("Distancia");
        Button btnAgregarRuta = new Button("Agregar Ruta");
        btnAgregarRuta.setOnAction(e -> {
            try {
                String origen = txtOrigen.getText().trim();
                String destino = txtDestino.getText().trim();
                double peso = Double.parseDouble(txtDist.getText().trim());
                grafo.agregarRuta(origen, destino, peso);
                output.appendText("🖎 Ruta: " + origen + " → " + destino + " (" + peso + ")\n");
                txtOrigen.clear(); txtDestino.clear(); txtDist.clear();
            } catch (Exception ex) {
                output.appendText("⚠️ Error al agregar ruta\n");
            }
        });

        HBox filaRuta = new HBox(10, txtOrigen, txtDestino, txtDist, btnAgregarRuta);

        TextField txtDelRutaOrigen = new TextField(); txtDelRutaOrigen.setPromptText("Origen");
        TextField txtDelRutaDestino = new TextField(); txtDelRutaDestino.setPromptText("Destino");
        Button btnEliminarRuta = new Button("Eliminar Ruta");
        btnEliminarRuta.setOnAction(e -> {
            String o = txtDelRutaOrigen.getText().trim();
            String d = txtDelRutaDestino.getText().trim();
            grafo.eliminarRuta(o, d);
            output.appendText("🗑️ Ruta eliminada: " + o + " → " + d + "\n");
            txtDelRutaOrigen.clear(); txtDelRutaDestino.clear();
        });

        TextField txtDelUbic = new TextField(); txtDelUbic.setPromptText("Ubicación");
        Button btnEliminarUbic = new Button("Eliminar Ubicación");
        btnEliminarUbic.setOnAction(e -> {
            String u = txtDelUbic.getText().trim();
            grafo.eliminarUbicacion(u);
            output.appendText("🗑️ Ubicación eliminada: " + u + "\n");
            txtDelUbic.clear();
        });

        VBox boxEliminar = new VBox(10,
            new Label("✂️ Eliminar Rutas y Ubicaciones"),
            new HBox(10, txtDelRutaOrigen, txtDelRutaDestino, btnEliminarRuta),
            new HBox(10, txtDelUbic, btnEliminarUbic)
        );

        TextField txtUbicActual = new TextField(); txtUbicActual.setPromptText("Nombre actual");
        TextField txtUbicNuevo = new TextField(); txtUbicNuevo.setPromptText("Nuevo nombre");
        Button btnModificarUbic = new Button("Modificar Ubicación");

        btnModificarUbic.setOnAction(e -> {
            String actual = txtUbicActual.getText().trim();
            String nuevo = txtUbicNuevo.getText().trim();
            if (!actual.isEmpty() && !nuevo.isEmpty()) {
                grafo.modificarUbicacion(actual, nuevo);
                output.appendText("✏️ Ubicación modificada: " + actual + " → " + nuevo + "\n");
                txtUbicActual.clear(); txtUbicNuevo.clear();
            }
        });

        TextField txtModRutaOrigen = new TextField(); txtModRutaOrigen.setPromptText("Origen");
        TextField txtModRutaDestino = new TextField(); txtModRutaDestino.setPromptText("Destino");
        TextField txtNuevoPeso = new TextField(); txtNuevoPeso.setPromptText("Nuevo peso");

        Button btnModificarRuta = new Button("Modificar Ruta");
        btnModificarRuta.setOnAction(e -> {
            try {
                String o = txtModRutaOrigen.getText().trim();
                String d = txtModRutaDestino.getText().trim();
                double nuevoPeso = Double.parseDouble(txtNuevoPeso.getText().trim());
                grafo.modificarRuta(o, d, nuevoPeso);
                output.appendText("✏️ Ruta modificada: " + o + " → " + d + " con peso " + nuevoPeso + "\n");
                txtModRutaOrigen.clear(); txtModRutaDestino.clear(); txtNuevoPeso.clear();
            } catch (Exception ex) {
                output.appendText("⚠️ Error al modificar ruta\n");
            }
        });

        VBox boxModificar = new VBox(10,
            new Label("✏️ Modificar Ubicaciones y Rutas"),
            new HBox(10, txtUbicActual, txtUbicNuevo, btnModificarUbic),
            new HBox(10, txtModRutaOrigen, txtModRutaDestino, txtNuevoPeso, btnModificarRuta)
        );

        VBox boxUbicaciones = new VBox(10, filaUbicacion, filaRuta, boxEliminar, boxModificar);
        paneUbicaciones.setContent(boxUbicaciones);

        // 🍎 Sección: Productos
        TitledPane paneProductos = new TitledPane();
        paneProductos.setText("🍚 Productos");

        TextField txtUbicProd = new TextField(); txtUbicProd.setPromptText("Ubicación");
        TextField txtClave = new TextField(); txtClave.setPromptText("Clave Producto");
        Button btnInsertar = new Button("Insertar Producto");
        btnInsertar.setOnAction(e -> {
            try {
                String ubic = txtUbicProd.getText().trim();
                int clave = Integer.parseInt(txtClave.getText().trim());
                Ubicacion u = grafo.getUbicacion(ubic);
                if (u != null) {
                    u.productos.insertar(clave);
                    output.appendText("📦 Producto " + clave + " insertado en " + ubic + "\n");
                } else {
                    output.appendText("⚠️ Ubicación no encontrada\n");
                }
                txtUbicProd.clear(); txtClave.clear();
            } catch (Exception ex) {
                output.appendText("⚠️ Error al insertar producto\n");
            }
        });

        TextField txtMostrar = new TextField(); txtMostrar.setPromptText("Ubicación a consultar");
        Button btnMostrar = new Button("Ver productos");
        btnMostrar.setOnAction(e -> {
            String nombre = txtMostrar.getText().trim();
            Ubicacion u = grafo.getUbicacion(nombre);
            if (u != null) {
                output.appendText("🌳 Productos en " + nombre + ":\n" + u.productos.mostrarComoTexto());
            } else {
                output.appendText("⚠️ Ubicación no encontrada\n");
            }
        });

        TextField txtBuscarClave = new TextField(); txtBuscarClave.setPromptText("Clave a buscar");
        Button btnBuscar = new Button("Buscar Producto");
        btnBuscar.setOnAction(e -> {
            try {
                int clave = Integer.parseInt(txtBuscarClave.getText().trim());
                boolean encontrado = false;

                for (String nombre : grafo.getNombresUbicaciones()) {
                    Ubicacion u = grafo.getUbicacion(nombre);
                    if (u.productos.buscar(clave)) {
                        output.appendText("🔍 Producto " + clave + " encontrado en: " + nombre + "\n");
                        encontrado = true;
                        break;
                    }
                }

                if (!encontrado) {
                    output.appendText("❌ Producto " + clave + " no se encuentra en ninguna ubicación\n");
                }
                txtBuscarClave.clear();
            } catch (Exception ex) {
                output.appendText("⚠️ Error al buscar producto\n");
            }
        });

        TextField txtEliminarClave = new TextField(); txtEliminarClave.setPromptText("Clave a eliminar");
        Button btnEliminarProd = new Button("Eliminar Producto");
        btnEliminarProd.setOnAction(e -> {
            try {
                int clave = Integer.parseInt(txtEliminarClave.getText().trim());
                boolean eliminado = false;

                for (String nombre : grafo.getNombresUbicaciones()) {
                    Ubicacion u = grafo.getUbicacion(nombre);
                    if (u.productos.eliminar(clave)) {
                        output.appendText("🗑️ Producto " + clave + " eliminado de: " + nombre + "\n");
                        eliminado = true;
                        break;
                    }
                }

                if (!eliminado) {
                    output.appendText("⚠️ Producto " + clave + " no se encontró para eliminar\n");
                }
                txtEliminarClave.clear();
            } catch (Exception ex) {
                output.appendText("⚠️ Error al eliminar producto\n");
            }
        });

        VBox boxProductos = new VBox(10,
            new HBox(10, txtUbicProd, txtClave, btnInsertar),
            new HBox(10, txtMostrar, btnMostrar),
            new HBox(10, txtBuscarClave, btnBuscar),
            new HBox(10, txtEliminarClave, btnEliminarProd)
        );
        paneProductos.setContent(boxProductos);

        // 📊 Sección: Análisis
        TitledPane paneAnalisis = new TitledPane();
        paneAnalisis.setText("📊 Análisis del Almacén");

        Button btnMapa = new Button("🌐 Ver mapa del almacén");
        Button btnDijkstra = new Button("🔹 Rutas más cortas");
        Button btnBFS = new Button("🔍 Explorar BFS");
        Button btnDFS = new Button("🔎 Explorar DFS");
        Button btnCiclo = new Button("❓ ¿Hay ciclo?");
        Button btnConectadas = new Button("🔗 Zonas conectadas");
        Button btnAisladas = new Button("🚫 Ubicaciones aisladas");

        btnMapa.setOnAction(e -> {
            output.appendText("\n📌 Mapa del Almacén:\n");
            output.appendText(grafo.mostrarGrafo());
            GrafoVisualizador.mostrar(grafo);
        });

        btnDijkstra.setOnAction(e -> {
            output.appendText("\n🔹 Rutas más cortas desde 'Entrada':\n");
            Map<String, Double> dist = Dijkstra.calcularDistancias(grafo, "Entrada");
            for (String d : dist.keySet()) output.appendText("A " + d + ": " + dist.get(d) + "\n");
        });

        btnBFS.setOnAction(e -> output.appendText(grafo.bfs("Entrada")));
        btnDFS.setOnAction(e -> output.appendText(grafo.dfs("Entrada")));
        btnCiclo.setOnAction(e -> {
            boolean ciclo = grafo.hayCiclo();
            String mensaje = ciclo ? "⚠️ Sí, existen ciclos en el grafo.\n" : "✅ No hay ciclos, el grafo es eficiente.\n";
            output.appendText(mensaje);
        });

        btnConectadas.setOnAction(e -> {
            output.appendText("\n🔗 Zonas conectadas:\n");
            for (Set<String> c : grafo.componentesConexas()) output.appendText("Zona: " + c + "\n");
        });

        btnAisladas.setOnAction(e -> {
            output.appendText("\n🚫 Ubicaciones aisladas:\n");
            for (String z : grafo.zonasAisladas()) output.appendText("Ubicación: " + z + "\n");
        });

        GridPane gridBotones = new GridPane();
        gridBotones.setHgap(15);
        gridBotones.setVgap(15);
        gridBotones.setPadding(new Insets(10));
        gridBotones.setAlignment(Pos.TOP_LEFT);

        double anchoFijo = 180;
        List<Button> botonesAnalisis = List.of(
            btnMapa, btnDijkstra, btnBFS, btnDFS,
            btnCiclo, btnConectadas, btnAisladas
        );
        for (Button b : botonesAnalisis) b.setPrefWidth(anchoFijo);

        gridBotones.add(btnMapa, 0, 0);
        gridBotones.add(btnDijkstra, 1, 0);
        gridBotones.add(btnBFS, 2, 0);
        gridBotones.add(btnDFS, 3, 0);
        gridBotones.add(btnCiclo, 0, 1);
        gridBotones.add(btnConectadas, 1, 1);
        gridBotones.add(btnAisladas, 2, 1);

        paneAnalisis.setContent(gridBotones);

        root.getChildren().addAll(titulo, paneUbicaciones, paneProductos, paneAnalisis, output);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }
}
