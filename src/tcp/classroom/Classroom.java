package tcp.classroom;

/**
 * Represents a classroom with a code and availability status.
 */
public class Classroom {

    // TODO: declarar atributos (código del salón y su estado)

    /**
     * @param code classroom code, e.g. "E301"
     */
    public Classroom(String code) {
        // TODO: inicializar atributos (el salón empieza disponible)
    }

    /**
     * @return true if the classroom is available
     */
    public boolean isAvailable() {
        // TODO
        return false;
    }

    /**
     * Marks the classroom as reserved.
     */
    public void reserve() {
        // TODO
    }

    /**
     * Marks the classroom as available.
     */
    public void release() {
        // TODO
    }

    /**
     * @return the classroom code
     */
    public String getCode() {
        // TODO
        return null;
    }
}
