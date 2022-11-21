module com.appcontactossf {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    exports com.appcontactossf;
    exports com.login to javafx.fxml;
    opens com.login to javafx.fxml;
    requires commons.logging;
    requires commons.codec;
    requires httpcore;
    requires httpclient;
    requires httpmime;
    requires httpcore.nio;
    requires httpasyncclient;
    requires com.google.gson;
    requires json;
}
