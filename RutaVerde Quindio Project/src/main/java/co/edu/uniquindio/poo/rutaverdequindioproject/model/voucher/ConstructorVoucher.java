package co.edu.uniquindio.poo.rutaverdequindioproject.model.voucher;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Agencia;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Reserva;

/** PATRÓN BUILDER (creacional): interfaz del constructor. Un canal nuevo = una clase nueva. */

public interface ConstructorVoucher {
    void construirEncabezado(Agencia agencia);
    void construirDetalle(Reserva reserva);
    void construirPie();
    Voucher obtener();
}
