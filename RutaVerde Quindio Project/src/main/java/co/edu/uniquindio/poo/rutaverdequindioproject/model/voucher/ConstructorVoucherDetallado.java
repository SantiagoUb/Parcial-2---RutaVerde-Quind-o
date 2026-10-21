package co.edu.uniquindio.poo.rutaverdequindioproject.model.voucher;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Actividad;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Reserva;

import java.text.NumberFormat;
import java.util.Locale;

/** Voucher detallado para la pantalla del asesor. */

public class ConstructorVoucherDetallado extends ConstructorBase {
    @Override public void construirDetalle(Reserva r) {
        NumberFormat m = NumberFormat.getCurrencyInstance(Locale.of("es", "CO"));
        m.setMaximumFractionDigits(0);
        StringBuilder s = new StringBuilder();
        s.append("Reserva No. ").append(r.getNumero()).append("\n");
        s.append("Cliente: ").append(r.getCliente().getNombre()).append(" (").append(r.getCliente().getDocumento()).append(")\n");
        s.append("Producto: ").append(r.getProducto().getNombre()).append("\n");
        s.append("Fecha del viaje: ").append(r.getFechaViaje()).append("   Viajeros: ").append(r.getViajeros()).append("\n");
        s.append("Actividades incluidas:\n");
        for (Actividad a : r.getProducto().actividades())
            s.append("  - ").append(a.getNombre()).append(" (").append(a.getMunicipio()).append(", ")
                    .append(a.duracionHoras()).append(" h)\n");
        s.append("Guía: ").append(r.getGuia() == null ? "No incluido" : r.getGuia().getNombre()
                + " - " + r.getGuia().diasPara(r.getProducto().duracionHoras()) + " día(s)").append("\n");
        s.append("Transporte desde el hotel: ").append(r.isTransporte() ? "Sí" : "No").append("\n");
        s.append("Seguro de viaje: ").append(r.isSeguro() ? "Sí" : "No").append("\n");
        if (!r.getObservaciones().isEmpty()) s.append("Observaciones: ").append(r.getObservaciones()).append("\n");
        s.append("VALOR TOTAL: ").append(m.format(r.valorTotal())).append("\n");
        voucher.agregar(s.toString());
    }
}