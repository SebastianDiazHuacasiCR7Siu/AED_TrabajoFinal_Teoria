# Sistema de Gestion y Optimizacion de Inventarios en Almacenes

Proyecto academico desarrollado para el curso Algoritmos y Estructura de Datos en la Universidad Catolica de Santa Maria (Arequipa, Peru).
El sistema permite gestionar un almacen simulando ubicaciones conectadas mediante rutas, usando estructuras de datos como grafos y arboles B+.

## Funcionalidades principales

* Gestion de ubicaciones del almacen (agregar, eliminar, modificar).
* Gestion de rutas entre ubicaciones con pesos (distancias).
* Insercion, visualizacion, busqueda y eliminacion de productos por ubicacion (usando Arbol B+).
* Deteccion automatica de ciclos en el grafo (rutas ineficientes).
* Exploracion de rutas mediante BFS y DFS.
* Calculo de rutas mas cortas usando el algoritmo de Dijkstra.
* Visualizacion interactiva del mapa del almacen.
* Interfaz grafica intuitiva desarrollada con JavaFX.

## Creditos por area

| Integrante              | Rol         | Aporte principal                                                              |
| ----------------------- | ----------- | ----------------------------------------------------------------------------- |
| Antonio Arquino Tejada  | Programador | Estructura del grafo, logica de rutas, simulacion de escenarios               |
| Sebastian Diaz Huacasi  | Programador | Implementacion de algoritmos: BFS, DFS, Dijkstra, deteccion de ciclos         |
| Giancarlo Huerta Fabian | Programador | Interfaz grafica (JavaFX), ajustes visuales, estructura portable del proyecto |
| Xiomara Ortiz Puma      | Programador | Arbol B+ para productos, documentacion tecnica                                |

## Requisitos del sistema

* Java 17 o superior
* Sistema operativo: Windows (recomendado para compatibilidad con .bat)
* Un editor de codigo (VSCode recomendado)

## Instrucciones de instalación y uso

Este proyecto es totalmente portable. Ya incluye el SDK de JavaFX, las librerías necesarias y los archivos de configuración.

Abrir una terminal en la raíz del proyecto.

Ejecutar el archivo compilar.bat:

./compilar.bat

Esto compilará y lanzará automáticamente la interfaz gráfica del sistema de inventario.

Importante: No cambies de ubicación las carpetas bin, lib, javafx ni el archivo compilar.bat sin actualizar las rutas internas del script.

## Observaciones

* El proyecto es totalmente portable. No requiere configuracion adicional si las rutas estan correctamente establecidas.
* En caso de errores de ejecucion, verificar que tu JAVA\_HOME este bien configurado y que tengas Java 17+ instalado.

UCSM - Escuela Profesional de Ingenieria de Sistemas
Junio 2025
