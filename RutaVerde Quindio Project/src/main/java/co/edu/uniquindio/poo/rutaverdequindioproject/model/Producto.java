package co.edu.uniquindio.poo.rutaverdequindioproject.model;

import java.util.List;

/**
 * PATRÓN COMPOSITE (estructural) - componente.
 * Una actividad suelta y un paquete con varios niveles se consultan exactamente igual.
 */

public interface Producto {
    String getCodigo();
    String getNombre();
    double precioPorPersona();
    double duracionHoras();

    // Lista plana de todas las actividades que contiene (sin importar los niveles). */
    List<Actividad> actividades();

    // true si este producto es 'otro' o lo contiene en cualquier nivel. */
    boolean incluye(Producto otro);

    default boolean tieneActividadSuspendida() {
        return actividades().stream().anyMatch(a -> a.getEstado() == EstadoActividad.SUSPENDIDA);
    }
}
