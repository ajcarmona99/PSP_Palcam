import java.util.concurrent.ThreadLocalRandom;

public class Sesion1Multihilo_Directo {

    // El método principal que es el hilo principal
    public static void main(String[] args) {
        // Creamos tres hilos
        Thread coche1 = new Thread(new Coche(), "Coche-1");
        Thread coche2 = new Thread(new Coche(), "Coche-2");
        Thread coche3 = new Thread(new Coche(), "Coche-3");

        // Muestro por pantalla que ha comenzado la carrera
        System.out.println("¡Comienza la carrera!");

        // Inicializo la carrera ejecutando la tarea de cada uno de los coches
        coche1.start();
        coche2.start();
        coche3.start();

        try {
            // join() hace que el hilo principal espere a cada coche.
            // Los coches siguen ejecutandose mientras el hilo principal espera.
            coche1.join();
            coche2.join();
            coche3.join();

            // Este mensaje aparece posteriormente a que cada uno de los hilos finalice.
            System.out.println("Todos los coches han terminado la carrera");

        }catch (InterruptedException e){
            // Restauramos la señal de interrupcion del hilo principal
            Thread.currentThread().interrupt();
            System.out.println("Se ha interrumpido la espera del programa principal.");
        }
    }

    // Runnable define la tarea que ejecutara cada hilo
    static class Coche implements Runnable{
        // Con el método run hace que esta clase sea la definicón de un hilo
        @Override
        public void run(){
            // Obtener el nombre del hilo que ejecuta este coche
            String nombre = Thread.currentThread().getName();

            // Cada coche recorre los kilómetros del 1 al 10
            for (int kilometro = 1; kilometro <=10; kilometro++){
                System.out.println(nombre + " ha alcanzado el kilometro " + kilometro);

                // Vamos a generar con un objeto de java en particular un numero aleatorio entre el 100 y el 500
                // nextInt genera valores donde el limite superior es exclusivo
                int tiempoEspera = ThreadLocalRandom.current().nextInt(100, 501);

                try{
                    // Pausamos unicamente este coche
                    // Los demás hilos pueden continuar avanzando
                    Thread.sleep(tiempoEspera);
                }catch (InterruptedException e){
                    // Restauramos la señal y terminamos con este hilo
                    Thread.currentThread().interrupt();
                    System.out.println(nombre + " ha interrumpido su carrera");

                    // Con el return salimos de lo que es el método run
                    // El metodo run es lo que define que debe de hacer el hilo cuando mandamos su ejecución
                    return;
                }
            }
            // Muestre el coche que ha conseguido llegar a la meta
            System.out.println(nombre + " ha llegado a la meta.");
        }
    }
}
