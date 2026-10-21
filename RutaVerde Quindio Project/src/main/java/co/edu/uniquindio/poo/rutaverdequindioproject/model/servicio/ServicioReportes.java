package co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.DominioException;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Producto;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Repositorio;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Reserva;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServicioReportes {
    public record ProductoTop(Producto producto, int viajeros) { }
    private final Repositorio repo;

    public ServicioReportes(Repositorio repo) { this.repo = repo; }

    /** Ingresos de reservas confirmadas con viaje entre desde y hasta (ambos incluidos). */
    public long ingresos(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) throw new DominioException("Debe indicar la fecha inicial y la final.");
        if (desde.isAfter(hasta)) throw new DominioException("La fecha inicial no puede ser posterior a la final.");
        long total = 0;
        for (Reserva r : repo.reservas())
            if (!r.getFechaViaje().isBefore(desde) && !r.getFechaViaje().isAfter(hasta)) total += r.valorTotal();
        return total;
    }

    /** Producto con más viajeros acumulados; null si no hay reservas. */
    public ProductoTop productoConMasViajeros() {
        Map<Producto, Integer> acumulado = new LinkedHashMap<>();
        for (Reserva r : repo.reservas()) acumulado.merge(r.getProducto(), r.getViajeros(), Integer::sum);
        ProductoTop mayor = null;
        for (Map.Entry<Producto, Integer> e : acumulado.entrySet())
            if (mayor == null || e.getValue() > mayor.viajeros()) mayor = new ProductoTop(e.getKey(), e.getValue());
        return mayor;
    }
}
