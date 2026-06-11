import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabInventoryServiceImpl extends UnicastRemoteObject implements LabInventoryService {
    private final Map<String, Equipment> equipment = new HashMap<>();

    public LabInventoryServiceImpl() throws RemoteException {
        equipment.put("PC01", new Equipment("PC01", "Computador Dell", "Lab Sistemas"));
        equipment.put("PC02", new Equipment("PC02", "Computador HP",   "Lab Sistemas"));
        equipment.put("RO01", new Equipment("RO01", "Router Cisco",    "Lab Redes"));
        equipment.put("RO02", new Equipment("RO02", "Switch Cisco",    "Lab Redes"));
        equipment.put("AR01", new Equipment("AR01", "Arduino Mega",    "Lab Electronica"));
    }

    @Override
    public List<String> consultarEquipos() throws RemoteException {
        List<String> list = new ArrayList<>();
        for (Equipment e : equipment.values()) list.add(e.toString());
        return list;
    }

    @Override
    public String consultarEquipo(String codigo) throws RemoteException {
        Equipment e = equipment.get(codigo);
        return e == null ? "ERROR: equipo no encontrado" : e.toString();
    }

    @Override
    public boolean reservarEquipo(String codigo) throws RemoteException {
        Equipment e = equipment.get(codigo);
        if (e == null || !e.isAvailable()) return false;
        e.reserve();
        return true;
    }

    @Override
    public boolean liberarEquipo(String codigo) throws RemoteException {
        Equipment e = equipment.get(codigo);
        if (e == null || e.isAvailable()) return false;
        e.release();
        return true;
    }
}
