package classroom;

public class Salon {
    private String id;
    private boolean available;

    public Salon(String id) {
        this.id = id;
        this.available = true;
    }

    public String getId() { return id; }
    public boolean isAvailable() { return available; }
    public void reserve() { this.available = false; }
    public void release() { this.available = true; }

    public String getStatus() {
        return available ? "SALON_DISPONIBLE" : "SALON_RESERVADO";
    }
}
