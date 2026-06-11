package classroom;

import java.util.HashMap;
import java.util.Map;

public class SalonRepository {
    private final Map<String, Salon> salons = new HashMap<>();

    public SalonRepository() {
        salons.put("E301", new Salon("E301"));
        salons.put("E302", new Salon("E302"));
        salons.put("E303", new Salon("E303"));
        salons.put("E304", new Salon("E304"));
    }

    public Salon findById(String id) {
        return salons.get(id);
    }
}
