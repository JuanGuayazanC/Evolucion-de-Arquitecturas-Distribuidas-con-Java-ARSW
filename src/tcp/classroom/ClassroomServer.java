package tcp.classroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Ejercicio 1 - Parte I: Sockets TCP.
 *
 * <p>Servidor TCP que gestiona reservas de salones en el puerto 35001.
 * El protocolo es texto plano: el cliente envía {@code COMANDO,CODIGO} y el servidor
 * responde con una cadena que indica el resultado de la operación.
 *
 * <p>Comandos soportados:
 * <ul>
 *   <li>{@code CONSULTAR_SALON,E303} — retorna {@code SALON_DISPONIBLE} o {@code SALON_RESERVADO}</li>
 *   <li>{@code RESERVAR_SALON,E303}  — retorna {@code RESERVA_EXITOSA} o {@code ERROR_*}</li>
 *   <li>{@code LIBERAR_SALON,E303}   — retorna {@code LIBERACION_EXITOSA} o {@code ERROR_*}</li>
 * </ul>
 */
public class ClassroomServer {

    /**
     * Inicia el servidor y escucha indefinidamente conexiones de clientes.
     *
     * @param args argumentos de línea de comandos (no se usan)
     * @throws Exception si no se puede crear el ServerSocket
     */
    public static void main(String[] args) throws Exception {
        ClassroomRepository repository = new ClassroomRepository();
        ServerSocket serverSocket = new ServerSocket(35001);
        System.out.println("ClassroomServer TCP escuchando en puerto 35001...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = in.readLine();
            String response = processRequest(request, repository);
            out.println(response);

            in.close();
            out.close();
            clientSocket.close();
        }
    }

    /**
     * Procesa un comando de salón y retorna la respuesta apropiada.
     *
     * @param request    comando recibido del cliente con formato {@code COMANDO,CODIGO}
     * @param repository repositorio de salones
     * @return cadena de respuesta a enviar al cliente
     */
    private static String processRequest(String request, ClassroomRepository repository) {
        if (request == null || !request.contains(",")) {
            return "ERROR: formato invalido. Use MOVIE:id";
        }
        try {
            String[] parts = request.split(",");
            String comando = parts[0];
            String codigo  = parts[1];
            Classroom classroom = repository.findByCode(codigo);
            if (classroom == null) return "ERROR_SALON_NO_EXISTE";
            if (comando.equals("CONSULTAR_SALON")) {
                if (classroom.isAvailable()) { return "SALON_DISPONIBLE"; }
                return "SALON_RESERVADO";
            } else if (comando.equals("RESERVAR_SALON")) {
                if (!classroom.isAvailable()) return "ERROR_OPERACION_INVALIDA";
                classroom.reserve();
                return "RESERVA_EXITOSA";
            } else if (comando.equals("LIBERAR_SALON")) {
                if (classroom.isAvailable()) return "ERROR_OPERACION_INVALIDA";
                classroom.release();
                return "LIBERACION_EXITOSA";
            }
            return "ERROR_OPERACION_INVALIDA";
        } catch (Exception e) {
            return "ERROR: solicitud invalida";
        }
    }
}
