package rmi.lab;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Parte III — RMI | Ejercicio: Lab Inventory System.
 *
 * <p>Interfaz remota del servicio de inventario de laboratorios.
 * Define las operaciones disponibles para clientes remotos.
 */
public interface LabService extends Remote {

    /**
     * @return lista de todos los equipos registrados
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    List<Equipment> consultarEquipos() throws RemoteException;

    /**
     * @param code código del equipo a consultar
     * @return el equipo, o null si no existe
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    Equipment consultarEquipo(String code) throws RemoteException;

    /**
     * @param code código del equipo a reservar
     * @return mensaje de resultado: RESERVA_EXITOSA o ERROR_*
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    String reservarEquipo(String code) throws RemoteException;

    /**
     * @param code código del equipo a liberar
     * @return mensaje de resultado: LIBERACION_EXITOSA o ERROR_*
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    String liberarEquipo(String code) throws RemoteException;
}
