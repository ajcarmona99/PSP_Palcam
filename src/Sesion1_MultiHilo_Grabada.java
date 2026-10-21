// Sumar un array utilizando dos hilos distintos
public class Sesion1_MultiHilo_Grabada {
    public static void main(String[] args) throws InterruptedException {

        // Inicializar el array que es el que va a atacar los dos hilos distintos
        int[] numeros = {4, 7, 2, 9, 5, 1, 8, 3};

        // El resultado que devuelve cada uno de los hilos
        int[] resultados = new int[2];

        // La longitud mitad del array de los numeros
        int mitad = numeros.length / 2;

        // Inicializo el hilo que va a estar sumando hasta la mitad del array
        Thread hilo1 = new Thread(() -> {
            // Inicializo una variable auxiliar que me va a representar la suma de este hilo
            int suma = 0;

            // Recorro desde la posicion 0 del array hasta la mitad del mismo
            for (int i=0; i<mitad; i++){
                // Sumo cada posicion a la variable auxiliar que tengo
                suma += numeros[i];
            }

            // A la variable que tengo inicializada para guardar el resultado le asigno mi variable auxiliar
            resultados[0] = suma;
        });

        // Inicializo el hilo que va a estar sumando desde la mitad del array hacia delante
        Thread hilo2 = new Thread(() -> {
           // Inicializo una variable auxiliar que va a representa la suma de este hilo
           int suma = 0;

           // Recorro todas las posiciones desde la mitad hacia delante
            for (int i= mitad; i< numeros.length; i++){
                // Sumo cada posicion a la variable auxiliar que tengo
                suma += numeros[i];
            }

            // A la variable que tengo inicializada le asigna el valor de mi variable suma
            resultados[1] = suma;
        });

        // Inicializo los dos hilos
        hilo1.start();
        hilo2.start();

        // Espero que estos dos hilos terminen
        hilo1.join();
        hilo2.join();

        // Si tomo el total
        int total = resultados[0] + resultados[1];

        System.out.println("Primera mitad: " + resultados[0]);
        System.out.println("Segunda mitad: " + resultados[1]);
        System.out.println("Total: " + total);
    }
}
