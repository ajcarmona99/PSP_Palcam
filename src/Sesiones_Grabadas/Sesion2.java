package Sesiones_Grabadas;

import java.util.ArrayList;
import java.util.Collections;

public class Sesion2 {
    public static void main(String[] args) throws Exception{
        System.out.println("PADRE PID=" + ProcessHandle.current().pid());
        //inheritIO permite observar la salida del hijo directamente en mi consola
        Process hijo = prepararHijo("Taller").inheritIO().start();

        System.out.println("HIJO CREADO: " + hijo.pid());
        System.out.println("Vivo al consultar= " + hijo.isAlive());
        // Con esto espera el hilo main del padre
        int codigo = hijo.waitFor();
        System.out.println("HIJO TERMINADO codigo = " + codigo);
        System.out.println("Vivo despues");
    }

    static void ejecutarHijo(String etiqueta) throws InterruptedException{
        System.out.println(etiqueta + " PID=" + ProcessHandle.current().pid());
        // Los dos hilos comparten este array que se ejecutando al mismo tiempo
        int[] resultados = new int[2];
        Thread a = new Thread(() -> tarea(resultados,0, 10), "A");
        Thread b = new Thread(() -> tarea(resultados, 1, 20), "B");

        //Inicializo los dos hilos
        a.start();
        b.start();

        //join
        a.join(); //Espera que termine el hilo A
        b.join(); //Espera que termine el hilo B
        System.out.println("A despues de join= " + a.getState());
        System.out.println("Suma compartida= "+(resultados[0] + resultados[1]));
     }
    static ProcessBuilder prepararHijo(String... argumentos){
        // Estoy creando la ejecucion de java -cp . Sesiones_Grabadas/Sesion2
        // Sesion2.java
        String ejecutable = java.nio.file.Path.of(
                System.getProperty("java.home"), "bin",
                System.getProperty("os.name").startsWith("Windows") ? "java.exe" : "java"
        ).toString();

        java.util.List<String> orden = new ArrayList<>();
        orden.add(ejecutable);
        orden.add("-cp");
        orden.add(System.getProperty("java.class.path"));
        orden.add(Sesion2.class.getName());
        orden.add("hijo");
        Collections.addAll(orden, argumentos);

        return new ProcessBuilder(orden);
    }

    // La tarea que quiero que haga cuando se ejecuta un proceso hijo dentro del padre
    static void tarea(int[] resultados, int posicion, int valor){
        System.out.println("INICIO hilo=" + Thread.currentThread().getName() + " PID= "
        + ProcessHandle.current().pid());
        try {
            // Ventana observacion: espera artificial, no trabajo de la CPU
            Thread.sleep(10_000);
            resultados[posicion] = valor;
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println("Tarea interumpida");
        }
    }
}
