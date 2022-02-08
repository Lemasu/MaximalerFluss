module ch.kbw.maximalerfluss {
	requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
	requires transitive javafx.fxml;
	requires json.simple;

    opens ch.kbw.maximalerfluss to javafx.fxml;
    exports ch.kbw.maximalerfluss;
}