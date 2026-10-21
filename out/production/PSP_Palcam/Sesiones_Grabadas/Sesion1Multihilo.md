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