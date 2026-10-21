package co.edu.uniquindio.poo.rutaverdequindioproject.model.pago;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.proveedor.PagoAndinoAPI;

/**
 * PATRÓN ADAPTER (estructural): traduce pesos -> centavos y códigos numéricos -> ResultadoPago.
 * Una segunda pasarela tendrá su propio adaptador sin tocar el proceso de reservas.
 */

public class AdaptadorPagoAndino implements PasarelaPago {
    private final PagoAndinoAPI api;

    public AdaptadorPagoAndino(PagoAndinoAPI api) {
        this.api = api;
    }

    @Override
    public ResultadoPago cobrar(String numeroTarjeta, long valorPesos, String referencia) {
        int codigo = api.procesarCobro(numeroTarjeta, valorPesos * 100L, referencia);
        return switch (codigo) {
            case 0 -> ResultadoPago.aprobado(api.ultimaAutorizacion());
            case 51 -> ResultadoPago.rechazado("Fondos insuficientes.");
            case 14 -> ResultadoPago.rechazado("Tarjeta inválida.");
            case 91 -> ResultadoPago.rechazado("La pasarela de pagos no está disponible. Intente más tarde.");
            default -> ResultadoPago.rechazado("Error desconocido de la pasarela (código " + codigo + ").");
        };
    }
}