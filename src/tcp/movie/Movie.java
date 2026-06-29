package tcp.movie;

/**
 * Represents a movie in the Movie Information System.
 */
public class Movie {
    private int id;
    private String title;
    private String director;
    private int year;

    /**
     * @param id       unique identifier
     * @param title    movie title
     * @param director director name
     * @param year     release year
     */
    public Movie(int id, String title, String director, int year) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
    }

    /** @return unique identifier */
    public int getId() {
        return id;
    }

    /** @return comma-separated text representation */
    public String toText() {
        return id + "," + title + "," + director + "," + year;
    }
}
