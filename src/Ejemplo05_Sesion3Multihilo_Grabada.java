// Contador seguro
public class Ejemplo05_Sesion3Multihilo_Grabada {
    private static final int REPETICIONES = 10_000;

    static class Contador{

        // No necesito bajo ningun concepto declarar una variable de tipo volatile
        // porque estos estan protegidos mediante synchronized
        private int valor = 0;

        // Un metodo con una instancia synchronized utiliza
        // como monitor el objeto sobre el que se ha creado la instancia(this).
        synchronized void incrementar(){

            // Ahora el incremento queda totalmente protedigo
            valor++;
        }

        // Utilizamos el mismo monitor para leer el dato
        int leer(){
            return valor;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Los dos hilos compaten el MISMO CONTADOR
        Contador contador = new Contador();

        Runnable tarea = () -> {
          for (int i = 0; i< REPETICIONES; i++){

              // Cada llamada hace que el synchronized funcione como monitor del contador
              contador.incrementar();
              // Al salir del método se libera el contador
          }
        };

        Thread primero = new Thread(tarea, "seguro-A");
        Thread segundo = new Thread(tarea, "seguro-B");

        // Arranco ambos hilos
        primero.start();
        segundo.start();

        primero.join();
        segundo.join();
        // Esperamos todos los incrementos
        System.out.println("Obtenido: " + contador.leer());
    }
}

// ¿volatile hace atomico un valor++? No
//¿join() evita actualizaciones perdidas? No, espera la terminacion
// ¿Porque usamos la barrera? Para controlar la intercalación de la demostracion
// ¿Que protege el synchronized? La ejecucion bajo el mismo monitor

// Siempre que tengais problemas a la hora de que varios hilos esten intentando acceder a una variable
// no solo para mostrarla si no también para modificarla, lo más aconsejable y facil de implementar
// es meter un metodo sincronizado para hacer que un unico hilo de los que estan intentando acceder
// pueda realizarlo.