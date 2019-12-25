package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String DATA_FILE_EXT = "REST_DATA";

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
      FileExplorerPopup fileExplorerPopup = new FileExplorerPopup();
    }
}
