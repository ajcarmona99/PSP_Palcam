// Ahora lo que tenemos son dos consumidores, pero sigue habiendo una sola cola.
// Cuando uno retira el numero 3, el otro ya no puede retirar el mismo elementos.

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// La cola protege sus propias operaciones. Como dos hilos pueden actualizar el mismo elementos, vamos a proteger el registrar
// con un synchronized.
public class Ejemplo03_Sesion4Multihilo_Grabada {
    private static final int NUM_CONSUMIDORES = 2;
    private static final int NUM_DATOS = 10;

    private static final int FIN = -1;

    static class Resumen{

        // Los dos consumidores que modifican estos camposç
        private int cantidad;
        private int suma;

        synchronized void registrar(int dato){
            // Los dos cambios quedan protegidos por el monitor
            cantidad++;
            suma += dato;
        }

        void mostrarTrasJoin(){
            // Main llama a este metodo despues de esperar a terminar los dos hilos consumidores.
            // En ese momento ya no hay escrituras concurrentes
            System.out.println("PROCESADOS=" + cantidad + " SUMA= " + suma);
        }
    }

    public static void main(String[] args) {
        // La cola admite dos elementos a la vez, aunque enviemos diez datos
        BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(2);

        // Los consumidores registran los datos en el mismo resumen
        Resumen resumen = new Resumen();

        Thread productor = new Thread(() -> {
            try {
                for (int numero = 1; numero <= NUM_DATOS; numero++){
                    cola.put(numero);
                }

                // Cada consumidor necesita su propia señal de salida
                for (int i=0; i< NUM_CONSUMIDORES; i++){
                    cola.put(FIN);
                }
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }, "Productor");

        Runnable consumir = () -> {
          try {
              while (true){
                  // Si la cola está vacía, take espera sin hacer un bucle de consulta
                  int dato = cola.take();

                  // El centinela es una orden, no lo procesamos como dato
                  if(dato == FIN){
                      System.out.println("Consumidor: fin recibido");
                      break;
                  }

                  // La cola protege take, el resumen protege registrar
                  resumen.registrar(dato);


                  System.out.println("Consumidor: proceso " + dato);
              }
          }catch (InterruptedException e){
              Thread.currentThread().interrupt();
          }
        };

        Thread[] consumidores = new Thread[NUM_CONSUMIDORES];

        // Creamos y arrancamos los consumidores
        for (int i=0; i < consumidores.length; i++){
            consumidores[i] = new Thread(consumir, "Consumidor-" + (i+1));
            consumidores[i].start();
        }

        productor.start();
    }

}
