package rmi.lab;


import java.io.Serializable;

/**
 * Parte III — RMI | Ejercicio: Lab Inventory System.
 *
 * <p>Modelo de un equipo de laboratorio. Implementa {@link Serializable} para que
 * los objetos puedan ser enviados a través de la red entre JVMs en RMI.
 */
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String lab;
    private EquipmentState state;


    public Equipment( String lab, String name, String code) {
        this.state = EquipmentState.DISPONIBLE;
        this.lab = lab;
        this.name = name;
        this.code = code;
    }

    /** @return true si el equipo está disponible */
    public boolean isAvailable() {
        return state == EquipmentState.DISPONIBLE;
    }

    /** Marca el equipo como reservado. */
    public void reserve() {
        if (isAvailable()) {state=EquipmentState.RESERVADO;}
    }

    /** Marca el equipo como disponible. */
    public void release() {
        if (!isAvailable()) {state=EquipmentState.DISPONIBLE;}
    }

    /** @return código del equipo */
    public String getCode() {
        return code;
    }

    /** @return laboratorio del equipo */
    public String getLab() {
        return lab;
    }

    public EquipmentState getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return code + " | " + name + " | " + lab + " | " + state;
    }
}
