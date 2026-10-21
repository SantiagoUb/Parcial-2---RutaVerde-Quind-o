package co.edu.uniquindio.poo.rutaverdequindioproject.controller;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Agencia;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Alertas;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/** Edición de los datos de la agencia (Singleton del modelo). */
public class AgenciaController {
    private final AppContext ctx;

    @FXML private TextField txtNombre, txtNit, txtRnt, txtDireccion, txtTelefono, txtCorreo, txtWeb;

    public AgenciaController(AppContext ctx) { this.ctx = ctx; }

    @FXML
    private void initialize() { cargar(); }

    @FXML
    private void onGuardar() {
        if (txtNombre.getText() == null || txtNombre.getText().isBlank()) {
            Alertas.error("No se pudo guardar", "El nombre comercial es obligatorio.");
            return;
        }
        Agencia.getInstancia().actualizar(txtNombre.getText().trim(), txtNit.getText().trim(),
                txtRnt.getText().trim(), txtDireccion.getText().trim(), txtTelefono.getText().trim(),
                txtCorreo.getText().trim(), txtWeb.getText().trim());
        ctx.nombreAgenciaProperty().set(Agencia.getInstancia().getNombreComercial());
        ctx.informar("Datos de la agencia actualizados.");
    }

    @FXML
    private void onDeshacer() { cargar(); }

    private void cargar() {
        Agencia a = Agencia.getInstancia();
        txtNombre.setText(a.getNombreComercial());
        txtNit.setText(a.getNit());
        txtRnt.setText(a.getRnt());
        txtDireccion.setText(a.getDireccion());
        txtTelefono.setText(a.getTelefono());
        txtCorreo.setText(a.getCorreo());
        txtWeb.setText(a.getPaginaWeb());
    }
}
