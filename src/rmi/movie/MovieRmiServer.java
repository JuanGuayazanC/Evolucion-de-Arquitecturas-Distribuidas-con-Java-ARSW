package rmi.movie;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Parte III — RMI | Ejemplo: Movie RMI Server.
 *
 * <p>Crea el registry RMI en el puerto 23000, registra el servicio de películas
 * y espera llamadas remotas de los clientes.
 */
public class MovieRmiServer {

    /**
     * Inicia el registry RMI y registra el servicio de películas.
     *
     * @param args argumentos de línea de comandos (no se usan)
     * @throws Exception si ocurre un error al crear el registry o registrar el servicio
     */
    public static void main(String[] args) throws Exception {
        MovieServiceImpl service = new MovieServiceImpl();
        Registry registry = LocateRegistry.createRegistry(23000);
        registry.rebind("MovieService", service);
        System.out.println("MovieRmiServer ready on port 23000...");
    }
}
