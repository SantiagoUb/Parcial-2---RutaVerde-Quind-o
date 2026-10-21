package co.edu.uniquindio.poo.rutaverdequindioproject.controller;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.DominioException;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Reserva;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio.ServicioReportes;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Alertas;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Formato;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Tablas;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Historial de reservas confirmadas y reportes gerenciales. */
public class ReporteController {
    private final AppContext ctx;

    @FXML private DatePicker dpDesde, dpHasta;
    @FXML private Label lblIngresos, lblTop;
    @FXML private TableView<Reserva> tabla;
    @FXML private TableColumn<Reserva, String> colNumero, colCliente, colProducto, colFecha, colViajeros,
            colGuia, colExtras, colTotal;

    public ReporteController(AppContext ctx) { this.ctx = ctx; }

    @FXML
    private void initialize() {
        dpDesde.setValue(LocalDate.now());
        dpHasta.setValue(LocalDate.now().plusMonths(3));

        Tablas.texto(colNumero, r -> String.valueOf(r.getNumero()));
        Tablas.texto(colCliente, r -> r.getCliente().getNombre());
        Tablas.texto(colProducto, r -> r.getProducto().getNombre());
        Tablas.texto(colFecha, r -> r.getFechaViaje().toString());
        Tablas.texto(colViajeros, r -> String.valueOf(r.getViajeros()));
        Tablas.texto(colGuia, r -> r.getGuia() == null ? "—" : r.getGuia().getNombre());
        Tablas.texto(colExtras, ReporteController::servicios);
        Tablas.texto(colTotal, r -> Formato.moneda(r.valorTotal()));
        Tablas.ajustar(tabla);
        tabla.setItems(ctx.reservas());

        lblTop.setText("");
        lblIngresos.setText("");
    }

    @FXML
    private void onIngresos() {
        try {
            long total = ctx.reportes().ingresos(dpDesde.getValue(), dpHasta.getValue());
            lblIngresos.setText(Formato.moneda(total));
            ctx.informar("Ingresos calculados para el rango seleccionado.");
        } catch (DominioException e) {
            lblIngresos.setText("");
            Alertas.error("No se pudo calcular el ingreso", e.getMessage());
        }
    }

    @FXML
    private void onProductoTop() {
        ServicioReportes.ProductoTop top = ctx.reportes().productoConMasViajeros();
        lblTop.setText(top == null
                ? "Aún no hay reservas confirmadas."
                : top.producto().getNombre() + " — " + top.viajeros() + " viajeros acumulados");
    }

    private static String servicios(Reserva r) {
        List<String> extras = new ArrayList<>();
        if (r.getGuia() != null) extras.add("Guía");
        if (r.isTransporte()) extras.add("Transporte");
        if (r.isSeguro()) extras.add("Seguro");
        return extras.isEmpty() ? "—" : String.join(", ", extras);
    }
}
