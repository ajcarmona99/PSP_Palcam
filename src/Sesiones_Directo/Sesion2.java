package Sesiones_Directo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sesion2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Estoy pasando esto como hijos
        Process[] hijos = new Process[2];

        System.out.println("PADRE PID = " + ProcessHandle.current().pid());

        for(int i = 0; i< hijos.length; i++){
            hijos[i] = prepararHijo("H" + (i+1)).inheritIO().start();
            System.out.println("CREADO H" + (i+1));
        }

        ejecutarHijo("H1");
    }
    // Define el proceso de cada proceso hijo
    static void ejecutarHijo(String etiqueta) throws InterruptedException{
        int[] resultados = new int[2];

        // Creo el hilo A y B pero con esto no lo inicializa
        Thread a = new Thread(() -> tarea(resultados, 0, 10, etiqueta), etiqueta + "-A");
        Thread b = new Thread(() -> tarea(resultados, 1, 20, etiqueta), etiqueta + "-B");

        // Inicio los hilos
        a.start();
        b.start();

        // El hilo principal espera que termine los dos hilos
        a.join();
        b.join();

        System.out.println(etiqueta + "PID = " + ProcessHandle.current().pid());
    }

    // Creo una tarea que es lo que va a hacer cada hilo
    static void tarea(int[] resultados, int posicion, int valor, String etiqueta){
        // Muestra la etiqueta y el nombre del hilo
        System.out.println(etiqueta + " hilo =" + Thread.currentThread().getName() + " PID=" + ProcessHandle.current().pid());
        try {
            Thread.sleep(10_000);

            // Modificar la posicion de resultados con el valor
            resultados[posicion] = valor;
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println(etiqueta + " interrumpido");
        }
    }

    // Metodo que prepara el proceso
    static ProcessBuilder prepararHijo(String... argumentos){
        // Construir el ejecutable
        String ejecutable = Path.of(
                System.getProperty("java.home"), "bin",
                System.getProperty("os.name").startsWith("Windows") ? "java.exe" : "java"
        ).toString();

        // Creo que una lista que me va aservir para definir el comando
        List<String> orden = new ArrayList<>();

        orden.add(ejecutable);
        orden.add("-cp");
        // Añado el classpath del padre
        orden.add(Sesion2.class.getName());
        orden.add("hijo");
        Collections.addAll(orden, argumentos);

        // Retorno el proceso
        return new ProcessBuilder(orden);
    }
}
