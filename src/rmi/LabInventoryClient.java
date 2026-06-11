import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class LabInventoryClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23001);
        LabInventoryService service = (LabInventoryService) registry.lookup("labInventory");

        System.out.println("=== Todos los equipos ===");
        List<String> equipos = service.consultarEquipos();
        for (String e : equipos) System.out.println(e);

        System.out.println("\n=== Reservar PC01 ===");
        System.out.println(service.reservarEquipo("PC01") ? "Reserva exitosa" : "No se pudo reservar");

        System.out.println("\n=== Consultar PC01 ===");
        System.out.println(service.consultarEquipo("PC01"));

        System.out.println("\n=== Liberar PC01 ===");
        System.out.println(service.liberarEquipo("PC01") ? "Liberacion exitosa" : "No se pudo liberar");
    }
}
