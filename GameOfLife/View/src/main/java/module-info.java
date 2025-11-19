module pl.lodz.p.view {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.lodz.p.view to javafx.fxml;
    exports pl.lodz.p.view;
}