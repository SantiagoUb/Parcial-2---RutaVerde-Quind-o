module co.edu.uniquindio.poo.rutaverdequindioproject {
    requires javafx.controls;
    requires javafx.fxml;

    // FXMLLoader necesita reflexión sobre los controladores (campos @FXML y métodos onAction).
    opens co.edu.uniquindio.poo.rutaverdequindioproject.controller to javafx.fxml;
    exports co.edu.uniquindio.poo.rutaverdequindioproject;
}
