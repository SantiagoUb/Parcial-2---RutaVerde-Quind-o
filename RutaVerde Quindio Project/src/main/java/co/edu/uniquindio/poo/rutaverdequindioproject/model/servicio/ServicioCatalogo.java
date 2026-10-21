package co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.DominioException;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Paquete;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Producto;
import co.edu.uniquindio.poo.rutaverdequindioproject.model.Repositorio;

import java.util.Set;
import java.util.stream.Collectors;

public class ServicioCatalogo {
    private final Repositorio repo;
    public ServicioCatalogo(Repositorio repo) { this.repo = repo; }

    /**
     * Agrega un componente a un paquete. Además de las reglas del propio paquete, revisa los paquetes
     * que ya lo contienen, para que el plan final tampoco repita actividades (RN-09).
     */
    public void agregarComponente(Paquete destino, Producto nuevo) {
        if (destino == null) throw new DominioException("Seleccione un paquete.");
        if (nuevo != null && (nuevo == destino || nuevo.incluye(destino)))
            throw new DominioException("Un paquete no puede contenerse a sí mismo, ni directa ni indirectamente.");
        if (nuevo != null) {
            Set<String> nuevas = nuevo.actividades().stream().map(a -> a.getCodigo()).collect(Collectors.toSet());
            for (Paquete otro : repo.paquetes())
                if (otro != destino && otro.incluye(destino))
                    for (var a : otro.actividades())
                        if (nuevas.contains(a.getCodigo()))
                            throw new DominioException("«" + a.getNombre() + "» ya está en el plan «" + otro.getNombre() + "», que contiene este paquete.");
        }
        destino.agregar(nuevo);
    }
}
