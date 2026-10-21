package co.edu.uniquindio.poo.rutaverdequindioproject.controller;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Actividad;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Agencia;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Canal;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Cliente;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Guia;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Paquete;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Producto;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Repositorio;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Reserva;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.pago.AdaptadorPagoAndino;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.proveedor.PagoAndinoAPI;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio.ControlCupos;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio.DatosDemo;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio.NotificadorSimulado;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio.ProcesoConfirmacion;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio.ServicioCatalogo;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio.ServicioReportes;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.voucher.ConstructorTiqueteKiosco;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.voucher.ConstructorVoucher;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.voucher.ConstructorVoucherDetallado;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Raíz de composición de la aplicación (capa de controladores).
 *
 * - Construye y conecta los objetos del modelo (repositorio, servicios, fachada de confirmación).
 * - Expone listas observables que las vistas enlazan; así, cuando un controlador cambia el modelo
 *   y llama a refrescarXxx(), todas las pantallas que muestran esos datos se actualizan solas.
 */
public class AppContext {

    // ---- Modelo ----
    private final Repositorio repo = new Repositorio();
    private final ControlCupos cupos = new ControlCupos();
    private final NotificadorSimulado notificador = new NotificadorSimulado();
    private final ServicioCatalogo catalogo = new ServicioCatalogo(repo);
    private final ServicioReportes reportes = new ServicioReportes(repo);
    private final ProcesoConfirmacion confirmacion;

    // ---- Estado observable para las vistas ----
    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private final ObservableList<Guia> guias = FXCollections.observableArrayList();
    private final ObservableList<Actividad> actividades = FXCollections.observableArrayList();
    private final ObservableList<Paquete> paquetes = FXCollections.observableArrayList();
    private final ObservableList<Producto> productos = FXCollections.observableArrayList();
    private final ObservableList<Reserva> reservas = FXCollections.observableArrayList();
    private final ObservableList<String> correos = FXCollections.observableArrayList();

    /** Se incrementa cada vez que algo cambia; las vistas con datos calculados la escuchan para repintarse. */
    private final LongProperty cambios = new SimpleLongProperty();
    private final StringProperty mensaje = new SimpleStringProperty("Listo.");
    private final StringProperty nombreAgencia = new SimpleStringProperty();

    public AppContext() {
        Map<Canal, Supplier<ConstructorVoucher>> constructores = new EnumMap<>(Canal.class);
        constructores.put(Canal.ASESOR, ConstructorVoucherDetallado::new);
        constructores.put(Canal.KIOSCO, ConstructorTiqueteKiosco::new);
        constructores.put(Canal.MOVIL, ConstructorTiqueteKiosco::new); // el modelo aún no tiene un constructor propio para la app móvil

        confirmacion = new ProcesoConfirmacion(repo, cupos, new AdaptadorPagoAndino(new PagoAndinoAPI()),
                notificador, constructores);

        DatosDemo.cargar(repo);
        nombreAgencia.set(Agencia.getInstancia().getNombreComercial());
        refrescarClientes();
        refrescarGuias();
        refrescarActividades();
        refrescarPaquetes();
        refrescarReservas();
    }

    // ---- Acceso al modelo ----
    public Repositorio repo() { return repo; }
    public ControlCupos cupos() { return cupos; }
    public ServicioCatalogo catalogo() { return catalogo; }
    public ServicioReportes reportes() { return reportes; }
    public ProcesoConfirmacion confirmacion() { return confirmacion; }

    // ---- Listas observables ----
    public ObservableList<Cliente> clientes() { return clientes; }
    public ObservableList<Guia> guias() { return guias; }
    public ObservableList<Actividad> actividades() { return actividades; }
    public ObservableList<Paquete> paquetes() { return paquetes; }
    public ObservableList<Producto> productos() { return productos; }
    public ObservableList<Reserva> reservas() { return reservas; }
    public ObservableList<String> correos() { return correos; }

    // ---- Refrescos: llamar después de modificar el modelo ----
    public void refrescarClientes() { clientes.setAll(repo.clientes()); notificarCambios(); }
    public void refrescarGuias() { guias.setAll(repo.guias()); notificarCambios(); }
    public void refrescarActividades() {
        actividades.setAll(repo.actividades());
        productos.setAll(repo.productos());
        notificarCambios();
    }
    public void refrescarPaquetes() {
        paquetes.setAll(repo.paquetes());
        productos.setAll(repo.productos());
        notificarCambios();
    }
    public void refrescarReservas() {
        reservas.setAll(repo.reservas());
        correos.setAll(notificador.getBandeja());
        notificarCambios();
    }
    public void notificarCambios() { cambios.set(cambios.get() + 1); }

    public LongProperty cambiosProperty() { return cambios; }

    // ---- Barra de estado y encabezado ----
    public void informar(String texto) { mensaje.set(texto); }
    public StringProperty mensajeProperty() { return mensaje; }
    public StringProperty nombreAgenciaProperty() { return nombreAgencia; }
}
