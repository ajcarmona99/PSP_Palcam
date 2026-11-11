// El productor enviar los numeros del 1 al 5.
// Cuando termine, va a poner un -1 en la cola.
// Ese número está reservado: significa no quedan mas datos. Y a esto en programación se le denomina centinela.

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Que la cola quede vacia en su momento no demuestra el productor haya terminado.
// Puede estar esperando al sigueinte dato.
// Por eso el consumidor espera un take()
public class Ejemplo02_Sesion4Multihilo_Grabada {
    // Los datos válidos van del 1 al 5. El - 1 se reserva para el cierre.
    private static final int FIN = -1;

    public static void main(String[] args) throws InterruptedException {
        // Productor y consumidor van a compartir esta misma cola
        BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(2);

        // Creamos el hilo que va a ser el productor que va a introducir datos en mi cola
        Thread productor = new Thread(() -> {
           try {
               // Si la cola se llena, put espera a que quede un hueco
               for (int numero = 1; numero <= 5; numero++){
                   cola.put(numero);
               }

               // El centinela entra después de todos los datos
               cola.put(FIN);
           }catch (InterruptedException e){
               // Restauramos la marca y abandonamos la tarea
           }
        }, "Productor");

        Thread consumidor = new Thread(() -> {
           try {
               while (true){
                   // Si la cola está vacía, take espera sin hacer un bucle de consulta
                   int dato = cola.take();

                   // El centinela es una orden, no lo procesamos como dato
                   if(dato == FIN){
                       System.out.println("Consumidor: fin recibido");
                       break;
                   }

                   System.out.println("Consumidor: proceso " + dato);
               }
           }catch (InterruptedException e){
               // take puede tambien interrumpirse mientras espera
               Thread.currentThread().interrupt();
           }
        }, "Consumidor");

        // Arrancamos ambos antes de la terminacion de ninguno
        consumidor.start();
        productor.start();

        try{
            productor.join();
            consumidor.join();
        }finally {
            // Si el main deja de esperar por una interrupcion, pide tambien que los trabajadores salgan del put o take
            productor.interrupt();
            consumidor.interrupt();
        }

        System.out.println("Main: ambos hilos han terminado.");
    }
}

// put y take ya resuelven la espera por huecos y por datos.
// Gracias a estos metodos no tenemos que escribir nuestro propio wait y notifyAll para gestionar esta cola.
// Fijaros tambien que esperamos con un join depues de arrancar los datos.
//
// Su arrancamos al productor y espera a que termine antes de arrancar el consumir, el productor llenaria las dos plazas que tiene la cola y se quedaria
// esperando a que retirase un dato.