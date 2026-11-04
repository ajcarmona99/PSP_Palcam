import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicReference;

// Forzar que los hilos primero lean para luego que ellos puedan escribir
public class Ejemplo04_Sesion4Multihilo_Grabada {
    // Empezamos con una sola ronda para entender el resultado
    private static final int REPETICIONES = 1000;

    static class Dato{
        // volatile no hace atómico el incremento
        volatile int valor = 0;
    }

    public static void main(String[] args) throws InterruptedException {
        // Dato compatido por los dos trabajadores
        Dato dato = new Dato();

        // La barrera necesita dos particiones
        CyclicBarrier barrera = new CyclicBarrier(2);

        // Guarden los error
        AtomicReference<Throwable> fallo = new AtomicReference<>();

        // Creamos la tarea de los hilos
        Runnable tarea = () -> {
          try {
              for (int i = 0; i < REPETICIONES; i++){
                  // Cada trabajador ontiene su propia copia local
                  int copia = dato.valor;

                  // Ninguno puede escribir hasta que ambos hayan leido
                  barrera.await();

                  //Ambos escriben el nuevo valor
                  dato.valor = copia + 1;

                  // Ninguno inicia la siguiente ronda hasta que no hayan escrito
                  barrera.await();
              }
          }catch (InterruptedException e){
              // Restauramos la marca de interrupcion
              Thread.currentThread().interrupt();

              // Registro el fallo solo si todavia no hay otro
              fallo.compareAndSet(null, e);
          }catch (BrokenBarrierException e){
              // La barrea ya no se puede completar en esta fase
              fallo.compareAndSet(null, e);
          }
        };

        // Creamos los dos hilos diferentes
        Thread primero = new Thread(tarea, "carrera-A");
        Thread segundo = new Thread(tarea, "carrera-B");

        primero.start();
        segundo.start();

        try {
            primero.join();
            segundo.join();
        }finally {
            // Solicitar la interrupcion de los hilos
            primero.interrupt();
            segundo.interrupt();
        }

        // No presentamos valida si hay algun fallo
        if (fallo.get() != null){
            throw new IllegalStateException(
                    "Fallo de la demostracion: ",
                    fallo.get()
            );
        }

        System.out.println(dato.valor);
    }
}
