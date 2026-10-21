package co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Reserva;

public record ResultadoConfirmacion(boolean exito, String mensaje, Reserva reserva) { }