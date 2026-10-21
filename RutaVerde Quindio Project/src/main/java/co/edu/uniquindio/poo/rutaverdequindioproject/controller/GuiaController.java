package co.edu.uniquindio.poo.rutaverdequindioproject.controller;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.DominioException;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Guia;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Alertas;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Formato;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Tablas;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

/** Gestión de guías turísticos. */
public class GuiaController {
    private final AppContext ctx;

    @FXML private TextField txtIdentificacion, txtNombre, txtIdiomas, txtTelefono, txtTarifa;
    @FXML private TableView<Guia> tabla;
    @FXML private TableColumn<Guia, String> colIdentificacion, colNombre, colIdiomas, colTelefono, colTarifa;

    public GuiaController(AppContext ctx) { this.ctx = ctx; }

    @FXML
    private void initialize() {
        Tablas.texto(colIdentificacion, Guia::getIdentificacion);
        Tablas.texto(colNombre, Guia::getNombre);
        Tablas.texto(colIdiomas, g -> String.join(", ", g.getIdiomas()));
        Tablas.texto(colTelefono, Guia::getTelefono);
        Tablas.texto(colTarifa, g -> Formato.moneda(g.getTarifaPorDia()));
        Tablas.ajustar(tabla);
        tabla.setItems(ctx.guias());
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> mostrar(actual));
    }

    @FXML
    private void onGuardar() {
        try {
            List<String> idiomas = Arrays.stream(txtIdiomas.getText().split("[,;/]"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
            Guia guia = new Guia(txtIdentificacion.getText(), txtNombre.getText(), idiomas, txtTelefono.getText(),
                    Formato.decimal("Tarifa por día", txtTarifa.getText()));

            boolean editando = tabla.getSelectionModel().getSelectedItem() != null;
            boolean existe = ctx.repo().guias().stream()
                    .anyMatch(g -> g.getIdentificacion().equals(guia.getIdentificacion()));
            if (existe && !editando)
                throw new DominioException("Ya existe un guía con esa identificación. Selecciónelo en la tabla para editarlo.");

            ctx.repo().guardar(guia);
            ctx.refrescarGuias();
            ctx.informar("Guía " + (editando ? "actualizado: " : "registrado: ") + guia.getNombre());
            limpiar();
        } catch (DominioException e) {
            Alertas.error("No se pudo guardar el guía", e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        Guia seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alertas.advertencia("Seleccione un guía", "Elija un guía de la tabla para eliminarlo.");
            return;
        }
        if (!Alertas.confirmar("Eliminar guía", "¿Eliminar a " + seleccionado.getNombre() + "?")) return;
        ctx.repo().eliminarGuia(seleccionado.getIdentificacion());
        ctx.refrescarGuias();
        limpiar();
        ctx.informar("Guía eliminado: " + seleccionado.getNombre());
    }

    @FXML
    private void onLimpiar() { limpiar(); }

    private void mostrar(Guia g) {
        txtIdentificacion.setDisable(g != null);
        if (g == null) return;
        txtIdentificacion.setText(g.getIdentificacion());
        txtNombre.setText(g.getNombre());
        txtIdiomas.setText(String.join(", ", g.getIdiomas()));
        txtTelefono.setText(g.getTelefono());
        txtTarifa.setText(Formato.numero(g.getTarifaPorDia()));
    }

    private void limpiar() {
        tabla.getSelectionModel().clearSelection();
        txtIdentificacion.clear();
        txtNombre.clear();
        txtIdiomas.clear();
        txtTelefono.clear();
        txtTarifa.clear();
    }
}
