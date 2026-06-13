import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LabInventoryServer {
    public static void main(String[] args) throws Exception {
        LabInventoryService service = new LabInventoryServiceImpl();
        Registry registry = LocateRegistry.createRegistry(23001);
        registry.rebind("labInventory", service);
        System.out.println("LabInventoryService RMI publicado en puerto 23001...");
    }
}
