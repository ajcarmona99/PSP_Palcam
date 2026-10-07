package Sesiones_Grabadas;

public class Sesion2 {
    public static void main(String[] args) throws Exception{
        System.out.println("Programa iniciado.");
        trabajar(args[0], Integer.parseInt(args[1]));
        System.out.println("Mi PID es :" + ProcessHandle.current().pid());
    }

    public static void trabajar(String nombre, int pasos){
        System.out.println(nombre + "comienza. PID:" + ProcessHandle.current().pid());

        for (int paso = 1; paso <=pasos;paso++){
            System.out.println(nombre + " realiza el paso " + paso);
        }
    }
}
