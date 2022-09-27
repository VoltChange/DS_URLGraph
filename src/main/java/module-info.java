module org.volt.urlgraph {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires JavaFXSmartGraph;

    opens org.volt.urlgraph to javafx.fxml;
    exports org.volt.urlgraph;
}
