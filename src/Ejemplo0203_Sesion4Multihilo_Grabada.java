// Contador compartido inseguro (ejemplo 2) que esto hace que se pierda informacion de nuestro contador
// al intentar acceder a una misma variable al mismo tiempo
// Ejemplo 3. Simplemente vamos a añadir contador o este caso, valor de tipo volatile
// Con volatile no solucionamos el problema, aporta lo que son garantías de visibilidad,
// pero no protege una operación compuesta como un incremento en dicha variable.
public class Ejemplo0203_Sesion4Multihilo_Grabada {
    // Numero de incrementos realizados por cada hilo
    private static final int REPETICIONES = 100_000;

    static class Contador{
        // Los dos hilos que voy a crear van a tener esta variable compartida
        private volatile int valor = 0;

        void incrementar(){
            // Voy a acceder a increnentarla sin ningun tipo de sincronizacion
            valor++;
        }

        int leer(){
            // Consultamos el resultado posteriormente a que los hilos hayan finalizado
            return valor;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Creamos UN UNICO contador para compartirlo
        Contador contador = new Contador();

        // Los dos hilos ejecutan la misma tarea
        Runnable tarea = () -> {
          for (int i = 0; i < REPETICIONES; i++){
              contador.incrementar();
          }
        };

        // Creamos los dos hilos diferentes
        Thread primero = new Thread(tarea, "contador-A");
        Thread segundo = new Thread(tarea, "contador-B");

        // Arrancamos los dos
        primero.start();
        segundo.start();

        // Esperamos a que termine los dos
        primero.join();
        segundo.join();

        System.out.println("Valor obtenido: " + contador.leer());
    }
}
