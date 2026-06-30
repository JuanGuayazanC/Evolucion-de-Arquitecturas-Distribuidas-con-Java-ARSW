package rmi.lab;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Parte III — RMI | Ejercicio: Lab Inventory System.
 *
 * <p>Servidor RMI que registra {@link LabServiceImpl} en el registry del puerto 23001
 * y queda esperando peticiones de clientes remotos.
 */
public class LabRmiServer {

    /**
     * Crea el registry, registra el servicio y espera conexiones.
     *
     * @param args argumentos de línea de comandos (no se usan)
     * @throws Exception si ocurre un error al crear el registry o exportar el servicio
     */
    public static void main(String[] args) throws Exception {
        LabServiceImpl service = new LabServiceImpl();
        Registry registry = LocateRegistry.createRegistry(23001);
        registry.rebind("LabService", service);
        System.out.println("LabRmiServer ready on port 23001...");
    }
}
