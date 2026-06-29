package tcp.classroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP client that sends classroom management commands to ClassroomServer.
 */
public class ClassroomClient {

    /**
     * Prompts the user for a command and classroom code, sends the request, and prints the response.
     *
     * @param args command-line arguments (unused)
     * @throws Exception if the connection to the server fails
     */
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // TODO: pedir al usuario la operación (CONSULTAR, RESERVAR, LIBERAR)
        // TODO: pedir el código del salón (E301..E304)
        // TODO: armar el mensaje con formato "COMANDO,CODIGO"
        // TODO: conectar al servidor en 127.0.0.1:35001, enviar y mostrar respuesta
    }
}
