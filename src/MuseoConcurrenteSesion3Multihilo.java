public class MuseoConcurrenteSesion3Multihilo {

    // Numero de trabajadores que son el numero de torno
    private static final int TORNOS = 4;

    // Incrementos o las entradas de cada hilo
    private static final int ENTRADAS_POR_TORNO = 50_000;

    // Operaciones que deben ofrecer ambos contadores
    interface Contador{
        void incrementar();

        int leer();
    }

    static class ContadorInseguro implements Contador{
        // Variable compartida que es el numero de entradas
        private int valor = 0;

        @Override
        public void incrementar(){
            valor++;
        }

        @Override
        public int leer(){
            return valor;
        }
    }

    static class ContadorSeguro implements Contador{
        // Los accesos se protegen con el monitor de este objeto
        private int valor = 0;

        @Override
        public synchronized void incrementar(){
            valor++;
        }

        @Override
        public synchronized int leer(){
            return valor;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Contador contador;

        // Si no se indica un modo, utilizamos el seguro
        String modo = args.length == 0 ? "seguro" : args[0];

        if (modo.equals("inseguro")){
            contador = new ContadorInseguro();
        }else {
            contador = new ContadorSeguro();
        }

        // Creamos unos hilos que formen los 4
        Thread[] hilos = new Thread[TORNOS];

        // Creamos las tareas
        for (int i = 0; i< hilos.length; i++){

            hilos[i] = new Thread(() -> {
               for (int entrada = 0; entrada < ENTRADAS_POR_TORNO; entrada++){
                   contador.incrementar();
               }
            }, "Torno-" + (i+1) );
        }

        // Arrancamos los hilos
        for (Thread hilo:hilos){
            hilo.start();
        }

        // Esperamos los hilos
        for (Thread hilo:hilos){
            hilo.join();
        }

        // Numero total de incrementos solicitados
        int esperado = TORNOS * ENTRADAS_POR_TORNO;

        // Consultamos despues de terminar los trabajadores
        int obtenido = contador.leer();

        System.out.println("Esperado: " + esperado);
        System.out.println("Obtenido: " + obtenido);
    }
}
