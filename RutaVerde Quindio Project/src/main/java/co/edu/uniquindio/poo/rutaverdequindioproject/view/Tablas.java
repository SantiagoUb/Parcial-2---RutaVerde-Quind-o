package co.edu.uniquindio.poo.rutaverdequindioproject.view;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.function.Function;

/** Ayudas para configurar tablas sin usar reflexión (PropertyValueFactory). */
public final class Tablas {
    private Tablas() { }

    /** Muestra en la columna el texto que devuelva la función para cada fila. */
    public static <S> void texto(TableColumn<S, String> columna, Function<S, String> extractor) {
        columna.setCellValueFactory(celda -> new ReadOnlyStringWrapper(extractor.apply(celda.getValue())));
    }

    /** Las columnas se reparten todo el ancho disponible de la tabla. */
    @SuppressWarnings("deprecation")
    public static <S> void ajustar(TableView<S> tabla) {
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
}
