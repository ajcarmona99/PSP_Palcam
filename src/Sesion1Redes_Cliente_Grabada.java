import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Sesion1Redes_Cliente_Grabada {
    static final String HOST = "127.0.0.1"; // Este host es el que representa a mi maquina local
    static final int PUERTO = 5001; // Este puerto es por el que entra la información a mi servidor

    // El cliente llama a ese puerto.
    static void cliente(String mensaje) throws IOException {
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

    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("No podemos hacer la comprobacion");
            return;
        }
        cliente(args[1]);
    }
}
