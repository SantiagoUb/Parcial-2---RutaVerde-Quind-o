package co.edu.uniquindio.poo.rutaverdequindioproject.model.voucher;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Reserva;

/** Tiquete corto del kiosco: el detalle es una sola línea con un código para la entrada. */

public class ConstructorTiqueteKiosco extends ConstructorBase {
    @Override public void construirDetalle(Reserva r) {
        String codigo = "RV-" + r.getNumero() + "-" + r.getFechaViaje().toString().replace("-", "");
        voucher.agregar("Reserva " + r.getNumero() + " | " + r.getProducto().getNombre() + " | "
                + r.getViajeros() + " pax | " + r.getFechaViaje() + " | Código: " + codigo + "\n");
    }
}
