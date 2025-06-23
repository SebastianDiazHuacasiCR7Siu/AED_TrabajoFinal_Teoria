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
* JavaFX SDK 21.0.7
* Sistema operativo: Windows (recomendado para compatibilidad con .bat)
* Un editor de codigo (VSCode recomendado)

## Estructura del proyecto

.
├── bin/                 -> Archivos compilados (.class)
├── lib/                 -> Librerias externas necesarias (JavaFX, GraphStream)
├── src/                 -> Codigo fuente (.java)
├── .vscode/             -> Configuracion del entorno para VSCode (opcional)
├── compilar.bat         -> Script para compilar y ejecutar automaticamente
└── README.md            -> Este archivo

## Instrucciones de instalacion y ejecucion

1. Descargar y configurar JavaFX

* Descargar JavaFX SDK 21.0.7 desde: [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)
* Extraerlo y colocar la carpeta javafx-sdk-21.0.7 dentro de lib/ (o ajustar la ruta en el .bat)

2. Ejecutar el sistema

* Abrir una terminal en la raiz del proyecto
* Ejecutar el siguiente comando:

  .\compilar.bat

Este script:

* Limpia y recompila el proyecto
* Usa el modulo JavaFX y las librerias de lib/
* Ejecuta automaticamente el programa

Importante: No mover las carpetas bin, lib ni el archivo compilar.bat a otras ubicaciones sin actualizar las rutas internas del script.

## Observaciones

* El proyecto es totalmente portable. No requiere configuracion adicional si las rutas estan correctamente establecidas.
* En caso de errores de ejecucion, verificar que tu JAVA\_HOME este bien configurado y que tengas Java 17+ instalado.

UCSM - Escuela Profesional de Ingenieria de Sistemas
Junio 2025
