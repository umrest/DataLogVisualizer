package sample;

import dataObjects.Motor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DataAnalyzer extends Stage {

    private TreeMap<Integer, ArrayList<Motor>> motorMap = new TreeMap<Integer, ArrayList<Motor>>();

    private VBox root = new VBox();
    private Scene scene;

    private Text titleLabel = new Text("Motor List");
    private ListView<String> listView = new ListView<>();

    public DataAnalyzer(File selectedFile) {
        loadFromFile(selectedFile);


        for (Integer keys : motorMap.keySet()) {
            listView.getItems().add("Motor ID: " + keys.toString());
        }

        double cellHeight = Font.getDefault().getSize() * 2.5;
        listView.setFixedCellSize(cellHeight);

        listView.setMaxHeight(15 * cellHeight);
        listView.setPrefHeight(motorMap.size() * cellHeight);



        root.getChildren().addAll(titleLabel, listView);


        root.setPadding(new Insets(Main.SPACING, 2 * Main.SPACING, 1.25 * Main.SPACING, 2 * Main.SPACING));
        scene = new Scene(root);

        setScene(scene);
        sizeToScene();
        show();

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
