package co.edu.uniquindio.poo.rutaverdequindioproject.model;

import java.util.*;

/** COMPOSITE - compuesto: contiene actividades y otros paquetes, a cualquier profundidad. */

public class Paquete implements Producto{
    public static final double DESCUENTO_MAXIMO = 20;
    private final String codigo, nombre;
    private final double porcentajeDescuento;
    private final List<Producto> hijos = new ArrayList<>();

    public Paquete(String codigo, String nombre, double porcentajeDescuento) {
        if (codigo == null || codigo.isBlank()) throw new DominioException("El código del paquete es obligatorio.");
        if (nombre == null || nombre.isBlank()) throw new DominioException("El nombre del paquete es obligatorio.");
        if (porcentajeDescuento < 0 || porcentajeDescuento > DESCUENTO_MAXIMO)
            throw new DominioException("El descuento debe estar entre 0 % y 20 %.");
        this.codigo = codigo.trim(); this.nombre = nombre.trim(); this.porcentajeDescuento = porcentajeDescuento;
    }


    //* RN-08 (sin ciclos) y RN-09 (sin actividades repetidas). */
    public void agregar(Producto nuevo) {
        if (nuevo == null) throw new DominioException("Debe seleccionar un producto para agregar.");
        if (nuevo == this || nuevo.incluye(this))
            throw new DominioException("Un paquete no puede contenerse a sí mismo, ni directa ni indirectamente.");
        Set<String> actuales = new HashSet<>();
        actividades().forEach(a -> actuales.add(a.getCodigo()));
        for (Actividad a : nuevo.actividades())
            if (actuales.contains(a.getCodigo()))
                throw new DominioException("La actividad «" + a.getNombre() + "» ya está incluida en el plan.");
        hijos.add(nuevo);
    }

    @Override public double precioPorPersona() {
        double suma = 0;
        for (Producto p : hijos) suma += p.precioPorPersona();
        return suma * (1 - porcentajeDescuento / 100.0);
    }
    @Override public double duracionHoras() {
        double suma = 0;
        for (Producto p : hijos) suma += p.duracionHoras();
        return suma;
    }
    @Override public List<Actividad> actividades() {
        List<Actividad> todas = new ArrayList<>();
        for (Producto p : hijos) todas.addAll(p.actividades());
        return todas;
    }
    @Override public boolean incluye(Producto otro) {
        if (otro == this) return true;
        for (Producto p : hijos) if (p.incluye(otro)) return true;
        return false;
    }

    @Override public String getCodigo() { return codigo; }
    @Override public String getNombre() { return nombre; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public List<Producto> getHijos() { return Collections.unmodifiableList(hijos); }
    @Override public String toString() { return codigo + " - " + nombre + " (paquete)"; }

}
