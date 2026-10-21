import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sesion3 {

    static final int NUM_HIJOS = 2; // Se construyen dos procesos hijos
    static final  int TAM_50 = 50; // Cada hijo suma 50 numeros

    public static void main(String[] args) throws Exception{
        // Si el primer argumento que se lanza es hijo, este proceso proceso actua como hijo.prefix
        if (args.length > 0 && args[0].equals("hijo")){
            ejecutarHijo(args[1]);
        }

        // Sin argumentos usa "ok"
        String modo = args.length == 0 ? "ok" : args[0];

        // Crear un array que guarda la referencia a los hijos
        Process[] hijos = new Process[NUM_HIJOS];

        // Creo una carpeta temporal donde se guarda la salida hijo
        Path carpeta = java.nio.file.Files.createTempDirectory("psp-sesion3-");

        // Primero se lanzan todos los hijos y se le envian los rangos
        for (int i=0; i < hijos.length; i++){
            // El ultimo hijo usa el modo elegido, los demas van a usar ok
            String modoHijo = i == hijos.length - 1 ? modo : "ok";

            // Preparar el comando
            hijos[i] = prepararHijo(modoHijo).redirectError(ProcessBuilder.Redirect.INHERIT).redirectOutput(carpeta.resolve(i + ".txt"). toFile()).start();

            // Calcula el final, para i=0, es 50, para i= 1 es 100
            int desde = i * TAM_50 + 1;
            int hasta = (i+1) * TAM_50;
            System.err.println("INICIO TAREA " + i + " PID= " + hijos[i].pid() + " rango= " + desde + ".." + hasta);
        }
        }

    // Contiene el trabajo que debe realizar un proceso hijo
    static void ejecutarHijo(String modo) throws Exception{
        int desde; // Guardar el límite inferior del rango
        int hasta; // Guarda el limite superior

        // Intentar leer y validar la entrada que le pasa el padre
        try {
            // Crear un lector de texto
            var lector = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)); // Interpreta los bytes como UTF-8;
            // Lee una linea. Devuelve null si no encuentra una linea
            String linea = lector.readLine();

            //Eliminar los espacios y separar por uno cada espacio
            String[] partes = linea.trim().split("\\s+");
            // Debe de haber exactamente dos valores: inicio y final
            if (partes.length != 2){
                throw new IllegalArgumentException("Se experan dos limites");
            }

            desde = Integer.parseInt(partes[0]); //Convierte el primer texto a int
            hasta = Integer.parseInt(partes[1]); // Convierto el segundo texto
        }catch (IllegalArgumentException e){
            // Explica el error
            System.err.println("ENTRADA_INVALIDA: " + e.getMessage());
            System.exit(2); // Termina todo el proceso con codigo dos
            return;
        }

        // Si se pide lentitud
        if (modo.equals("lento")){
            Thread.sleep(10_000);
        }

        // Si pide simular un error
        if (modo.equals("error")){
            System.err.println("Fallo simulado");
            System.exit(3); // Termino con codigo de error 3
        }

        // Si se pidio una respuesta con formato incorrecto
        if (modo.equals("malformado")){
            System.out.println("no-numero"); // Devuelve texto en vez de un entero
            return;
        }

        long suma = 0; // Inicializar el acumulador de este hijo

        // Recorre todo los números del rango, incluido ambos límites
        // En cada iterracion añade n a la suma
        for (int n = desde; n<=hasta; n++){
            suma =+n;
        }

        // Envia resultado
        System.out.println(suma);
    }

    // Preparar otro proceso Java con la instalacion y el classpath del padre
    // String... permite recibir una cantidad variable de argumentos
    static ProcessBuilder prepararHijo(String... argumentos){
        //Construir la ruta al ejecutable de Java
        String ejecutable = Path.of(
                // Obtengo la carpeta de instalación de Java y añade el bin
                System.getProperty("java.home"), "bin",
                // En Windoes el ejecutable es "java.exe", en otros es "Java"
                System.getProperty("os.name").startsWith("Windows") ? "java.exe" : "java"
        ).toString();

        // Creo una lista vacía con las partes del comando que se van a ejecutar
        List<String> orden = new ArrayList<>();

        orden.add(ejecutable);
        orden.add("-cp"); // Indique el classpath
        // Añade el nombre de esta clase; sera la que ejecute el nuevo proceso
        orden.add(Sesion3.class.getName());

        // Primer argumento del nuevo proceso
        orden.add("hijo");

        // Alado los argumentos recibido
        Collections.addAll(orden, argumentos);

        // Devuelve el objeto que permita lanzar el comando
        return new ProcessBuilder(orden); // Con esto no iniciamos el proceso con esto hasta que no llamen al start esto no comienza
    }


}
