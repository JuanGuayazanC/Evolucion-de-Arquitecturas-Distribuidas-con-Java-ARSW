package rmi.movie;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementación del servicio remoto de películas.
 *
 * <p>Extiende {@link UnicastRemoteObject} para exportar el objeto a la red —
 * es decir, hacerlo accesible remotamente desde otra JVM via RMI.
 */
public class MovieServiceImpl extends UnicastRemoteObject implements MovieService {

    private Map<Integer, Movie> movies = new HashMap<>();

    /**
     * Inicializa el repositorio con tres películas de ejemplo.
     *
     * @throws RemoteException si ocurre un error al exportar el objeto
     */
    public MovieServiceImpl() throws RemoteException {
        super();
        movies.put(1, new Movie(1, "Interstellar", "Christopher Nolan", 2014));
        movies.put(2, new Movie(2, "Matrix", "Wachowski", 1999));
        movies.put(3, new Movie(3, "Inception", "Christopher Nolan", 2010));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie findById(int id) throws RemoteException {
        return movies.get(id);
    }
}
