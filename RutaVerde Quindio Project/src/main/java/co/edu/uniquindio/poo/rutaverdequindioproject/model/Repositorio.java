package co.edu.uniquindio.poo.rutaverdequindioproject.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Almacén en memoria de todas las entidades (modelo de datos de la aplicación). */

public class Repositorio {
    private final Map<String, Cliente> clientes = new LinkedHashMap<>();
    private final Map<String, Guia> guias = new LinkedHashMap<>();
    private final Map<String, Actividad> actividades = new LinkedHashMap<>();
    private final Map<String, Paquete> paquetes = new LinkedHashMap<>();
    private final List<Reserva> reservas = new ArrayList<>();
    private int consecutivo = 0;

    // Guardar = crear o actualizar por identificador
    public void guardar(Cliente c) { clientes.put(c.getDocumento(), c); }
    public void guardar(Guia g) { guias.put(g.getIdentificacion(), g); }
    public void guardar(Actividad a) {
        if (paquetes.containsKey(a.getCodigo())) throw new DominioException("Ya existe un paquete con ese código.");
        actividades.put(a.getCodigo(), a);
    }
    public void guardar(Paquete p) {
        if (paquetes.containsKey(p.getCodigo()) || actividades.containsKey(p.getCodigo()))
            throw new DominioException("Ya existe un producto con el código " + p.getCodigo() + ".");
        paquetes.put(p.getCodigo(), p);
    }
    public void eliminarCliente(String doc) {
        if (reservas.stream().anyMatch(r -> r.getCliente().getDocumento().equals(doc)))
            throw new DominioException("No se puede eliminar: el cliente tiene reservas.");
        clientes.remove(doc);
    }
    public void eliminarGuia(String id) { guias.remove(id); }

    /** Registra una reserva confirmada con número consecutivo. */
    public synchronized void registrar(Reserva r) { r.asignarNumero(++consecutivo); reservas.add(r); }

    public List<Cliente> clientes() { return new ArrayList<>(clientes.values()); }
    public List<Guia> guias() { return new ArrayList<>(guias.values()); }
    public List<Actividad> actividades() { return new ArrayList<>(actividades.values()); }
    public List<Paquete> paquetes() { return new ArrayList<>(paquetes.values()); }
    public List<Reserva> reservas() { return new ArrayList<>(reservas); }
    public List<Producto> productos() {
        List<Producto> todos = new ArrayList<>(actividades.values());
        todos.addAll(paquetes.values());
        return todos;
    }
}
