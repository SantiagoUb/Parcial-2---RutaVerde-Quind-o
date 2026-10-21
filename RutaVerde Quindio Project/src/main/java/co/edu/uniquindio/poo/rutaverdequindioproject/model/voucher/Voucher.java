package co.edu.uniquindio.poo.rutaverdequindioproject.model.voucher;

/** Producto final del Builder. */
public class Voucher {
    private final StringBuilder texto = new StringBuilder();
    void agregar(String parte) { texto.append(parte); }
    public String getTexto() { return texto.toString(); }
    @Override public String toString() { return getTexto(); }
}

