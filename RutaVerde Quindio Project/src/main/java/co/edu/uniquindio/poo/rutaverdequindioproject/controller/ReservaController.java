package co.edu.uniquindio.poo.rutaverdequindioproject.controller;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.Canal;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Cliente;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.DominioException;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Guia;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Producto;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Reserva;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio.ResultadoConfirmacion;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio.SolicitudReserva;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Alertas;
import co.edu.uniquindio.poo.rutaverdequindioproject.view.Formato;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Pantalla "Nueva reserva" (asesor / kiosco / app móvil).
 * Solo recoge datos y delega todo en la fachada ProcesoConfirmacion: no repite reglas de negocio.
 */
public class ReservaController {
    private final AppContext ctx;
    private final FilteredList<Guia> guiasCompatibles;

    @FXML private ComboBox<Canal> cmbCanal;
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<Producto> cmbProducto;
    @FXML private ComboBox<Guia> cmbGuia;
    @FXML private DatePicker dpFecha;
    @FXML private Spinner<Integer> spnViajeros;
    @FXML private CheckBox chkGuia, chkTransporte, chkSeguro;
    @FXML private Label lblGuiaInfo, lblTotal, lblDetalle;
    @FXML private TextArea txtObservaciones, txtVoucher;
    @FXML private TextField txtTarjeta;

    public ReservaController(AppContext ctx) {
        this.ctx = ctx;
        this.guiasCompatibles = new FilteredList<>(ctx.guias());
    }

    @FXML
    private void initialize() {
        cmbCanal.setItems(FXCollections.observableArrayList(Canal.values()));
        cmbCanal.setValue(Canal.ASESOR);
        cmbCliente.setItems(ctx.clientes());
        cmbProducto.setItems(ctx.productos());
        cmbGuia.setItems(guiasCompatibles);
        cmbGuia.disableProperty().bind(chkGuia.selectedProperty().not());
        spnViajeros.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(Reserva.MIN_VIAJEROS, Reserva.MAX_VIAJEROS, 1));
        Formato.soloFechasFuturas(dpFecha);

        // Solo se ofrecen los guías que hablan el idioma del cliente (el modelo lo valida de todos modos).
        cmbCliente.valueProperty().addListener((obs, anterior, actual) -> filtrarGuias(actual));

        escuchar(cmbCliente.valueProperty(), cmbProducto.valueProperty(), dpFecha.valueProperty(),
                spnViajeros.valueProperty(), chkGuia.selectedProperty(), cmbGuia.valueProperty(),
                chkTransporte.selectedProperty(), chkSeguro.selectedProperty());
        ctx.cambiosProperty().addListener((obs, anterior, actual) -> actualizarResumen());
        actualizarResumen();
    }

    @FXML
    private void onConfirmar() {
        Canal canal = cmbCanal.getValue();
        if (canal == null) {
            Alertas.advertencia("Seleccione el canal", "Indique desde qué canal se confirma la reserva.");
            return;
        }
        if (chkGuia.isSelected() && cmbGuia.getValue() == null) {
            Alertas.advertencia("Seleccione un guía", "Elija un guía de la lista o desmarque «Incluir».");
            return;
        }

        SolicitudReserva solicitud = new SolicitudReserva(
                cmbCliente.getValue(), cmbProducto.getValue(), dpFecha.getValue(), spnViajeros.getValue(),
                guiaElegida(), chkTransporte.isSelected(), chkSeguro.isSelected(),
                txtObservaciones.getText(), txtTarjeta.getText());

        // Un solo punto de entrada (Facade): cupos, tarjeta, pago, registro, voucher y correo.
        ResultadoConfirmacion resultado = ctx.confirmacion().confirmar(solicitud, canal);

        if (resultado.exito()) {
            txtVoucher.setText(resultado.mensaje());
            ctx.refrescarReservas();
            txtTarjeta.clear();
            int numero = resultado.reserva().getNumero();
            ctx.informar("Reserva No. " + numero + " confirmada. Voucher enviado a "
                    + resultado.reserva().getCliente().getCorreo() + ".");
            Alertas.info("Reserva confirmada", "Reserva No. " + numero + " por "
                    + Formato.moneda(resultado.reserva().valorTotal()) + ".\nEl voucher fue enviado al correo del cliente.");
        } else {
            txtVoucher.setText("RESERVA NO CONFIRMADA\n\n" + resultado.mensaje());
            ctx.refrescarReservas();   // el sistema envía un correo con el motivo del fallo
            ctx.informar("No se pudo confirmar la reserva.");
            Alertas.error("No se pudo confirmar la reserva", resultado.mensaje());
        }
    }

    @FXML
    private void onLimpiar() {
        cmbCliente.setValue(null);
        cmbProducto.setValue(null);
        dpFecha.setValue(null);
        spnViajeros.getValueFactory().setValue(1);
        chkGuia.setSelected(false);
        cmbGuia.setValue(null);
        chkTransporte.setSelected(false);
        chkSeguro.setSelected(false);
        txtObservaciones.clear();
        txtTarjeta.clear();
        txtVoucher.clear();
    }

    // ---------- apoyo ----------

    private Guia guiaElegida() { return chkGuia.isSelected() ? cmbGuia.getValue() : null; }

    private void filtrarGuias(Cliente cliente) {
        guiasCompatibles.setPredicate(g -> cliente == null || g.habla(cliente.getIdioma()));
        Guia actual = cmbGuia.getValue();
        if (actual != null && cliente != null && !actual.habla(cliente.getIdioma())) cmbGuia.setValue(null);
        if (cliente == null) lblGuiaInfo.setText("");
        else if (guiasCompatibles.isEmpty()) lblGuiaInfo.setText("Ningún guía habla " + cliente.getIdioma() + ".");
        else lblGuiaInfo.setText("Guías que hablan " + cliente.getIdioma() + ".");
    }

    private void escuchar(ObservableValue<?>... propiedades) {
        for (ObservableValue<?> propiedad : propiedades)
            propiedad.addListener((obs, anterior, actual) -> actualizarResumen());
    }

    /**
     * Calcula el valor estimado construyendo una Reserva de prueba: así se reutilizan las reglas y
     * la fórmula del modelo. Si faltan datos o alguna regla falla, se muestra el motivo como pista.
     */
    private void actualizarResumen() {
        Producto producto = cmbProducto.getValue();
        if (producto == null) {
            mostrarResumen("Valor estimado: —", "Seleccione un plan para calcular el valor.");
            return;
        }
        try {
            Reserva r = new Reserva(cmbCliente.getValue(), producto, dpFecha.getValue(), spnViajeros.getValue(),
                    guiaElegida(), chkTransporte.isSelected(), chkSeguro.isSelected(), "");
            String sinCupo = ctx.cupos().verificar(producto, r.getFechaViaje(), r.getViajeros());

            StringBuilder s = new StringBuilder();
            s.append("Plan: ").append(Formato.moneda(producto.precioPorPersona())).append(" × ")
                    .append(r.getViajeros()).append(" viajero(s)\n");
            s.append("Guía: ").append(Formato.moneda(r.costoGuia())).append('\n');
            s.append("Transporte: ").append(Formato.moneda(r.costoTransporte())).append('\n');
            s.append("Seguro: ").append(Formato.moneda(r.costoSeguro())).append('\n');
            s.append("Duración: ").append(Formato.horas(producto.duracionHoras())).append('\n');
            s.append(sinCupo == null ? "Cupo disponible para la fecha." : sinCupo);
            mostrarResumen("Valor estimado: " + Formato.moneda(r.valorTotal()), s.toString());
        } catch (DominioException e) {
            mostrarResumen("Valor estimado: —", e.getMessage());
        }
    }

    private void mostrarResumen(String total, String detalle) {
        lblTotal.setText(total);
        lblDetalle.setText(detalle);
    }
}
