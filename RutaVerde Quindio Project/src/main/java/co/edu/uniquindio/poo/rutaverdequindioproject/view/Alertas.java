package co.edu.uniquindio.poo.rutaverdequindioproject.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/** Cuadros de diálogo estándar de la aplicación. */
public final class Alertas {
    private Alertas() { }

    public static void error(String titulo, String mensaje) { mostrar(Alert.AlertType.ERROR, titulo, mensaje); }
    public static void info(String titulo, String mensaje) { mostrar(Alert.AlertType.INFORMATION, titulo, mensaje); }
    public static void advertencia(String titulo, String mensaje) { mostrar(Alert.AlertType.WARNING, titulo, mensaje); }

    public static boolean confirmar(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.OK, ButtonType.CANCEL);
        alerta.setTitle("RutaVerde Quindío");
        alerta.setHeaderText(titulo);
        alerta.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alerta.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("RutaVerde Quindío");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alerta.showAndWait();
    }
}
