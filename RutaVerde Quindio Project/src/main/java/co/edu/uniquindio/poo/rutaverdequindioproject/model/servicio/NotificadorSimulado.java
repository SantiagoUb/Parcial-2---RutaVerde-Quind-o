package co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio;

import java.util.ArrayList;
import java.util.List;

/** Simula el envío de correo: lo imprime en consola y lo guarda en una bandeja de salida. */

public class NotificadorSimulado implements Notificador {
    private final List<String> bandeja = new ArrayList<>();

    @Override public void enviar(String correo, String asunto, String cuerpo) {
        String mensaje = "Para: " + correo + "\nAsunto: " + asunto + "\n\n" + cuerpo;
        bandeja.add(mensaje);
        System.out.println("[CORREO]\n" + mensaje);
    }
    public List<String> getBandeja() { return bandeja; }
}