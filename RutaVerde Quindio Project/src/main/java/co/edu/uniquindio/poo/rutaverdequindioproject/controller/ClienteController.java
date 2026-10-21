package co.edu.uniquindio.poo.rutaverdequindioproject.controller;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Cliente;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.DominioException;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Alertas;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Formato;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Tablas;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/** Gestión de clientes: crear, editar y eliminar. */
public class ClienteController {
    private final AppContext ctx;

    @FXML private TextField txtDocumento, txtNombre, txtTelefono, txtCorreo, txtPais;
    @FXML private ComboBox<String> cmbIdioma;
    @FXML private TableView<Cliente> tabla;
    @FXML private TableColumn<Cliente, String> colDocumento, colNombre, colTelefono, colCorreo, colPais, colIdioma;

    public ClienteController(AppContext ctx) { this.ctx = ctx; }

    @FXML
    private void initialize() {
        Tablas.texto(colDocumento, Cliente::getDocumento);
        Tablas.texto(colNombre, Cliente::getNombre);
        Tablas.texto(colTelefono, Cliente::getTelefono);
        Tablas.texto(colCorreo, Cliente::getCorreo);
        Tablas.texto(colPais, Cliente::getPais);
        Tablas.texto(colIdioma, Cliente::getIdioma);
        Tablas.ajustar(tabla);
        tabla.setItems(ctx.clientes());

        cmbIdioma.setItems(FXCollections.observableArrayList(Formato.IDIOMAS));
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> mostrar(actual));
    }

    @FXML
    private void onGuardar() {
        try {
            Cliente cliente = new Cliente(txtDocumento.getText(), txtNombre.getText(), txtTelefono.getText(),
                    txtCorreo.getText(), txtPais.getText(), cmbIdioma.getEditor().getText());

            boolean editando = tabla.getSelectionModel().getSelectedItem() != null;
            boolean existe = ctx.repo().clientes().stream()
                    .anyMatch(c -> c.getDocumento().equals(cliente.getDocumento()));
            if (existe && !editando)
                throw new DominioException("Ya existe un cliente con ese documento. Selecciónelo en la tabla para editarlo.");

            ctx.repo().guardar(cliente);
            ctx.refrescarClientes();
            ctx.informar("Cliente " + (editando ? "actualizado: " : "registrado: ") + cliente.getNombre());
            limpiar();
        } catch (DominioException e) {
            Alertas.error("No se pudo guardar el cliente", e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        Cliente seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alertas.advertencia("Seleccione un cliente", "Elija un cliente de la tabla para eliminarlo.");
            return;
        }
        if (!Alertas.confirmar("Eliminar cliente", "¿Eliminar a " + seleccionado.getNombre() + "?")) return;
        try {
            ctx.repo().eliminarCliente(seleccionado.getDocumento());
            ctx.refrescarClientes();
            limpiar();
            ctx.informar("Cliente eliminado: " + seleccionado.getNombre());
        } catch (DominioException e) {
            Alertas.error("No se pudo eliminar el cliente", e.getMessage());
        }
    }

    @FXML
    private void onLimpiar() { limpiar(); }

    private void mostrar(Cliente c) {
        txtDocumento.setDisable(c != null);   // el documento es la llave: no se edita
        if (c == null) return;
        txtDocumento.setText(c.getDocumento());
        txtNombre.setText(c.getNombre());
        txtTelefono.setText(c.getTelefono());
        txtCorreo.setText(c.getCorreo());
        txtPais.setText(c.getPais());
        cmbIdioma.setValue(c.getIdioma());
    }

    private void limpiar() {
        tabla.getSelectionModel().clearSelection();
        txtDocumento.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtPais.clear();
        cmbIdioma.setValue(null);
        cmbIdioma.getEditor().clear();
    }
}
