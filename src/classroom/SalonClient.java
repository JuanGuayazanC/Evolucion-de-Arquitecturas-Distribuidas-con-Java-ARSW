package classroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SalonClient {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Operaciones: CONSULTAR_SALON, RESERVAR_SALON, LIBERAR_SALON");
        System.out.println("Salones disponibles: E301, E302, E303, E304");
        System.out.print("Ingrese operacion,salonId (ej: CONSULTAR_SALON,E303): ");
        String request = scanner.nextLine();
        Socket socket = new Socket("127.0.0.1", 36000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(request);
        System.out.println("Respuesta: " + in.readLine());
        in.close();
        out.close();
        socket.close();
    }
}
