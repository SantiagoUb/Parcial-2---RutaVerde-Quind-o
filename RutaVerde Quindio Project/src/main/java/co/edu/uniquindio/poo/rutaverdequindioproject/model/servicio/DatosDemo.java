package co.edu.uniquindio.poo.rutaverdequindioproject.model.servicio;

import co.edu.uniquindio.poo.rutaverdequindioproject.model.*;

import java.util.List;

/** Datos de ejemplo para demostrar la aplicación. */

public final class DatosDemo {
    private DatosDemo() { }

    public static void cargar(Repositorio repo) {
        repo.guardar(new Cliente("1094567890", "Carlos Pérez", "3001112233", "carlos@mail.com", "Colombia", "Español"));
        repo.guardar(new Cliente("US123456", "John Smith", "+1 555 0100", "john@mail.com", "Estados Unidos", "Inglés"));
        repo.guardar(new Cliente("FR778899", "Marie Dubois", "+33 1 2345 6789", "marie@mail.com", "Francia", "Francés"));

        repo.guardar(new Guia("G-01", "Ana Restrepo", List.of("Español", "Inglés"), "3105550001", 120_000));
        repo.guardar(new Guia("G-02", "Marc Laurent", List.of("Francés", "Español"), "3105550002", 130_000));
        repo.guardar(new Guia("G-03", "Lena Weber", List.of("Alemán", "Inglés"), "3105550003", 140_000));

        Actividad cocora = new Actividad("CAM01", "Caminata Valle del Cocora", "Salento", 5, 60_000, 20, NivelDificultad.MEDIA, EstadoActividad.DISPONIBLE);
        Actividad finca = new Actividad("CAF01", "Recorrido finca cafetera", "Montenegro", 4, 70_000, 15, NivelDificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad aves = new Actividad("AVI01", "Avistamiento de aves", "Salento", 4, 85_000, 10, NivelDificultad.MEDIA, EstadoActividad.DISPONIBLE);
        Actividad parqueCafe = new Actividad("PAR01", "Parque del Café", "Montenegro", 8, 120_000, 30, NivelDificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad panaca = new Actividad("PAR02", "Panaca", "Quimbaya", 8, 110_000, 30, NivelDificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad cascada = new Actividad("CAM02", "Ruta cascada Filandia", "Filandia", 6, 55_000, 12, NivelDificultad.ALTA, EstadoActividad.SUSPENDIDA);
        for (Actividad a : List.of(cocora, finca, aves, parqueCafe, panaca, cascada)) repo.guardar(a);

        Paquete salento = new Paquete("PQ01", "Paquete Salento", 10);
        salento.agregar(cocora); salento.agregar(finca);
        Paquete parques = new Paquete("PQ02", "Paquete Parques", 15);
        parques.agregar(parqueCafe); parques.agregar(panaca);
        Paquete total = new Paquete("PQ03", "Quindío Total", 5);
        total.agregar(salento); total.agregar(parques); total.agregar(aves);
        repo.guardar(salento); repo.guardar(parques); repo.guardar(total);
    }
}
