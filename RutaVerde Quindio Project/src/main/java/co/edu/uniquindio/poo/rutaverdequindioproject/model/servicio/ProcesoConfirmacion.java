package co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Canal;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.DominioException;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Repositorio;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Reserva;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.pago.PasarelaPago;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.pago.ResultadoPago;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.pago.ValidadorTarjeta;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.voucher.ConstructorVoucher;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.voucher.DirectorVoucher;

import java.util.Map;
import java.util.function.Supplier;

/**
 * PATRÓN FACADE (estructural): único punto de entrada para confirmar una reserva desde cualquier
 * canal (asesor, kiosco, app móvil). Ninguna pantalla repite estos pasos.
 * Depende de abstracciones (DIP): PasarelaPago, Notificador y fábricas de ConstructorVoucher.
 */

public class ProcesoConfirmacion {
    private final Repositorio repo;
    private final ControlCupos cupos;
    private final PasarelaPago pasarela;
    private final Notificador notificador;
    private final DirectorVoucher director = new DirectorVoucher();
    private final Map<Canal, Supplier<ConstructorVoucher>> constructores;

    public ProcesoConfirmacion(Repositorio repo, ControlCupos cupos, PasarelaPago pasarela,
                               Notificador notificador, Map<Canal, Supplier<ConstructorVoucher>> constructores) {
        this.repo = repo; this.cupos = cupos; this.pasarela = pasarela;
        this.notificador = notificador; this.constructores = constructores;
    }

    public ResultadoConfirmacion confirmar(SolicitudReserva s, Canal canal) {
        // Paso 0: datos y reglas de la reserva (RN-10 a RN-14)
        Reserva reserva;
        try {
            reserva = new Reserva(s.cliente(), s.producto(), s.fecha(), s.viajeros(), s.guia(),
                    s.transporte(), s.seguro(), s.observaciones());
        } catch (DominioException e) {
            return fallo(s, e.getMessage());
        }
        // Paso 1: cupo en la fecha para todas las actividades
        String sinCupo = cupos.verificar(reserva.getProducto(), reserva.getFechaViaje(), reserva.getViajeros());
        if (sinCupo != null) return fallo(s, sinCupo);
        // Paso 2: validar tarjeta (Luhn) antes de tocar la pasarela
        if (!ValidadorTarjeta.esValida(s.tarjeta())) return fallo(s, "El número de tarjeta no es válido.");
        // Paso 3: cobrar
        String referencia = "RV-" + reserva.getCliente().getDocumento() + "-" + System.currentTimeMillis();
        ResultadoPago pago = pasarela.cobrar(s.tarjeta(), reserva.valorTotal(), referencia);
        if (!pago.aprobado()) return fallo(s, "Pago rechazado: " + pago.motivo());
        // Paso 4: descontar cupos
        cupos.descontar(reserva.getProducto(), reserva.getFechaViaje(), reserva.getViajeros());
        // Paso 5: registrar con consecutivo
        repo.registrar(reserva);
        // Paso 6: emitir voucher según el canal
        Supplier<ConstructorVoucher> fabrica = constructores.get(canal);
        String voucher = director.emitir(reserva, fabrica.get()).getTexto();
        // Paso 7: enviar por correo
        notificador.enviar(reserva.getCliente().getCorreo(), "Voucher de su reserva No. " + reserva.getNumero(),
                voucher + "Autorización de pago: " + pago.autorizacion() + "\n");
        return new ResultadoConfirmacion(true, voucher + "Autorización de pago: " + pago.autorizacion() + "\n", reserva);
    }

    /** Ante cualquier fallo: correo al cliente con el motivo; no se descuenta ningún cupo. */
    private ResultadoConfirmacion fallo(SolicitudReserva s, String motivo) {
        if (s.cliente() != null)
            notificador.enviar(s.cliente().getCorreo(), "No se pudo confirmar su reserva",
                    "Estimado(a) " + s.cliente().getNombre() + ", su reserva no pudo confirmarse.\nMotivo: " + motivo + "\n");
        return new ResultadoConfirmacion(false, motivo, null);
    }
}