package rmi.movie;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz remota del servicio de películas.
 *
 * <p>Extiende {@link Remote} para indicarle al sistema RMI que sus métodos
 * pueden ser invocados desde otra JVM. Cada método debe declarar
 * {@link RemoteException} porque una llamada remota puede fallar por red.
 */
public interface MovieService extends Remote {

    /**
     * Busca una película por su ID.
     *
     * @param id identificador de la película
     * @return la película, o {@code null} si no existe
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    Movie findById(int id) throws RemoteException;
}
