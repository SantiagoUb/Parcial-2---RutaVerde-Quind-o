package co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio;


import co.edu.uniquindio.poo.rutaverdequindioproject.model.Cliente;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Guia;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Producto;

import java.time.LocalDate;

public record SolicitudReserva(Cliente cliente, Producto producto, LocalDate fecha, int viajeros, Guia guia,
                               boolean transporte, boolean seguro, String observaciones, String tarjeta) { }