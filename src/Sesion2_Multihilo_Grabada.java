import java.util.concurrent.CountDownLatch;

public class Sesion2_Multihilo_Grabada {
    public static void main(String[] args) throws InterruptedException {
        ejemplo3();
    }

    // Clase que se llame Contador
    static class Contador{
        // Ese objeto que va a actuar como monitor
        private Object monitor = new Object();

        // Esta variable esta compartida entre los hilos
        private int valor = 0;

        void incrementar(){
            // Solo un hilo va a poder entrar al mismo tiempo en los bloques protegidos por el monitor
            synchronized (monitor){
                valor++;
            }
        }

        int consultar(){
            // Tambien protegemos la lectura con el mismo monitor
            synchronized (monitor){
                return valor;
            }

        }

    }

    // Ejemplo1: Protegemos la variable mediante un monitor
    public static void ejemplo1() throws InterruptedException {
        // Los dos hilos van a compartir este contador
        Contador contador = new Contador();

        // Cada hilo incrementar el contador 10000 veces
        Runnable tarea = () -> {
          for (int i=0;i<10_000;i++){
              contador.incrementar();
          }
        };

        // Creamo los hilos
        Thread primero = new Thread(tarea, "primero");
        Thread segundo = new Thread(tarea, "segundo");

        // Arrancamos ambos
        primero.start();
        segundo.start();

        // Esperamos a que termine
        primero.join();
        segundo.join();

        System.out.println("Resultado: " + contador.consultar());
    }

    // El monitor lo que hace es que los dos hilos realicen simultáneamente esa modificación sobre el mismo contador.
    // Además, sincronizar los accesos a la variable con el mismo monitor permite que los hilos observen los cambios correctamente.

    // En este caso introducimos, una condicion compartida (booleano)
    static class Puerta{

        // Monitor que protege la condicion
        private final Object monitor = new Object();

        // Todas las lecturas y escrituras se hacen bajo el monitor
        private boolean abierta = false;

        void esperarApertura() throws InterruptedException {

            // Adquiero el monitor
            synchronized (monitor){
                // Mientras la condcion no permita continuar, debe de esperar
                while (!abierta){
                    System.out.println("HILO: La puerta está cerrada");

                    // Esperamos y liberamos el monitor
                    // Cuando el wait termine normalmente, recuperamos lo que es el monitor
                    monitor.wait();
                }

                // Solo llegamos aqui si la puerta pues se encuentra abierta
                System.out.println("HILO: La puerta está abierta, continua");
            }
        }

        void abrir(){
            // Adquiero de nuevo el monitor
            synchronized (monitor){

                // Primero modifico la condicion
                abierta = true;

                // Despues aviso a los demás que hilos que estan en espera que pueden continuar
                monitor.notifyAll();
                // El monitor se libera al salir del bloque
            }
        }
    }

    public static void ejemplo2() throws InterruptedException{
        // Ejemplo 2. Esperar una señal con wait() y notifyAll().
        Puerta puerta = new Puerta();

        // Crear un hilo que espera la apertura
        Thread trabajador = new Thread(() -> {
           try {
               puerta.esperarApertura();
               System.out.println("TRABAJADOR: realizo mi tarea.");
           }catch (InterruptedException e){
               // Interrumpo el hilo
               Thread.currentThread().interrupt();
               System.out.println("TRABAJADOR: Espera interrumpida");
           }
        });

        // Arranco el trabajador
        trabajador.start();

        try {
            // Esta pausa sirve para esperar visualmente las fases de la puerta
            Thread.sleep(1000);

            System.out.println("Main: abro la puerta");

            // Modificando la condicion y avisa
            puerta.abrir();
        }finally {
            // Si main fue interrumpido antes de abrir
            if(Thread.currentThread().isInterrupted()){
                trabajador.interrupt();
            }

            // El trabajador pueda terminar
            trabajador.join();
        }
    }

    static class Trabajador implements Runnable{
        // Monitor utilizado para proteger la condición y para realizar un wait/notifyAll
        private final Object monitor = new Object();

        // El trabajado empieza pausado.

        // Todas las lecturas y escrituras de esta variable se realizan pues dentro del synchronized
        private boolean pausado = true;

        // Señal de una unica utilizacion
        // Indica que el trabajador llego a su primera espera
        private final CountDownLatch puerta = new CountDownLatch(1);

        void esperarPrimeraPausa() throws InterruptedException {
            // Main espera hasta que el trabajador reduzca el contador a 0
            puerta.await();
        }

        void reanudar(){

            // Adquiero la variable que es el monitor que protege el pausado
            synchronized (monitor){

                // La condicion permite continuar
                pausado = false;

                // Avisamos a quien esperan este monitor
                monitor.notify();
            }
        }

        void pausar(){

            // Adquiero la variable monitor
            synchronized (monitor){
                pausado = true;
            }
        }

        @Override
        public void run(){

            // Variable local: solo la utiliza este hilo
            int paso = 0;

            try {

                //Continuamos mientras no este interrumpido
                while (!Thread.currentThread().isInterrupted()){

                    // Adquiero el monitor
                    synchronized (monitor){

                        // Usamos while para comprobar la condicion despues de despertar
                        while (pausado){

                            // Señalo la primera llegada
                            puerta.countDown();

                            // Liberamos el monitor
                            monitor.wait();
                        }
                    }

                    paso++;

                    System.out.println("Paso " + paso + " de " + Thread.currentThread().getName());

                    // Pausar para una mejor ejecucion
                    Thread.sleep(200);
                }
            }catch (InterruptedException e){
                // Realizo la interrupcion del hilo
                Thread.currentThread().interrupt();
                System.out.println("TRABAJADO: interrupcion atendida");
            }finally {
                System.out.println("TRABAJADOR finalizado");

            }
        }
    }

    public static void ejemplo3() throws InterruptedException{
        // Ejemplo 3. Pausar, reanudar y cancelar un trabajador.

        // Creo el hilo que ejecuta la clase anterior
        Trabajador tarea = new Trabajador();
        Thread hilo = new Thread(tarea, "Controlado");

        // Puedes decir que tiene una prioridad normal de ejecucion
        hilo.setPriority(Thread.NORM_PRIORITY);

        System.out.println("Antes: " + hilo.getState());

        // Arranco el trabajador
        hilo.start();

        try {

            // Esperamos a que llegue a la primera zona de pausa
            tarea.esperarPrimeraPausa();

            System.out.println("Observacion final: " + hilo.getState());

            System.out.println("MAIN: reanudar");

            tarea.reanudar();

            Thread.sleep(1000);

            System.out.println("MAIN: solicitar pausa");

            tarea.pausar();

            System.out.println("PAUSA SOLICITADA");

            Thread.sleep(700);

            System.out.println("Observacion en pausa: " + hilo.getState());

            System.out.println("MAIN: reanudar");

            tarea.reanudar();

            Thread.sleep(600);
        }finally {
            System.out.println("MAIN: solicita informacion");
            hilo.interrupt();
            hilo.join();

            System.out.println("Despues: " + hilo.getState());
        }
    }
}
