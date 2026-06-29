package tcp.movie;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory repository of movies.
 */
public class MovieRepository {
    private Map<Integer, Movie> movies = new HashMap<>();

    /** Initializes the repository with three sample movies. */
    public MovieRepository() {
        movies.put(1, new Movie(1, "Interstellar", "Christopher Nolan", 2014));
        movies.put(2, new Movie(2, "Matrix", "Wachowski", 1999));
        movies.put(3, new Movie(3, "Inception", "Christopher Nolan", 2010));
    }

    /**
     * @param id movie identifier
     * @return the Movie, or null if not found
     */
    public Movie findById(int id) {
        return movies.get(id);
    }
}
