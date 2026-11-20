import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Sesion1Redes_Servidor_Grabada {

    static final String HOST = "127.0.0.1"; // Este host es el que representa a mi maquina local
    static final int PUERTO = 5001; // Este puerto es por el que entra la información a mi servidor
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

    public static void main(String[] args) throws IOException {
        servidor();
    }
}
