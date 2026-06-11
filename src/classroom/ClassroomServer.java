package classroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClassroomServer {
    public static void main(String[] args) throws Exception {
        ClassroomRepository repository = new ClassroomRepository();
        ServerSocket serverSocket = new ServerSocket(36000);
        System.out.println("ClassroomServer TCP escuchando en puerto 36000...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String request = in.readLine();
            out.println(processRequest(request, repository));
            in.close();
            out.close();
            clientSocket.close();
        }
    }

    private static String processRequest(String request, ClassroomRepository repository) {
        if (request == null || !request.contains(",")) return "ERROR_OPERACION_INVALIDA";
        String[] parts = request.split(",", 2);
        String operation = parts[0].trim();
        String classroomId = parts[1].trim();
        classroom c = repository.findById(classroomId);
        if (c == null) return "ERROR_CLASSROOM_NO_EXISTE";
        switch (operation) {
            case "CONSULTAR_CLASSROOM": return c.getStatus();
            case "RESERVAR_CLASSROOM":
                if (!c.isAvailable()) return "ERROR_OPERACION_INVALIDA";
                c.reserve();
                return "RESERVA_EXITOSA";
            case "LIBERAR_CLASSROOM":
                if (c.isAvailable()) return "ERROR_OPERACION_INVALIDA";
                c.release();
                return "LIBERACION_EXITOSA";
            default: return "ERROR_OPERACION_INVALIDA";
        }
    }
}
