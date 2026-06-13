package classroom;

import java.util.HashMap;
import java.util.Map;

public class ClassroomRepository {
    private final Map<String, classroom> classrooms = new HashMap<>();

    public ClassroomRepository() {
        classrooms.put("E301", new classroom("E301"));
        classrooms.put("E302", new classroom("E302"));
        classrooms.put("E303", new classroom("E303"));
        classrooms.put("E304", new classroom("E304"));
    }

    public classroom findById(String id) {
        return classrooms.get(id);
    }
}
