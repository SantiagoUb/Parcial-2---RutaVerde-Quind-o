package co.edu.uniquindio.poo.rutaverdequindioproject.model;

/** PATRÓN SINGLETON (creacional): una única instancia con los datos básicos de la agencia. */

public class Agencia {
    private static Agencia instancia;

    private String nombreComercial = "RutaVerde Quindío";
    private String nit = "901.234.567-8";
    private String rnt = "RNT 123456";
    private String direccion = "Cra. 14 # 20-30, Armenia, Quindío";
    private String telefono = "+57 606 700 0000";
    private String correo = "reservas@rutaverde.co";
    private String paginaWeb = "www.rutaverde.co";

    private Agencia() { }

    public static synchronized Agencia getInstancia() {
        if (instancia == null) instancia = new Agencia();
        return instancia;
    }

    public String getNombreComercial() { return nombreComercial; }
    public String getNit() { return nit; }
    public String getRnt() { return rnt; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getPaginaWeb() { return paginaWeb; }

    public void actualizar(String nombre, String nit, String rnt, String direccion,
                           String telefono, String correo, String web) {
        this.nombreComercial = nombre;
        this.nit = nit;
        this.rnt = rnt;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.paginaWeb = web;
    }
}
