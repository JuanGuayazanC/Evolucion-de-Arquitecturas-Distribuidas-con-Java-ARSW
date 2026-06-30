package rmi.movie;

import java.io.Serializable;

/**
 * Parte III — RMI | Ejemplo: Movie Information System over RMI.
 *
 * <p>Modelo de datos de una película. Implementa {@link Serializable} para que
 * los objetos puedan ser enviados a través de la red entre JVMs en RMI.
 */
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String director;
    private int year;

    /**
     * @param id       identificador único
     * @param title    título de la película
     * @param director nombre del director
     * @param year     año de estreno
     */
    public Movie(int id, String title, String director, int year) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
    }

    /** @return identificador único */
    public int getId() { return id; }

    /** @return título de la película */
    public String getTitle() { return title; }

    /** @return nombre del director */
    public String getDirector() { return director; }

    /** @return año de estreno */
    public int getYear() { return year; }

    @Override
    public String toString() {
        return id + "," + title + "," + director + "," + year;
    }
}
