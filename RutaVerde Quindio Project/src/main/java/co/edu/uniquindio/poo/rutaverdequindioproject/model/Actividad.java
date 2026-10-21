package co.edu.uniquindio.poo.rutaverdequindioproject.model;

import java.util.List;

public class Actividad implements Producto{
    private final String codigo, nombre, municipio;
    private final double duracionHoras, precio;
    private final int cupoMaximoPorDia;
    private final NivelDificultad dificultad;
    private EstadoActividad estado;

    public Actividad(String codigo, String nombre, String municipio, double duracionHoras, double precio,
                     int cupoMaximoPorDia, NivelDificultad dificultad, EstadoActividad estado) {
        if (codigo == null || codigo.isBlank()) throw new DominioException("El código de la actividad es obligatorio.");
        if (nombre == null || nombre.isBlank()) throw new DominioException("El nombre de la actividad es obligatorio.");
        if (municipio == null || municipio.isBlank()) throw new DominioException("El municipio es obligatorio.");
        if (duracionHoras <= 0) throw new DominioException("La duración debe ser mayor que cero.");
        if (precio <= 0) throw new DominioException("El precio por persona debe ser mayor que cero.");
        if (cupoMaximoPorDia <= 0) throw new DominioException("El cupo máximo por día debe ser mayor que cero.");
        if (dificultad == null || estado == null) throw new DominioException("Dificultad y estado son obligatorios.");
        this.codigo = codigo.trim(); this.nombre = nombre.trim(); this.municipio = municipio.trim();
        this.duracionHoras = duracionHoras; this.precio = precio; this.cupoMaximoPorDia = cupoMaximoPorDia;
        this.dificultad = dificultad; this.estado = estado;
    }

    @Override public String getCodigo() { return codigo; }
    @Override public String getNombre() { return nombre; }
    @Override public double precioPorPersona() { return precio; }
    @Override public double duracionHoras() { return duracionHoras; }
    @Override public List<Actividad> actividades() { return List.of(this); }
    @Override public boolean incluye(Producto otro) { return otro == this; }

    public String getMunicipio() { return municipio; }
    public int getCupoMaximoPorDia() { return cupoMaximoPorDia; }
    public NivelDificultad getDificultad() { return dificultad; }
    public EstadoActividad getEstado() { return estado; }
    public void setEstado(EstadoActividad estado) { this.estado = estado; }
    @Override public String toString() { return codigo + " - " + nombre; }

}
