import java.util.concurrent.ArrayBlockingQueue;

public class Ejemplo01_Sesion4Multihilo_Grabada {
    public static void main(String[] args) throws InterruptedException {
        // La cola admite como máximo dos elementos a la vez
        ArrayBlockingQueue<Integer> cola = new ArrayBlockingQueue<>(2); // Definimos que la capacidad en este caso es dos

        // offer intenta insertar sin esperar
        System.out.println("¿Entra 10?: " + cola.offer(10));
        System.out.println("¿Entra 20?: " + cola.offer(20));

        // Vamos con la cola esta llena, e intento introducir otro numero
        System.out.println("¿Entra 30?: " + cola.offer(30)); // Como la cola esta llena este numero no entra y devuelve false

        // poll extrae el primer elemento sin esperar
        // Sale el 10 porque fue el primero en entrar
        System.out.println("Sacamos: " + cola.poll());

        // Ahora si hay un hueco: el 30 si entra
        System.out.println("¿Entra 30 ahora?: " + cola.offer(30));

        // Los datos salen en orden de entrada
        System.out.println("Sacamos: " + cola.poll());
        System.out.println("Sacamos: " + cola.poll());

        // Si no queda ningun elemento, poll devuelve null
        System.out.println("Sacamos de la cola vacía: " + cola.poll());

        // Aqui hemos utilizado offer y poll porque solo hay un hilo que es el main. Quiero que el programa me informe en el momento
        // de si hay espacio o si hay datos.
        //
        // Para el siguiente ejemplo si tenemos dos hilos. Entonces nos va a interesar que el productor espero si no hay hueco
        // y el consumidor espere si no tiene nada que recoger.
    }
}
