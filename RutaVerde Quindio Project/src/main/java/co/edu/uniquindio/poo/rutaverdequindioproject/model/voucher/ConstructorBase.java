package co.edu.uniquindio.poo.rutaverdequindioproject.model.voucher;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Agencia;

/** Encabezado y pie son siempre iguales; cada canal solo implementa el detalle. */

public abstract class ConstructorBase implements ConstructorVoucher {
    protected final Voucher voucher = new Voucher();

    @Override public void construirEncabezado(Agencia a) {
        voucher.agregar("== " + a.getNombreComercial() + " | " + a.getRnt() + " ==\n");
    }
    @Override public void construirPie() {
        voucher.agregar("Política de cancelación: sin costo hasta 48 horas antes del viaje.\n");
    }
    @Override public Voucher obtener() { return voucher; }
}