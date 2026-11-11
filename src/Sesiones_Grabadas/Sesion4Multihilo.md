# Contenidos

1) Ejemplo 1: una cola de dos plazas, sin hilos.
2) Ejemplo 2: un productor, un consumidor y un mensaje de fin
3) Dos consumidores y un resumen compartido.

Productor - pone datos -> [ cola de dos plazas] - saca datos -> Consumidor

* Productor: Crea los datos e introduce la cola.
* Consumidor: Extrae un dato y lo proceso. Al extraerlom ese dato ya no está disponible
para otro consumidor.
* Capacidad: es el número máximo de elementos que pueden estar en la cola a la vez.
Una cola de dos plazas puede transportar diez datos a los largo de la ejecución.

# ¿ Como creamos una cola en Java?

ArrayBlockingQueue(numero de hilos que pueden entrar en esta cola o si lo entendeis
mejor el numero de hilos que pueden compartir un mismo recurso)

* offer(dato) -> Si la cola está llena, devuelve un FALSE.
* poll() -> Si la cola esta vacía, devuelve null.
* put(dato) -> Si la cola está llena, espera a que haya espacio.
* take() -> Si la cola está vacía, espera a que haya un dato.

# Ejercicio para casa

Un productor introduce seis pedidos en una cola de dos.

Dos trabajadores que rertiran los pedidos y preparan sus unidas,

Al final, el programa muestra cuantos pedidos se han preparado y cuantas unidades suman entre todos.