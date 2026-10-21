package co.edu.uniquindio.poo.rutaverdequindioproject.view;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.DominioException;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/** Utilidades de presentación y lectura de campos de texto. */
public final class Formato {
    public static final List<String> IDIOMAS =
            List.of("Español", "Inglés", "Francés", "Alemán", "Italiano", "Portugués");

    private Formato() { }

    public static String moneda(double valor) {
        NumberFormat f = NumberFormat.getCurrencyInstance(Locale.of("es", "CO"));
        f.setMaximumFractionDigits(0);
        return f.format(valor);
    }

    /** 8.0 -> "8"; 2.5 -> "2.5" */
    public static String numero(double valor) {
        return valor == Math.rint(valor) ? String.valueOf((long) valor) : String.valueOf(valor);
    }

    public static String horas(double valor) { return numero(valor) + " h"; }

    /**
     * Lee un número escrito por una persona. Acepta "120000", "120.000", "$ 120.000" y "2,5".
     * Un punto seguido de exactamente tres dígitos se interpreta como separador de miles.
     */
    public static double decimal(String campo, String texto) {
        if (texto == null || texto.isBlank())
            throw new DominioException("El campo «" + campo + "» es obligatorio.");
        String t = texto.trim().replace("$", "").replace(" ", "");
        if (t.matches("\\d{1,3}(\\.\\d{3})+(,\\d+)?")) t = t.replace(".", "").replace(",", ".");
        else t = t.replace(",", ".");
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            throw new DominioException("El campo «" + campo + "» debe ser un número válido.");
        }
    }

    public static int entero(String campo, String texto) {
        double d = decimal(campo, texto);
        if (d != Math.rint(d)) throw new DominioException("El campo «" + campo + "» debe ser un número entero.");
        return (int) d;
    }

    /** Deshabilita en el calendario los días anteriores a hoy. */
    public static void soloFechasFuturas(DatePicker selector) {
        selector.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate fecha, boolean vacio) {
                super.updateItem(fecha, vacio);
                setDisable(vacio || fecha.isBefore(LocalDate.now()));
            }
        });
    }
}
