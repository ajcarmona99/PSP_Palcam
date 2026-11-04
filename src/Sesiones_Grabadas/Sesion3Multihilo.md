# Contenidos

1) Descomponer un incremento.
2) Dos hilos y un contador inseguro
3) Porque volatile no basta
4) Forzar la actualizacion perdida con CyclicBarrier
5) Corregirla con synchronized
6) Recapitulacion y ejercicio para casa


# ¿Volatile que soluciona?

Publicar cambio de manera sencilla -> Sí porque da seguridad de visibilidad.


Se puede incrementar un contador compartido -Z No

# Ejercicio para casa

***Contador de entradas de un museo***

Un museo tiene cuatro tornos de entrada.

Cada torno se representa mediante un hilo y registra un total de 5000 entradas.
Todos los tornos deben actualizar un unico contador compartido.