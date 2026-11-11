public class Ejemplo01_Sesion3Multihilo_Grabada {
    public static void main(String[] args) {
        // Valor inicial del contador
        int valor = 0;

        // Un incremento puede entender mediante estos pasos
        //1. Leer el valor actual
        int copia = valor;

        //2. Calculo el nuevo valor
        int nuevoValor = copia + 1;

        //3. Escribimos el resultado
        valor = nuevoValor;

        // hEMOS realizado un incremento
        System.out.println("Valor final: " + valor);
    }
}

// Con un unico hilo como es el main no hay conflicto. El problema aparece si otro hilo lee o modifica el contador.
