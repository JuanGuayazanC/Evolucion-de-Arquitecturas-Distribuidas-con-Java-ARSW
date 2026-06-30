package rmi.lab;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

/**
 * Parte III — RMI | Ejercicio: Lab Inventory System.
 *
 * <p>Cliente RMI que se conecta al {@link LabRmiServer} y permite consultar,
 * reservar y liberar equipos de laboratorio mediante un menú interactivo.
 */
public class LabRmiClient {

    /**
     * Conecta al registry, obtiene el stub de {@link LabService} y presenta
     * un menú para interactuar con el inventario.
     *
     * @param args argumentos de línea de comandos (no se usan)
     * @throws Exception si ocurre un error de conexión al registry
     */
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23001);
        LabService service = (LabService) registry.lookup("LabService");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Lab Inventory ---");
            System.out.println("1. Listar equipos");
            System.out.println("2. Consultar equipo");
            System.out.println("3. Reservar equipo");
            System.out.println("4. Liberar equipo");
            System.out.println("5. Salir");
            System.out.print("Opcion: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    List<Equipment> equipos = service.consultarEquipos();
                    for (Equipment e : equipos) {
                        System.out.println(e);
                    }
                    break;
                case "2":
                    System.out.print("Codigo del equipo: ");
                    String code = scanner.nextLine();
                    Equipment equipo = service.consultarEquipo(code);
                    System.out.println(equipo != null ? equipo : "ERROR_EQUIPO_NO_EXISTE");
                    break;
                case "3":
                    System.out.print("Codigo del equipo a reservar: ");
                    System.out.println(service.reservarEquipo(scanner.nextLine()));
                    break;
                case "4":
                    System.out.print("Codigo del equipo a liberar: ");
                    System.out.println(service.liberarEquipo(scanner.nextLine()));
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }
        scanner.close();
    }
}
