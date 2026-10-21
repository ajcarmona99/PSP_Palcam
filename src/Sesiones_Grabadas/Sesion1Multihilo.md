# Objetivo

Vamos a comenzar con la introducción de lo que es un hilo.

# Contenido

1) Que es un hilo y un primer ejemplo sobre ello.
2) Dos hilos trabajando al mismo tiempo.
3) Diferencia entre start() y run().
4) sleep() y join().
5) Hilos con tareas diferentes.

# Diferencia entre run y start

run
* Ejecuta un método normal.
* No crea un hilo adicional.
* El hilo actual es el que se lleva todo el trabajo (main)

start
* Inicia un hilo nuevo.
* Crea un hilo idependiente.
* Un nuevo que crea es el que se lleva todo el trabajo.

# Sleep y Join

1. sleep -> pausa temporalmente el hijo que ejecuta
2. join -> hace que el hilo que lo llama espera a que termine otro.

# join

Llamar al método join de un hilo lo que hace que el hilo principal espere a que ese hilo finalize.

# Ejercicio para casa

1. Crear tres hilos llamados Coche-1, Coche-2, Coche-3.
2. Cada coche va avanzar del kilometro 1 al 10 utilizando un for.
3. Cada vez que avance un kilometro, mostrara su nombre y el kilometro alcanzado.
4. Despues de cada kilometro, el hilo espera un tiempo aleatorio entre 100 y 500 milisegundos
utilizando el Thread.sleep().
5. Los tres coches comienza la carrera.
6. El programa principal debe de esperar a que terminen todos los coches.
7. Cuando los coches hayan llegado a meta, se debe de mostrar el mensaje Todos los coches han terminado la carrera.