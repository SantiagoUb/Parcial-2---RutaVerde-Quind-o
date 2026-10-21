package co.edu.uniquindio.poo.rutaverdequindioproject.model;

import java.time.LocalDate;

public class Reserva {
    public static final double TRANSPORTE_POR_VIAJERO = 35_000;
    public static final double SEGURO_POR_VIAJERO = 15_000;
    public static final int MIN_VIAJEROS = 1, MAX_VIAJEROS = 15;

    private int numero;                 // lo asigna el sistema al registrar (consecutivo)
    private final Cliente cliente;
    private final Producto producto;
    private final LocalDate fechaViaje;
    private final int viajeros;
    private final Guia guia;
    private final boolean transporte, seguro;
    private final String observaciones;

    public Reserva(Cliente cliente, Producto producto, LocalDate fechaViaje, int viajeros,
                   Guia guia, boolean transporte, boolean seguro, String observaciones) {
        if (cliente == null) throw new DominioException("La reserva requiere un cliente.");
        if (producto == null) throw new DominioException("La reserva requiere un producto.");
        if (fechaViaje == null) throw new DominioException("La fecha del viaje es obligatoria.");
        if (fechaViaje.isBefore(LocalDate.now())) throw new DominioException("Fecha invalida.");
        if (viajeros < MIN_VIAJEROS || viajeros > MAX_VIAJEROS)
            throw new DominioException("El grupo debe tener entre 1 y 15 viajeros.");
        if (guia != null && !guia.habla(cliente.getIdioma()))
            throw new DominioException("El guía " + guia.getNombre() + " no habla el idioma preferido del cliente (" + cliente.getIdioma() + ").");
        if (producto.tieneActividadSuspendida())
            throw new DominioException("El producto incluye una actividad suspendida y no se puede reservar.");
        this.cliente = cliente;
        this.producto = producto;
        this.fechaViaje = fechaViaje;
        this.viajeros = viajeros;
        this.guia = guia;
        this.transporte = transporte;
        this.seguro = seguro;
        this.observaciones = observaciones == null ? "" : observaciones.trim();
    }

    public double costoGuia() { return guia == null ? 0 : guia.costoPara(producto.duracionHoras()); }
    public double costoTransporte() { return transporte ? TRANSPORTE_POR_VIAJERO * viajeros : 0; }
    public double costoSeguro() { return seguro ? SEGURO_POR_VIAJERO * viajeros : 0; }

    /** Precio por persona x viajeros + servicios opcionales. Redondeado a pesos enteros. */
    public long valorTotal() {
        return Math.round(producto.precioPorPersona() * viajeros + costoGuia() + costoTransporte() + costoSeguro());
    }

    public int getNumero() { return numero; }
    public void asignarNumero(int numero) { this.numero = numero; }
    public Cliente getCliente() { return cliente; }
    public Producto getProducto() { return producto; }
    public LocalDate getFechaViaje() { return fechaViaje; }
    public int getViajeros() { return viajeros; }
    public Guia getGuia() { return guia; }
    public boolean isTransporte() { return transporte; }
    public boolean isSeguro() { return seguro; }
    public String getObservaciones() { return observaciones; }
}
