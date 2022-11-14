module com.appcontactossf {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    exports com.appcontactossf;
    opens com.loginsf to javafx.fxml;
    exports com.loginsf to javafx.fxml;
    requires commons.logging;
    requires commons.codec;
    requires httpcore;
    requires httpclient;
    requires httpmime;
    requires httpcore.nio;
    requires httpasyncclient;
}
