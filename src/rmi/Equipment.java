import java.io.Serializable;

public class Equipment implements Serializable {
    private String code;
    private String name;
    private String lab;
    private boolean available;

    public Equipment(String code, String name, String lab) {
        this.code = code;
        this.name = name;
        this.lab = lab;
        this.available = true;
    }

    public String getCode() { return code; }
    public boolean isAvailable() { return available; }
    public void reserve() { this.available = false; }
    public void release() { this.available = true; }

    @Override
    public String toString() {
        return code + " - " + name + " [" + lab + "] - " + (available ? "DISPONIBLE" : "RESERVADO");
    }
}
