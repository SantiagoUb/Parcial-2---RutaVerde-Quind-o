package co.edu.uniquindio.poo.rutaverdequindioproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/** Ventana principal: encabezado con el nombre de la agencia y barra de estado. */
public class PrincipalController {
    private final AppContext ctx;

    @FXML private Label lblNombreAgencia, lblEstado;

    public PrincipalController(AppContext ctx) { this.ctx = ctx; }

    @FXML
    private void initialize() {
        lblNombreAgencia.textProperty().bind(ctx.nombreAgenciaProperty());
        lblEstado.textProperty().bind(ctx.mensajeProperty());
    }
}
