package co.edu.uniquindio.poo.rutaverdequindioproject.model.pago;

/** Resultado del cobro expresado en los términos de la agencia (no en códigos de la pasarela). */

public record ResultadoPago(boolean aprobado, String motivo, String autorizacion) {
    public static ResultadoPago aprobado(String autorizacion) { return new ResultadoPago(true, "", autorizacion); }
    public static ResultadoPago rechazado(String motivo) { return new ResultadoPago(false, motivo, ""); }
}
