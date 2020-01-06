package stages;

import dataObjects.Motor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
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


    private static final String TITLE = "Select a \"" + Main.DATA_FILE_EXT + "\" file to load";
    private static final String NO_FILE_CHOSEN = "No File Chosen";
    private VBox root = new VBox();
    private HBox fileChooserLayout = new HBox();
    private Text titleLabel = new Text(TITLE);
    private Text chosenFileLabel = new Text(NO_FILE_CHOSEN);
    private Button fileChooserButton = new Button("Choose File:");
    private Button confirmButton = new Button("Analyze File");

    private File selectedFile = new File("C:\\Users\\bclaw\\IdeaProjects\\REST_DataLogVisualizer\\TestFileGenerator\\TestFileGenerator\\exampleData.REST_DATA");


    public FileExplorerPopup() {

        //Load Fonts
        Font normalFont = Main.APP_FONTS.NORMAL_FONT;
        Font titleFont = Main.APP_FONTS.TITLE_FONT;

        //Setup Components
        titleLabel.setFont(titleFont);

        chosenFileLabel.setFont(normalFont);
        fileChooserButton.setFont(normalFont);
        confirmButton.setFont(normalFont);


        //HHHHHHHHHHHHHHHHHHHHHHHHHHEEEEEEEEEEEEEEEEEEEERRRRRRRRRRRRRRRRRRRRRRREEEEEEEEEEEEEEEEEE
        //confirmButton.setDisable(true); // Also remove the default file from the global field File file

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
        root.setSpacing(1.5 * Main.SPACING);
        root.setPadding(new Insets(Main.SPACING, 2 * Main.SPACING, 1.25 * Main.SPACING, 2 * Main.SPACING));
        root.setAlignment(Pos.CENTER);

        fileChooserLayout.setAlignment(Pos.CENTER);
        fileChooserLayout.setSpacing(2 * Main.SPACING);
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

        String filePath = selectedFile.getPath();
    }

    private void confirmAction(File selectedFile) {
        hide();
        ArrayList<Motor> motorList = loadFromFile(selectedFile);
        MotorAnalyzer motorAnalyzer = new MotorAnalyzer(motorList);
    }

    private ArrayList<Motor> loadFromFile(File selectedFile) {

        ArrayList<Motor> motorDataList = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) motorDataList.add(new Motor(i));

        try {
            Scanner scanner = new Scanner(selectedFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] items = line.split(",");

                switch (items[0].charAt(0)) {
                    case '#':
                        //Comment line, ignore
                        break;

                    case Motor.CHARACTER_ID:
                        int CAN_ID = Integer.valueOf(items[1]);
                        float percentVBus = Float.valueOf(items[2]);
                        float currentDraw = Float.valueOf(items[3]);
                        float encoderPosition = Float.valueOf(items[4]);
                        float encoderVelocity = Float.valueOf(items[5]);

                        motorDataList.get(CAN_ID).addData(percentVBus, currentDraw, encoderPosition, encoderVelocity);
                        break;

                    default:
                        System.err.println("Character identifier (" + items[0].charAt(0) + ") not recognized!");
                }

            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return motorDataList;
    }


}
