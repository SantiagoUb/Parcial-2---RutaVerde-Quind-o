package co.edu.uniquindio.poo.rutaverdequindioproject.model;

import java.util.List;

public class Guia {
    public static final double HORAS_POR_DIA = 8;
    private final String identificacion, nombre, telefono;
    private final List<String> idiomas;
    private final double tarifaPorDia;

    public Guia(String identificacion, String nombre, List<String> idiomas, String telefono, double tarifaPorDia) {
        if (identificacion == null || identificacion.isBlank()) throw new DominioException("La identificación del guía es obligatoria.");
        if (nombre == null || nombre.isBlank()) throw new DominioException("El nombre del guía es obligatorio.");
        if (idiomas == null || idiomas.isEmpty()) throw new DominioException("El guía debe hablar al menos un idioma.");
        if (tarifaPorDia <= 0) throw new DominioException("La tarifa por día debe ser mayor que cero.");
        this.identificacion = identificacion.trim(); this.nombre = nombre.trim();
        this.idiomas = List.copyOf(idiomas); this.telefono = telefono == null ? "" : telefono.trim();
        this.tarifaPorDia = tarifaPorDia;
    }

    public boolean habla(String idioma) {
        return idiomas.stream().anyMatch(i -> i.equalsIgnoreCase(idioma));
    }
    // Un día cubre hasta 8 horas; se cobran los días adicionales necesarios. */
    public long diasPara(double horas) { return (long) Math.ceil(horas / HORAS_POR_DIA); }
    public double costoPara(double horas) { return diasPara(horas) * tarifaPorDia; }

    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public List<String> getIdiomas() { return idiomas; }
    public String getTelefono() { return telefono; }
    public double getTarifaPorDia() { return tarifaPorDia; }
    @Override public String toString() { return nombre + " (" + String.join("/", idiomas) + ")"; }

}
