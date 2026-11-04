import java.util.Scanner;

public class Sesion3Mutihilo_Directo {
    private static class Trabajador extends Thread{

        // Monitor privado que protege tanto el pausado como el progreso
        private final Object monitor = new Object();

        // Estas variables se consultan y modifican dentro de ese monitor
        private boolean pausado = true;
        private int progreso = 0;

        public Trabajador(){
            super("Trabajador-descargar");
        }

        public void reanudar(){
            synchronized (monitor){
                pausado = false;

                // Despierta a los hilos que esperan a este monitor
                monitor.notifyAll();
            }
        }

        public void pausar(){
            synchronized (monitor){
                pausado = true;
            }
        }

        public int consultarProgreso(){
            synchronized (monitor){
                return progreso;
            }
        }

        @Override // Sobreescribo el metodo run que tienen los hilos
        public void run(){
            System.out.println("Descarga iniciada en pausa: 0%");

            try {

                while (true){
                    synchronized (monitor){
                        if (progreso >= 100){
                            return;
                        }

                        // Usamos un while porque puede haber despertares sin que yo haya solicitado una reanudacion
                        while (pausado){
                            monitor.wait();
                        }
                    }

                    // El incremento empieza aqui
                    Thread.sleep(300);

                    synchronized (monitor){
                        // Detecta si hay alguna interrupcion recibida
                        if (Thread.currentThread().isInterrupted()){
                            throw new InterruptedException();
                        }

                        progreso += 10;
                        System.out.println("Progreso: " + progreso + "%");

                        if (progreso == 100){
                            System.out.println("Descarga completada.");
                            return;
                        }
                    }
                }
            }catch (InterruptedException e){
                // Tanto la funcion wait como la sleep pueden ser interrumpidos.
                // Restauramos la señal antes de que termine el trabajador
                Thread.currentThread().interrupt();
                System.out.println("Descarga cancelada.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Trabajador trabajador = new Trabajador();
        trabajador.start();

        try(Scanner teclado = new Scanner(System.in)) {
            boolean salir = false;
            while (!salir && trabajador.isAlive()){
                System.out.print("Selecciona una opcion");
                if (!teclado.hasNextLine()){
                    break;
                }

                String opcion = teclado.next().trim();

                switch (opcion){
                    case "1":
                        trabajador.reanudar();
                        System.out.println("Reanudacion solicitada");
                        break;

                    case "2":
                        trabajador.pausar();
                        System.out.println("Pausa solicitada");
                        break;

                    case "3":
                        System.out.println(
                                "Progreso actual: " + trabajador.consultarProgreso() + " %"
                        );
                        break;

                    case "4":
                        trabajador.interrupt();
                        salir = true;
                        break;

                    default:
                        System.out.println("Opcion no valida.");
                }
            }
        }finally {
            if(trabajador.isAlive()){
                trabajador.interrupt();
            }

            trabajador.join();
        }

        System.out.println("Programa finalizado.");
    }
}
