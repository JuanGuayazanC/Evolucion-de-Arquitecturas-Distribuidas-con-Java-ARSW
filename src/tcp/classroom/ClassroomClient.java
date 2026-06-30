package tcp.classroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Ejercicio 1 - Parte I: Sockets TCP.
 *
 * <p>Cliente TCP que envía comandos de gestión de salones a {@link ClassroomServer}.
 * El usuario escribe el comando y el código del salón en una sola línea
 * con el formato {@code COMANDO,CODIGO} y el servidor responde con el resultado.
 */
public class ClassroomClient {

    /**
     * Solicita al usuario un comando y código de salón, lo envía al servidor
     * y muestra la respuesta.
     *
     * @param args argumentos de línea de comandos (no se usan)
     * @throws Exception si la conexión al servidor falla
     */
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la operacion y codigo (ej: CONSULTAR_SALON,E303): ");
        String request = scanner.nextLine();


        Socket socket = new Socket("127.0.0.1", 35001);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println(request);
        String response = in.readLine();
        System.out.println("Respuesta del servidor: " + response);

        in.close();
        out.close();
        socket.close();
    }
}
