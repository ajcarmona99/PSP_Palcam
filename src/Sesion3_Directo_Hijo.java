import java.util.Scanner;

public class Sesion3_Directo_Hijo {
    public static void main(String[] args) throws Exception {
        // Leer los datos enviados del padre
        Scanner sc = new Scanner(System.in);
        int inicio = sc.nextInt();
        int fin = sc.nextInt();

        // El modo llega como argumento
        String modo = args[0];

        //Simulamos un proceso lento
        if(modo.equals("lento")){
            Thread.sleep(5000); // Se duerme cinco segundos
        }

        // Simulamos un error
        if (modo.equals("error")){
            System.out.println("Error provocado");

            // Hacemos que la salida del proceso sea uno
            System.exit(1);
        }

        // Calculamos la suma del rango
        int suma = 0;
        for (int i= inicio; i<fin;i++){
            suma += i;
        }

        if (modo.equals("maformado")){
            System.out.println("Resultado incorrecto");
        }else {
            System.out.println(suma);
        }

        // Se cierra el scanner
        sc.close();
    }
}
