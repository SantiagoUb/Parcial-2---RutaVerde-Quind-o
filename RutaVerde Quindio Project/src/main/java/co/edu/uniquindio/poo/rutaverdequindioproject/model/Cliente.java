package co.edu.uniquindio.poo.rutaverdequindioproject.model;

import java.util.regex.Pattern;

public class Cliente {
    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private final String documento;
    private final String nombre, telefono, correo, pais, idioma;

    public Cliente(String documento, String nombre, String telefono, String correo, String pais, String idioma) {
        if (vacio(documento)) throw new DominioException("El documento o pasaporte es obligatorio.");
        if (vacio(nombre)) throw new DominioException("El nombre completo es obligatorio.");
        if (vacio(telefono)) throw new DominioException("El teléfono es obligatorio.");
        if (vacio(correo) || !CORREO.matcher(correo.trim()).matches())
            throw new DominioException("El correo electrónico no tiene un formato inválido.");
        if (vacio(pais)) throw new DominioException("El país de origen es obligatorio.");
        if (vacio(idioma)) throw new DominioException("Ingrese el idioma");
        this.documento = documento.trim(); this.nombre = nombre.trim(); this.telefono = telefono.trim();
        this.correo = correo.trim(); this.pais = pais.trim(); this.idioma = idioma.trim();
    }

    private static boolean vacio(String s) { return s == null || s.isBlank(); }

    public String getDocumento() { return documento; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getPais() { return pais; }
    public String getIdioma() { return idioma; }

    @Override public String toString() { return nombre + " (" + documento + ")"; }

}
