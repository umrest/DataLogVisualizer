package stages;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {


    public static class APP_FONTS {
        public static Font NORMAL_FONT = Font.font(14);
        public static Font TITLE_FONT = Font.font(18);

    }

    public static final String DATA_FILE_EXT = "REST_DATA";
    public static final int SPACING = 10;


    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
      FileExplorerPopup fileExplorerPopup = new FileExplorerPopup();
    }
}
