# Tarea8: Ant pheromone trail simulation

## Autor: Arrieta Mancera Luis Sebastian 318174116

<img width="400px" src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExODR5cng5cmN0cGZseWpvcW05NDVrYWNnNmY2NjkxcjVwMjh1N2NrbiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/26vIeH7yWdqgEwTja/giphy.gif"/>

Este programa simula el rastro de feromonas de una colonia de hormigas, en donde los puntos **verdes** corresponden a las hormigas, el cuadrado **cafe** corresponde
al hormiguero, el cuadro **rojo** a la comida y los puntos **azules** corresponden al rastro de feromonas, estas desaparecen con el paso del tiempo.

## Ejecución

Para correr la práctica crea una carpeta `./classes`

```bash
mkdir ./classes
```

Ejecuta el siguiente comando para compilar todas las clases

```bash
javac -d ./classes -cp lib/core.jar:. hormigas/*.java
```

Ahora ejecutamos el programa

```bash
java -cp ./classes:lib/core.jar hormigas.Hormigas
```