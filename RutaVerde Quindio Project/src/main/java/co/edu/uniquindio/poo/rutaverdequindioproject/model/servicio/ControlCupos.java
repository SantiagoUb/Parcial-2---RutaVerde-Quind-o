package co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Actividad;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Producto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/** Cupos ocupados por actividad y fecha (evita la sobreventa). */
public class ControlCupos {
    private final Map<String, Integer> ocupados = new HashMap<>();

    private static String clave(Actividad a, LocalDate f) { return a.getCodigo() + "|" + f; }

    public int ocupados(Actividad a, LocalDate f) { return ocupados.getOrDefault(clave(a, f), 0); }
    public int disponibles(Actividad a, LocalDate f) { return a.getCupoMaximoPorDia() - ocupados(a, f); }

    /** @return null si hay cupo para todas las actividades; si no, el motivo. */
    public String verificar(Producto p, LocalDate fecha, int viajeros) {
        for (Actividad a : p.actividades())
            if (ocupados(a, fecha) + viajeros > a.getCupoMaximoPorDia())
                return "No hay cupo en «" + a.getNombre() + "» para el " + fecha + " (disponibles: "
                        + disponibles(a, fecha) + ", solicitados: " + viajeros + ").";
        return null;
    }

    public void descontar(Producto p, LocalDate fecha, int viajeros) {
        for (Actividad a : p.actividades()) ocupados.merge(clave(a, fecha), viajeros, Integer::sum);
    }
}

