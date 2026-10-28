# Contenido

* Trabajar el problema de la concurrencia con el método synchronized.
* Esperar para atacar a una variable con el método que ya conocer que el wait.
* Despertar un hilo a partir de notifyAll().
* Pasar y reanudar un hilo.
* Vamos a terminar un hilo mediante interrupción.

# Ejemplos

1) Ejemplo 1. Proteger una variable mediante el synchronized.
2) Ejemplo 2. Esperar una señal con wait() y notifyAll().
3) Ejemplo 3. Pausar, reanudar y cancelar un trabajador.

# Resumen

* sleep() -> Duerme el hilo actual y no libera monitores.
* wait() -> Espera liberando el monitor sobre el que se invoca.
* notifyAll() -> Avisa a los hilos que esperan ese monitor
* interrupt() -> Solicita una interrupción
* join() -> Espera a que termine un hilo

# Ejercicio para casa

El programa debe de simular una descarga que avanza desde el 0% al 100% incrementando de 10 en 10.

Tiene un hilo que debe de:

* Comenzar pausado.
* Esperar utilizando wait().
* Avanzar un 10% en cada paso.
* Dormir 300 milisegundos antes de completar cada incremento.
* Permitir pausas y reanudaciones.
* Terminar al llegar al 100% o al recibir una interrupcion.

El hilo principal(main) mostrara este menú:

1) Reanudar.
2) Pausar.
3) Consultar el progreso.
4) Cancelar y salir.