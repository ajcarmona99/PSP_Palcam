package Sesiones_Directo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Sesion1 {
    public static void main(String[] args) throws IOException {
        System.out.println("=============================================");
        System.out.println("JAVA PROCESS MANAGER");
        System.out.println("=============================================");

        // Para que el usuario introduzca el servidor al que se quiere comprobar la conexion
        Scanner scanner = new Scanner(System.in);
        String comando = scanner.nextLine();
        System.out.println("\nComando >" + comando);

        ejecutarProceso(comando);
    }

    public static void ejecutarProceso(String comando) throws IOException {
        boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
        ProcessBuilder builder;
        if (windows){
            builder = new ProcessBuilder("cmd", "/c", comando);
            System.out.println("Inciando el proceso");
            long inicio = System.currentTimeMillis();
            Process proceso = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));

            String linea;
            while ((linea = reader.readLine()) != null){
                System.out.println(linea);
            }
        }
    }
}
