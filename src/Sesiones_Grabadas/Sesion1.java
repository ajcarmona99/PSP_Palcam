package Sesiones_Grabadas;

import java.util.Scanner;

public class Sesion1 {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("PID: " + ProcessHandle.current().pid());
        System.out.println("El programa ha comenzado");

        teclado.nextLine();
    }
}
