package stages;

import dataObjects.Motor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MotorSelector extends Stage {

    private TreeMap<Integer, ArrayList<Motor>> motorMap = new TreeMap<Integer, ArrayList<Motor>>();

    private VBox root = new VBox();
    private Scene scene;

    private Text titleLabel = new Text("Motor List  -  ");
    private ListView<String> listView = new ListView<>();

    public MotorSelector(File selectedFile) {
        loadFromFile(selectedFile);

        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(Main.SPACING);

        titleLabel.setFont(Main.APP_FONTS.TITLE_FONT);


        for (Integer keys : motorMap.keySet()) {
            listView.getItems().add("Motor ID: " + keys.toString());
        }

        titleLabel.setText(titleLabel.getText() + listView.getItems().size() + " Motors");

        double cellHeight = Font.getDefault().getSize() * 2.25;
        listView.setFixedCellSize(cellHeight);

        listView.setMaxHeight(15 * cellHeight);
        listView.setPrefHeight((motorMap.size() * cellHeight) + 2);


        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                itemSelected();
            }
        });

        listView.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) itemSelected();

            }
        });


        root.getChildren().addAll(titleLabel, listView);


        root.setPadding(new Insets(Main.SPACING, 2 * Main.SPACING, 1.25 * Main.SPACING, 2 * Main.SPACING));
        scene = new Scene(root);

        setScene(scene);
        sizeToScene();
        show();

    }

    private void itemSelected() {
        hide();

      String items[] = listView.getSelectionModel().getSelectedItem().split(" ");
      int key = Integer.valueOf(items[items.length - 1]);
      new MotorAnalyzer(motorMap.get(key));
    }

    private void loadFromFile(File selectedFile) {
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
                        createMotorObject(items);
                        break;

                    default:
                        System.err.println("Character identifier (" + items[0].charAt(0) + ") not recognized!");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void createMotorObject(String[] items) {

        int CAN_ID = Integer.valueOf(items[1]);
        float percentVBus = Float.valueOf(items[2]);
        float currentDraw = Float.valueOf(items[3]);
        float encoderPosition = Float.valueOf(items[4]);
        float encoderVelocity = Float.valueOf(items[5]);

        Motor motor = new Motor(CAN_ID, percentVBus, currentDraw, encoderPosition, encoderVelocity);

        if (!motorMap.containsKey(CAN_ID)) motorMap.put(CAN_ID, new ArrayList<Motor>());
        motorMap.get(CAN_ID).add(motor);
    }

}
