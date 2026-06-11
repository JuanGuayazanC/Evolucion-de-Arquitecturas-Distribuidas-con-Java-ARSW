package classroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SalonServer {
    public static void main(String[] args) throws Exception {
        SalonRepository repository = new SalonRepository();
        ServerSocket serverSocket = new ServerSocket(36000);
        System.out.println("SalonServer TCP escuchando en puerto 36000...");
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

    private static String processRequest(String request, SalonRepository repository) {
        if (request == null || !request.contains(",")) return "ERROR_OPERACION_INVALIDA";
        String[] parts = request.split(",", 2);
        String operation = parts[0].trim();
        String salonId = parts[1].trim();
        Salon salon = repository.findById(salonId);
        if (salon == null) return "ERROR_SALON_NO_EXISTE";
        switch (operation) {
            case "CONSULTAR_SALON": return salon.getStatus();
            case "RESERVAR_SALON":
                if (!salon.isAvailable()) return "ERROR_OPERACION_INVALIDA";
                salon.reserve();
                return "RESERVA_EXITOSA";
            case "LIBERAR_SALON":
                if (salon.isAvailable()) return "ERROR_OPERACION_INVALIDA";
                salon.release();
                return "LIBERACION_EXITOSA";
            default: return "ERROR_OPERACION_INVALIDA";
        }
    }
}
