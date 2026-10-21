package co.edu.uniquindio.poo.rutaverdequindioproject.model.pago;

/** Algoritmo de Luhn. */

public final class ValidadorTarjeta {
    private ValidadorTarjeta() { }

    public static boolean esValida(String numero) {
        if (numero == null) return false;
        String limpio = numero.replace(" ", "");
        if (limpio.length() < 13 || limpio.length() > 19) return false;
        int suma = 0;
        boolean duplicar = false;                       // el último dígito no se duplica
        for (int i = limpio.length() - 1; i >= 0; i--) {
            char c = limpio.charAt(i);
            if (c < '0' || c > '9') return false;
            int d = c - '0';
            if (duplicar) { d *= 2; if (d > 9) d -= 9; }
            suma += d;
            duplicar = !duplicar;
        }
        return suma % 10 == 0;
    }
}
