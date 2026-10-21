package co.edu.uniquindio.poo.rutaverdequindioproject.model.pago;

/** Interfaz objetivo del ADAPTER: lo único que el proceso de reservas conoce de una pasarela. */
public interface PasarelaPago {
    ResultadoPago cobrar(String numeroTarjeta, long valorPesos, String referencia);
}

