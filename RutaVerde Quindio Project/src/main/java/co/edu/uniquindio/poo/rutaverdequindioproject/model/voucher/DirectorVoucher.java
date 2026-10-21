package co.edu.uniquindio.poo.rutaverdequindioproject.model.voucher;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Agencia;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Reserva;

/** El Director fija el orden: encabezado -> detalle -> pie. No cambia al agregar canales. */

public class DirectorVoucher {
    public Voucher emitir(Reserva reserva, ConstructorVoucher constructor) {
        constructor.construirEncabezado(Agencia.getInstancia());
        constructor.construirDetalle(reserva);
        constructor.construirPie();
        return constructor.obtener();
    }
}
