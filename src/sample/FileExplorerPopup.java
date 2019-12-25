package sample;

import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileExplorerPopup extends Stage {


    private VBox root = new VBox();
    private HBox fileChooserLayout = new HBox();

    private static final String TITLE = "Select a \"" + Main.DATA_FILE_EXT + "\" file to load";
    private Font titleFont = Font.font(18);
    private Text titleLabel = new Text(TITLE);

    private Font normalFont = Font.font(14);
    private static final String NO_FILE_CHOSEN = "No File Chosen";
    private Text chosenFileLabel = new Text(NO_FILE_CHOSEN);
    private Button fileChooserButton = new Button("Choose File:");
    private Button confirmButton = new Button("Analyze File");

    private final int SPACING = 10;


    private File selectedFile = null;


    public FileExplorerPopup(){

        //Setup Components
        titleLabel.setFont(titleFont);

        chosenFileLabel.setFont(normalFont);
        fileChooserButton.setFont(normalFont);
        confirmButton.setFont(normalFont);
        confirmButton.setDisable(true);

        //ActionEvents
        fileChooserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                fileChooserAction();
            }
        });
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                confirmAction(selectedFile);
            }
        });


        //Setup Layouts
        root.setSpacing(1.5 * SPACING);
        root.setPadding(new Insets(SPACING));
        root.setAlignment(Pos.CENTER);

        fileChooserLayout.setAlignment(Pos.CENTER);
        fileChooserLayout.setSpacing(2 * SPACING);
        fileChooserLayout.getChildren().addAll(fileChooserButton, chosenFileLabel);
        root.getChildren().addAll(titleLabel, fileChooserLayout, confirmButton);

        Scene scene = new Scene(root);

        setScene(scene);
        sizeToScene();
        show();

    }

    private void fileChooserAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a data file:");

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("REST Data File (." + Main.DATA_FILE_EXT + ")", "*." + Main.DATA_FILE_EXT);
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);

        File selectedFile = fileChooser.showOpenDialog(this);
        if (selectedFile != null) {
            this.selectedFile = selectedFile;
            confirmButton.setDisable(false);
            chosenFileLabel.setText(selectedFile.getName());
            sizeToScene();
        }
    }

    private void confirmAction(File selectedFile) {
        hide();

        DataAnalyzer dataAnalyzer = new DataAnalyzer(selectedFile);
    }


}
