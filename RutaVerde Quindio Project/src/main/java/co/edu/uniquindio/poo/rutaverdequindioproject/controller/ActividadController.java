package co.edu.uniquindio.poo.rutaverdequindioproject.controller;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Actividad;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.DominioException;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.EstadoActividad;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.NivelDificultad;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Alertas;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Formato;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Tablas;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/** Catálogo de actividades: alta, suspensión/reactivación y consulta de cupos por fecha. */
public class ActividadController {
    private final AppContext ctx;

    @FXML private TextField txtCodigo, txtNombre, txtMunicipio, txtDuracion, txtPrecio, txtCupo;
    @FXML private ComboBox<NivelDificultad> cmbDificultad;
    @FXML private ComboBox<EstadoActividad> cmbEstado;
    @FXML private DatePicker dpCupos;
    @FXML private TableView<Actividad> tabla;
    @FXML private TableColumn<Actividad, String> colCodigo, colNombreAct, colMunicipioAct, colDuracionAct,
            colPrecioAct, colCupoAct, colDificultadAct, colEstadoAct, colDisponibles;

    public ActividadController(AppContext ctx) { this.ctx = ctx; }

    @FXML
    private void initialize() {
        cmbDificultad.setItems(FXCollections.observableArrayList(NivelDificultad.values()));
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoActividad.values()));
        valoresPorDefecto();

        dpCupos.setValue(LocalDate.now());
        Formato.soloFechasFuturas(dpCupos);
        dpCupos.valueProperty().addListener((obs, anterior, actual) -> tabla.refresh());

        Tablas.texto(colCodigo, Actividad::getCodigo);
        Tablas.texto(colNombreAct, Actividad::getNombre);
        Tablas.texto(colMunicipioAct, Actividad::getMunicipio);
        Tablas.texto(colDuracionAct, a -> Formato.horas(a.duracionHoras()));
        Tablas.texto(colPrecioAct, a -> Formato.moneda(a.precioPorPersona()));
        Tablas.texto(colCupoAct, a -> String.valueOf(a.getCupoMaximoPorDia()));
        Tablas.texto(colDificultadAct, a -> a.getDificultad().toString());
        Tablas.texto(colEstadoAct, a -> a.getEstado().toString());
        Tablas.texto(colDisponibles, a -> dpCupos.getValue() == null
                ? "—" : String.valueOf(ctx.cupos().disponibles(a, dpCupos.getValue())));
        Tablas.ajustar(tabla);
        tabla.setItems(ctx.actividades());

        // las reservas descuentan cupos y el estado cambia: se repinta la tabla
        ctx.cambiosProperty().addListener((obs, anterior, actual) -> tabla.refresh());
    }

    @FXML
    private void onCrear() {
        try {
            Actividad actividad = new Actividad(txtCodigo.getText(), txtNombre.getText(), txtMunicipio.getText(),
                    Formato.decimal("Duración (horas)", txtDuracion.getText()),
                    Formato.decimal("Precio por persona", txtPrecio.getText()),
                    Formato.entero("Cupo máximo por día", txtCupo.getText()),
                    cmbDificultad.getValue(), cmbEstado.getValue());

            // El repositorio reemplazaría en silencio una actividad existente y los paquetes
            // seguirían apuntando a la anterior; por eso se impide aquí.
            boolean existe = ctx.repo().productos().stream()
                    .anyMatch(p -> p.getCodigo().equals(actividad.getCodigo()));
            if (existe) throw new DominioException("Ya existe un producto con el código " + actividad.getCodigo() + ".");

            ctx.repo().guardar(actividad);
            ctx.refrescarActividades();
            ctx.informar("Actividad creada: " + actividad.getNombre());
            limpiar();
        } catch (DominioException e) {
            Alertas.error("No se pudo crear la actividad", e.getMessage());
        }
    }

    @FXML
    private void onCambiarEstado() {
        Actividad seleccionada = tabla.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            Alertas.advertencia("Seleccione una actividad", "Elija una actividad de la tabla.");
            return;
        }
        boolean suspender = seleccionada.getEstado() == EstadoActividad.DISPONIBLE;
        seleccionada.setEstado(suspender ? EstadoActividad.SUSPENDIDA : EstadoActividad.DISPONIBLE);
        ctx.notificarCambios();
        ctx.informar("«" + seleccionada.getNombre() + "» ahora está " + seleccionada.getEstado().toString().toLowerCase() + ".");
    }

    @FXML
    private void onLimpiar() { limpiar(); }

    private void limpiar() {
        txtCodigo.clear();
        txtNombre.clear();
        txtMunicipio.clear();
        txtDuracion.clear();
        txtPrecio.clear();
        txtCupo.clear();
        valoresPorDefecto();
    }

    private void valoresPorDefecto() {
        cmbDificultad.setValue(NivelDificultad.BAJA);
        cmbEstado.setValue(EstadoActividad.DISPONIBLE);
    }
}
