import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Sesion4_Multihilo_Directo {

    private static final int NUM_TRABAJADORES = 2;
    private static final int CAPACIDAD = 2;

    // Cada pedido contiene su identificador y su cantidad de unidades
    record Pedido(int id, int unidades){}

    // El identificador 0 no pertenece a ningun pedido real e indica el final
    private static final Pedido FIN = new Pedido(0,0);

    // Esta clase define el resumen de todos los pedidos
    static class Resumen {

        private int pedidosPreparados;
        private int unidadesTotales;

        synchronized void registrar(Pedido pedido){
            // Los dos trabajadores pueden llegar aquí al mismo tiempo
            // Lo que hacemos con el synchronized es que permiten que actualicen el resumen de uno en uno
            pedidosPreparados++;
            unidadesTotales = unidadesTotales + pedido.unidades();
        }

        void mostrarTrasJoin(){
            // Solo se llama cuando hayan terminando ambos trabajadores
            System.out.println("Pedidos preparados: " + pedidosPreparados);
            System.out.println("Unidades totales: " + unidadesTotales);
        }
    }

    public static void main(String[] args) throws InterruptedException{
        // Defino la cola que tiene una capacidad de dos
        BlockingQueue<Pedido> cola = new ArrayBlockingQueue<>(CAPACIDAD);

        // Se crea una instancia del resumen de todos los pedidos
        // que lo representa la clase que hemos definido anteriormente
        Resumen resumen = new Resumen();

        // Defino un total de 6 pedidos en un array
        Pedido[] pedidos = {
                new Pedido(1,2),
                new Pedido(2,4),
                new Pedido(3, 1),
                new Pedido(4, 5),
                new Pedido(5, 3),
                new Pedido(6, 2)
        };

        Thread productor = new Thread(() -> {
           try {
               // Introducimos todos los pedidos reales
               for (Pedido pedido : pedidos){
                   cola.put(pedido);
               }

               // Introducimos despues una señal que indique que no haya pedidos
               // que es el pedido que hemos definido como 0
               for (int i = 0 ; i < NUM_TRABAJADORES; i++){
                   cola.put(FIN);
               }
           }catch (InterruptedException e){
               Thread.currentThread().interrupt();
           }
        }, "Recepcion");

        Runnable prepararPedidos = () -> {
          try {
              while (true){
                  // Esperamos si hay todavia pedidos disponibles
                  Pedido pedido = cola.take();

                  // Comprobamos el indentificador para ver si hemos llegado al final
                  if (pedido.id() == FIN.id()){
                      return;
                  }

                  resumen.registrar(pedido);
              }
          }catch (InterruptedException e){
              Thread.currentThread().interrupt();
          }
        };

        Thread[] trabajadores = new Thread[NUM_TRABAJADORES];

        for (int i = 0; i< trabajadores.length; i++){
            trabajadores[i] = new Thread(prepararPedidos, "Almacen-" + (i+1));
            trabajadores[i].start();
        }

        productor.start();

        try {
            // Espero el envio de todos los pedidos
            productor.join();

            for (Thread trabajador: trabajadores){
                trabajador.join();
            }
        }finally {
            productor.interrupt();
            for (Thread trabajador : trabajadores){
                trabajador.interrupt();
            }
        }

        resumen.mostrarTrasJoin();
    }
}
