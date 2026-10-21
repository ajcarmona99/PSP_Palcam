import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class Sesion3_Directo_Padre {
    public static void main(String[] args) throws Exception{
        // Crear una carpeta para guardar los errores
        Files.createDirectories(Path.of("errores"));

        // Modos de los tres hijos
        String[] modos = {"ok", "lento", "error"};

        // Lanzo los tres procesos
        for (int i=0;i<3;i++){
            int numero = i + 1;
            String modo = modos[i];

            // Creo el proceso hijo
            Process p = new ProcessBuilder("java", "-cp", "src", "Sesion3_Directo_Hijo", modo).start();

            // Envio la entrada al hijo
            try(PrintWriter pw = new PrintWriter(p.getOutputStream())) {
                pw.println((i) + " " + (i+1));
            }

            // Esperamos como máximo 3 segundos
            boolean terminado = p.waitFor(3, TimeUnit.SECONDS);

            String resultado;
            // Ahora saco el resultado
            if(!terminado){
                // Si tarda demasiado, lo detengo
                p.destroyForcibly();
                p.waitFor();
                resultado = "Tiempo agotado";
            } else if (p.exitValue()!=0) {
                // Si terminado con error
                resultado = new String(p.getErrorStream().readAllBytes());
            }else {
                resultado = new String(p.getInputStream().readAllBytes()).trim();

                System.out.println("Hijo " + numero + "resultado: " + resultado);
            }

            // Guardamos el fallo en un archivo
            Files.writeString(Path.of("errores/hijo" + numero + ".txt"), resultado);
        }
    }
}
