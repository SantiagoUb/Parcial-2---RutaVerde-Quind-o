package co.edu.uniquindio.poo.rutaverdequindioproject.model;

public enum NivelDificultad {
    BAJA("Baja"),
    MEDIA("Media"),
    ALTA("Alta");

    private final String texto;
    NivelDificultad(String t) { texto = t; }
    @Override public String toString() { return texto; }
}
