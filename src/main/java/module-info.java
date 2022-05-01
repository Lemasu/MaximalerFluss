module ch.kbw.maximalerfluss {
	requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
	requires transitive javafx.fxml;
	requires JavaFXSmartGraph;

    opens ch.kbw.maximalerfluss.gui to javafx.fxml;
    exports ch.kbw.maximalerfluss;
}