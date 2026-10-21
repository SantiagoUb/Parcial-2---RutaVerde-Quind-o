package co.edu.uniquindio.poo.rutaverdequindioproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.stream.Collectors;

/** Muestra los correos que el NotificadorSimulado "envió" a los clientes. */
public class CorreoController {
    private final AppContext ctx;

    @FXML private ListView<String> lista;
    @FXML private TextArea txtDetalle;

    public CorreoController(AppContext ctx) { this.ctx = ctx; }

    @FXML
    private void initialize() {
        lista.setItems(ctx.correos());
        // En la lista solo se ven las dos primeras líneas: "Para" y "Asunto".
        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String mensaje, boolean vacio) {
                super.updateItem(mensaje, vacio);
                setText(vacio || mensaje == null ? null : mensaje.lines().limit(2).collect(Collectors.joining("\n")));
            }
        });
        lista.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, actual) -> txtDetalle.setText(actual == null ? "" : actual));
    }
}
