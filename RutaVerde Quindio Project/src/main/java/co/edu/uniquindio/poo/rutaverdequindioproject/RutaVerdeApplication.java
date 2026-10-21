package co.edu.uniquindio.poo.rutaverdequindioproject;

import co.edu.uniquindio.poo.rutaverdequindioproject.controller.AppContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada. Crea el contexto del modelo una sola vez y lo inyecta en todos los
 * controladores mediante un controllerFactory (las vistas FXML no conocen el modelo).
 */
public class RutaVerdeApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        AppContext ctx = new AppContext();

        FXMLLoader loader = new FXMLLoader(RutaVerdeApplication.class.getResource("view/principal-view.fxml"));
        loader.setControllerFactory(clase -> crearControlador(clase, ctx));

        Scene scene = new Scene(loader.load(), 1180, 760);
        scene.getStylesheets().add(RutaVerdeApplication.class.getResource("view/estilo.css").toExternalForm());

        stage.setTitle("RutaVerde Quindío - Gestión de reservas");
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.show();
    }

    private static Object crearControlador(Class<?> clase, AppContext ctx) {
        try {
            return clase.getConstructor(AppContext.class).newInstance(ctx);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No se pudo crear el controlador " + clase.getSimpleName(), e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
