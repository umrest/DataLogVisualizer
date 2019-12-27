package stages;

import dataObjects.Motor;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MotorAnalyzer extends Stage {

    HBox root = new HBox();
    VBox optionsLayout = new VBox();

    Button backButton = new Button("Back");
    Button updateButton = new Button("Update Chart");

    ComboBox<String> chartSelector = new ComboBox<String>();


    Scene scene;

    private ArrayList<Motor> data;
    private LineChart graph;

    public MotorAnalyzer(ArrayList<Motor> data) {
        this.data = data;

        chartSelector.getItems().addAll(
                "Percentage (VBUS)",
                "Current",
                "GRAPH EVERYTHING INSTEAD");

        optionsLayout.getChildren().addAll(backButton, updateButton, chartSelector);
        optionsLayout.setAlignment(Pos.TOP_CENTER);
        optionsLayout.setSpacing(Main.SPACING);
        optionsLayout.setPadding(new Insets(Main.SPACING));

        root.getChildren().add(optionsLayout);


        //Create and add the chart
        generateGraph();
        root.getChildren().add(graph);


        //Scene cleanup
        scene = new Scene(root);
        setScene(scene);
        sizeToScene();
        setResizable(false);
        show();
    }


    private static final int NUM_AXES_TICKS_X = 20;
    private static final int NUM_AXES_TICKS_Y = 15;

    private void generateGraph() {

        XYChart.Series series = new XYChart.Series();

        NumberAxis xAxis = new NumberAxis(0, data.size(), NUM_AXES_TICKS_X);
        xAxis.setLabel("Entries");

        float minVal = data.get(0).getCurrentDraw();
        float maxVal = minVal;

        series.setName("Data for Motor ID: " + data.get(0).getCAN_ID());
        series.getData().add(new XYChart.Data(0, minVal));


        for (int i = 1; i < data.size(); i++) {
            float val = data.get(i).getCurrentDraw();

            minVal = (val < minVal) ? val : minVal;
            maxVal = (val > maxVal) ? val : maxVal;

            series.getData().add(new XYChart.Data(i, val));
        }

        NumberAxis yAxis = new NumberAxis(minVal, maxVal, NUM_AXES_TICKS_Y);


        LineChart graph = new LineChart(xAxis, yAxis);


        // Preparing and loading the data

        graph.getData().addAll(series);
        graph.setCreateSymbols(false);
        this.graph = graph;
    }


}
