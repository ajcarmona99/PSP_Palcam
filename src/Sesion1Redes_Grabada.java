import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

// Quiero escribir cliente "Hola Ana MAaria" y que el saludo salga de la terminal. Si escribo solo cliente debe seguir usando HOLA Ana.
public class Sesion1Redes_Grabada {
    // Dos constantes que van ser quien figure como conexion de nuestro servidor
    static final String HOST = "127.0.0.1"; // Este host es el que representa a mi maquina local
    static final int PUERTO = 5001; // Este puerto es por el que entra la información a mi servidor

    // Primero vamos a ver que el cliente llega al servidor.
    // El servidor abro un puerto y espero.
    // El cliente llama a ese puerto.
    static void cliente(String mensaje) throws IOException{
        // Mediante el socket nos conectamos al servidor que se ha creado
        try(Socket conexion = new Socket()) {
            conexion.connect(new InetSocketAddress(HOST, PUERTO), 3000);

            // Todo el enviado de información de un lado a otro debe de ser a partir de conexion (se ha preparado el medio por el que se envía información al servidor)
            BufferedWriter salida = new BufferedWriter(new OutputStreamWriter(conexion.getOutputStream(), StandardCharsets.UTF_8));

            // Ahora le envio de parte del cliente todo lo que quiero al servidor
            salida.write(mensaje);
            salida.write("\n");
            // Para enviar toda la información y asegurarnos que no hay nada en el flujo que se quede atras
            salida.flush();

            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8));

            // Cogemos la respuesta que le ha enviado el cliente al servidor
            String respuesta = entrada.readLine();
            System.out.println("RESPUESTA=" + respuesta);
        }
    }

    static void servidor() throws IOException {
        // ServerSocket me permite crear un servidor que se comunica mediante un HOST y un puerto
        try(ServerSocket escucha = new ServerSocket()) {
            // Inicializo aqui cual es mi puerto y cual es mi host
            escucha.bind(new InetSocketAddress(HOST, PUERTO));
            // Si intenta inicializar y en 60 segundo no ha inicializado que de problemas de tiempo
            escucha.setSoTimeout(60_000);
            System.out.println("ESCUCHANDO");

            // Si el servidor concibe que esta teniendo algun tipo de conexion me lo notifica
            try(Socket conexion = escucha.accept()) {
                System.out.println("CONECTADO: " + conexion.getRemoteSocketAddress());

                // Vamos a incorporar la peticion mandanda al servidor mediante un lector
                BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8));

                // Tomamos lo que se ha enviado mediante un string
                String peticion = entrada.readLine();

                if(peticion == null){
                    // Se cierra el servidor si ningun tipo de información llegada
                    System.out.println("El cliente cerro sin enviar ninguna peticion");
                    return;
                }

                String respuesta = "ERROR FORMATO";

                // La comprobacion para enviar al cliente una respuesta por parte del servidor
                if (peticion.startsWith("HOLA") && peticion.length() > 5){
                    respuesta = "OK " + peticion.substring(5);
                }

                BufferedWriter salida = new BufferedWriter(new OutputStreamWriter(conexion.getOutputStream(), StandardCharsets.UTF_8));

                salida.write(respuesta);
                salida.write("\n");
                salida.flush();
            }
        }
    }

    public static void main(String[] args) throws IOException{
        // A partir de los argumentos vamos a decirle a partir de la terminal si es un servidor o si es un cliente.
        if (args.length == 0){
            System.out.println("Usa servidor o cliente.");
            return;
        }
        if (args[0].equals("cliente") && args.length < 2){
            cliente("HOLA Ana");
            return;
        }

        if (args[0].equals("servidor")){
            servidor();
        }else if (args[0].equals("cliente")){
            cliente(args[1]);
        }else {
            System.out.println("Papel desconocido");
        }
    }
}

// El cliente usa getOutPutStream() porque está escribiendo hacia la red.
// El sevidor usa getInputStream() porque desde su punto de vista, el dato entra. En los dos lados se elige el UFT-8 para interpretar igual el texto.
// \n marca donde acaba nuesto mensaje. flush() vacia el bufer del escribtor.
// El protocolo TCP lleva un flujo de bytes y que delimitar una linea es una decisión de nuestra aplicación.
// El error marcado lo ha escrito nuestro servidor. Es distinto de no poder conectar: normalmente suele haber conexión, lectura y respuesta.