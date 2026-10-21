package co.edu.uniquindio.poo.rutaverdequindioproject.model;

/** Canal desde el que se confirma una reserva. */

public enum Canal {
    ASESOR("Pantalla del asesor"),
    KIOSCO("Kiosco Terminal Armenia"),
    MOVIL("App móvil");

    private final String texto;
    Canal(String t) { texto = t; }
    @Override public String toString() { return texto; }
}
