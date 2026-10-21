package co.edu.uniquindio.poo.rutaverdequindioproject.controller;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Actividad;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.DominioException;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.EstadoActividad;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Paquete;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Producto;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Alertas;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Formato;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Tablas;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/** Gestión de paquetes (Composite): creación y armado de la composición, visible como árbol. */
public class PaqueteController {
    private final AppContext ctx;

    @FXML private TextField txtCodigo, txtNombre, txtDescuento;
    @FXML private TableView<Paquete> tablaPaquetes;
    @FXML private TableColumn<Paquete, String> colCodigo, colNombre, colDescuento, colPrecio, colDuracion;
    @FXML private TreeView<Producto> arbol;
    @FXML private ComboBox<Producto> cmbComponente;
    @FXML private Label lblResumen;

    public PaqueteController(AppContext ctx) { this.ctx = ctx; }

    @FXML
    private void initialize() {
        Tablas.texto(colCodigo, Paquete::getCodigo);
        Tablas.texto(colNombre, Paquete::getNombre);
        Tablas.texto(colDescuento, p -> Formato.numero(p.getPorcentajeDescuento()) + " %");
        Tablas.texto(colPrecio, p -> Formato.moneda(p.precioPorPersona()));
        Tablas.texto(colDuracion, p -> Formato.horas(p.duracionHoras()));
        Tablas.ajustar(tablaPaquetes);
        tablaPaquetes.setItems(ctx.paquetes());
        cmbComponente.setItems(ctx.productos());

        arbol.setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(Producto p, boolean vacio) {
                super.updateItem(p, vacio);
                setText(vacio || p == null ? null : describir(p));
            }
        });

        tablaPaquetes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> reconstruirArbol());
        ctx.cambiosProperty().addListener((obs, anterior, actual) -> {
            tablaPaquetes.refresh();
            reconstruirArbol();
        });
        reconstruirArbol();
    }

    @FXML
    private void onCrear() {
        try {
            Paquete paquete = new Paquete(txtCodigo.getText(), txtNombre.getText(),
                    Formato.decimal("Descuento", txtDescuento.getText()));
            ctx.repo().guardar(paquete);
            ctx.refrescarPaquetes();
            tablaPaquetes.getSelectionModel().select(paquete);
            ctx.informar("Paquete creado: " + paquete.getNombre() + ". Agréguele actividades o paquetes.");
            txtCodigo.clear();
            txtNombre.clear();
            txtDescuento.setText("0");
        } catch (DominioException e) {
            Alertas.error("No se pudo crear el paquete", e.getMessage());
        }
    }

    @FXML
    private void onAgregar() {
        Paquete destino = tablaPaquetes.getSelectionModel().getSelectedItem();
        Producto nuevo = cmbComponente.getValue();
        try {
            // El servicio aplica las reglas: sin ciclos y sin actividades repetidas (también en paquetes padres).
            ctx.catalogo().agregarComponente(destino, nuevo);
            ctx.notificarCambios();
            ctx.informar("«" + nuevo.getNombre() + "» agregado a «" + destino.getNombre() + "».");
        } catch (DominioException e) {
            Alertas.error("No se pudo agregar el componente", e.getMessage());
        }
    }

    private void reconstruirArbol() {
        Paquete seleccionado = tablaPaquetes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            arbol.setRoot(null);
            lblResumen.setText("Seleccione un paquete para ver su composición.");
            return;
        }
        arbol.setRoot(construir(seleccionado));
        lblResumen.setText(seleccionado.actividades().size() + " actividad(es) · "
                + Formato.moneda(seleccionado.precioPorPersona()) + " por persona · "
                + Formato.horas(seleccionado.duracionHoras()) + " en total");
    }

    private TreeItem<Producto> construir(Producto producto) {
        TreeItem<Producto> nodo = new TreeItem<>(producto);
        nodo.setExpanded(true);
        if (producto instanceof Paquete paquete)
            for (Producto hijo : paquete.getHijos()) nodo.getChildren().add(construir(hijo));
        return nodo;
    }

    private static String describir(Producto producto) {
        if (producto instanceof Paquete p) {
            return "Paquete · " + p.getNombre() + " (" + Formato.numero(p.getPorcentajeDescuento()) + " % desc.) · "
                    + Formato.moneda(p.precioPorPersona()) + " p/p";
        }
        if (producto instanceof Actividad a) {
            return "Actividad · " + a.getNombre() + " (" + a.getMunicipio() + ") · "
                    + Formato.moneda(a.precioPorPersona()) + " · " + Formato.horas(a.duracionHoras())
                    + (a.getEstado() == EstadoActividad.SUSPENDIDA ? " · SUSPENDIDA" : "");
        }
        return producto.getNombre();
    }
}
