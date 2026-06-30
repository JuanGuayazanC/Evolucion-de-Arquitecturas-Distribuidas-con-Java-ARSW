package rmi.lab;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parte III — RMI | Ejercicio: Lab Inventory System.
 *
 * <p>Implementación del servicio remoto {@link LabService}.
 * Extiende {@link UnicastRemoteObject} para ser exportado a la red vía RMI.
 */
public class LabServiceImpl extends UnicastRemoteObject implements LabService {

    private Map<String, Equipment> inventory = new HashMap<>();

    /**
     * Inicializa el inventario con equipos de ejemplo y lo exporta a la red.
     *
     * @throws RemoteException si ocurre un error al exportar el objeto
     */
    public LabServiceImpl() throws RemoteException {
        super();
        inventory.put("PC-01", new Equipment("Lab-A", "Computador Dell", "PC-01"));
        inventory.put("PC-02", new Equipment("Lab-B", "Computador HP", "PC-02"));
        inventory.put("PC-03", new Equipment("Lab-C", "Computador Lenovo", "PC-03"));
    }


    /**
     * @return lista de todos los equipos registrados
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    @Override
    public List<Equipment> consultarEquipos() throws RemoteException {
        return new ArrayList<>(inventory.values());
    }

    /**
     * @param code código del equipo a consultar
     * @return el equipo, o null si no existe
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    @Override
    public Equipment consultarEquipo(String code) throws RemoteException {
        return inventory.get(code);
    }

    /**
     * @param code código del equipo a reservar
     * @return RESERVA_EXITOSA, ERROR_EQUIPO_NO_EXISTE o ERROR_OPERACION_INVALIDA
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    @Override
    public String reservarEquipo(String code) throws RemoteException {
        if (inventory.containsKey(code)) {
            Equipment equipo = inventory.get(code);
            if (equipo.isAvailable()) {
                equipo.reserve();
                return "RESERVA_EXITOSA";
            } else {
                return "ERROR_OPERACION_INVALIDA";
            }
        } else {
            return "ERROR_EQUIPO_NO_EXISTE";
        }
    }

    /**
     * @param code código del equipo a liberar
     * @return LIBERACION_EXITOSA, ERROR_EQUIPO_NO_EXISTE o ERROR_OPERACION_INVALIDA
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    @Override
    public String liberarEquipo(String code) throws RemoteException {
        if (inventory.containsKey(code)) {
            Equipment equipo = inventory.get(code);
            if (!equipo.isAvailable()) {
                equipo.release();
                return "LIBERACION_EXITOSA";
            }
            return "ERROR_OPERACION_INVALIDA";
        }
        return "ERROR_EQUIPO_NO_EXISTE";
    }
}
