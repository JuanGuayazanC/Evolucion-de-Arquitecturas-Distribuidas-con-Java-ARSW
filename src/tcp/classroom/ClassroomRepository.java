package tcp.classroom;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory repository of classrooms.
 */
public class ClassroomRepository {

    private Map<String, Classroom> classroooms = new HashMap<>();

    /**
     * Initializes the repository with classrooms E301, E302, E303 and E304.
     */
    public ClassroomRepository() {
        classroooms.put("E301" ,new Classroom("E301"));
        classroooms.put("E302" ,new Classroom("E302"));
        classroooms.put("E303" ,new Classroom("E303"));
        classroooms.put("E304" ,new Classroom("E304"));
    }

    /**
     * @param code classroom code
     * @return the Classroom, or null if not found
     */
    public Classroom findByCode(String code) {
        return classroooms.get(code);
    }
}
