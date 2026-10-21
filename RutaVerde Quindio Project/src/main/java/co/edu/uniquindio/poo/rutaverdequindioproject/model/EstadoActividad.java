package co.edu.uniquindio.poo.rutaverdequindioproject.model;

public enum EstadoActividad {
    DISPONIBLE("Disponible"),
    SUSPENDIDA("Suspendida");

    private final String texto;
    EstadoActividad(String t) { texto = t; }
    @Override public String toString() { return texto; }
}
