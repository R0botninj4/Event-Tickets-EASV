module dk.easv.event_tickets_easv_bar {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;
    requires com.microsoft.sqlserver.jdbc;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;
    requires de.mkammerer.argon2.nolibs;

    opens dk.easv.event_tickets_easv_bar.GUI to javafx.fxml;

    exports dk.easv.event_tickets_easv_bar.GUI;
    exports dk.easv.event_tickets_easv_bar.BE;
    exports dk.easv.event_tickets_easv_bar.BLL;
    exports dk.easv.event_tickets_easv_bar.DAL;
    exports dk.easv.event_tickets_easv_bar.DAL.DB;
    exports dk.easv.event_tickets_easv_bar.DAL.Interface;

    exports dk.easv.event_tickets_easv_bar.GUI.Login;
    opens dk.easv.event_tickets_easv_bar.GUI.Login to javafx.fxml;
    exports dk.easv.event_tickets_easv_bar.GUI.Interface;
    opens dk.easv.event_tickets_easv_bar.GUI.Interface to javafx.fxml;
}
