package tcp.classroom;

/**
 * Represents a classroom with a code and availability status.
 */
public class Classroom {

    private String code;
    private State state;

    /**
     * @param code classroom code, e.g. "E301"
     */
    public Classroom(String code) {
        this.code = code;
        this.state = State.DISPONIBLE;
    }

    /**
     * @return true if the classroom is available
     */
    public boolean isAvailable() {
        return state == State.DISPONIBLE;
    }

    /**
     * Marks the classroom as reserved.
     */
    public void reserve() {
        if (isAvailable()) {state=State.RESERVADO;}
    }

    /**
     * Marks the classroom as available.
     */
    public void release() {
        if (!isAvailable()) {state=State.DISPONIBLE;}
    }

    /**
     * @return the classroom code
     */
    public String getCode() {
        return code;
    }
}
