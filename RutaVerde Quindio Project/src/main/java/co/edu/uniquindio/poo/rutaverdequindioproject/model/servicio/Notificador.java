package co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio;

public interface Notificador {
    void enviar(String correoDestino, String asunto, String cuerpo);
}

