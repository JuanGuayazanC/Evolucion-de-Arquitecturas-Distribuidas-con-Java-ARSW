package tcp.classroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP server that manages classroom reservations on port 35001.
 *
 * <p>Supported commands:
 * <ul>
 *   <li>{@code CONSULTAR_SALON,E303} — returns SALON_DISPONIBLE or SALON_RESERVADO</li>
 *   <li>{@code RESERVAR_SALON,E303}  — returns RESERVA_EXITOSA or ERROR_*</li>
 *   <li>{@code LIBERAR_SALON,E303}   — returns LIBERACION_EXITOSA or ERROR_*</li>
 * </ul>
 */
public class ClassroomServer {

    /**
     * Starts the server and listens indefinitely for client connections.
     *
     * @param args command-line arguments (unused)
     * @throws Exception if the server socket cannot be created
     */
    public static void main(String[] args) throws Exception {
        ClassroomRepository repository = new ClassroomRepository();
        // TODO: crear ServerSocket en puerto 35001
        System.out.println("ClassroomServer TCP escuchando en puerto 35001...");

        while (true) {
            // TODO: aceptar conexión, leer request, procesar, responder, cerrar
        }
    }

    /**
     * Processes a classroom command and returns the appropriate response.
     *
     * @param request    raw command string from the client
     * @param repository classroom repository
     * @return response string to send back
     */
    private static String processRequest(String request, ClassroomRepository repository) {
        if (request == null || request.isEmpty()) {
            return "ERROR_OPERACION_INVALIDA";
        }

        // TODO: separar el comando del código del salón (split por ",")
        // TODO: según el comando, llamar al método correspondiente del repositorio
        // Comandos: CONSULTAR_SALON, RESERVAR_SALON, LIBERAR_SALON
        // Respuestas posibles:
        //   SALON_DISPONIBLE, SALON_RESERVADO,
        //   RESERVA_EXITOSA, LIBERACION_EXITOSA,
        //   ERROR_SALON_NO_EXISTE, ERROR_OPERACION_INVALIDA

        return "ERROR_OPERACION_INVALIDA";
    }
}
