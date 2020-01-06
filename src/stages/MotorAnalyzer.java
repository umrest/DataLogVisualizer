package stages;

import dataObjects.Motor;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MotorAnalyzer extends Stage {

    public static final String VBUS = "Percent VBUS";
    public static final String CURRENT_DRAW = "Current Draw";
    public static final String ENCODER_POS = "Encoder Position";
    public static final String ENCODER_VEL = "Encoder Velocity";

    public static final String COMBO_BOX_MOTOR_NAME_PREFIX = "Motor ID: ";

    private static final int NUM_AXES_TICKS_X = 20;
    private static final int NUM_AXES_TICKS_Y = 15;

    //GUI Fields
    private HBox root = new HBox();
    private VBox optionsLayout = new VBox();
    private Button backButton = new Button("Back");
    private ComboBox<String> motorSelector = new ComboBox<String>();

    //Graphs
    private GridPane graphGridPane = new GridPane();
    private LineChart VBUSGraph;
    private LineChart currentDrawGraph;
    private LineChart encoderPositionGraph;
    private LineChart encoderVelocityGraph;

    //Other
    private Scene scene;


    //Data Fields
    private ArrayList<Motor> motorList;
    private Motor currentMotor;


    public MotorAnalyzer(ArrayList<Motor> motorList) {
        this.motorList = motorList;

        for (Motor motor : motorList)
            if (motor.isActive()) motorSelector.getItems().add(COMBO_BOX_MOTOR_NAME_PREFIX + motor.getCAN_ID());

        motorSelector.getSelectionModel().selectFirst();

        motorSelector.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                newMotorSelected(motorSelector.getSelectionModel().getSelectedItem());
            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                hide();

                FileExplorerPopup popup = new FileExplorerPopup();
            }
        });


        optionsLayout.getChildren().addAll(backButton, motorSelector);
        optionsLayout.setAlignment(Pos.TOP_CENTER);
        optionsLayout.setSpacing(Main.SPACING);
        optionsLayout.setPadding(new Insets(Main.SPACING));

        root.getChildren().add(optionsLayout);


        newMotorSelected(motorSelector.getItems().get(0));


        root.getChildren().add(graphGridPane);

        //Scene cleanup
        root.setPadding(new Insets(1.5 * Main.SPACING));
        scene = new Scene(root);
        setScene(scene);
        sizeToScene();
        setResizable(false);
        show();
    }

    private void newMotorSelected(String motorName) {
        motorName = motorName.substring(COMBO_BOX_MOTOR_NAME_PREFIX.length());
        int CAN_ID = Integer.valueOf(motorName);
        currentMotor = motorList.get(CAN_ID);
        graphGridPane.getChildren().removeAll(VBUSGraph, currentDrawGraph, encoderPositionGraph, encoderVelocityGraph);


        //Create and add the chart
        VBUSGraph = generateGraph(VBUS);
        currentDrawGraph = generateGraph(CURRENT_DRAW);
        encoderPositionGraph = generateGraph(ENCODER_POS);
        encoderVelocityGraph = generateGraph(ENCODER_VEL);

        graphGridPane.addRow(0, VBUSGraph, currentDrawGraph);
        graphGridPane.addRow(1, encoderPositionGraph, encoderVelocityGraph);

    }

    private LineChart generateGraph(String graphTitle) {

        ArrayList<Float> graphData = new ArrayList();

        switch (graphTitle) {
            case VBUS:
                graphData = currentMotor.getPercentVBusList();
                break;
            case CURRENT_DRAW:
                graphData = currentMotor.getCurrentDrawList();
                break;
            case ENCODER_POS:
                graphData = currentMotor.getEncoderPositionList();
                break;
            case ENCODER_VEL:
                graphData = currentMotor.getEncoderVelocityList();
                break;
        }


        XYChart.Series series = new XYChart.Series();

        NumberAxis xAxis = new NumberAxis(0, graphData.size(), NUM_AXES_TICKS_X);
        xAxis.setLabel("Entries");

        float minVal = graphData.get(0);
        float maxVal = minVal;

        series.setName("Data for Motor ID: " + currentMotor.getCAN_ID());
        series.getData().add(new XYChart.Data(0, minVal));


        for (int i = 1; i < motorList.size(); i++) {
            float val = graphData.get(i);

            minVal = (val < minVal) ? val : minVal;
            maxVal = (val > maxVal) ? val : maxVal;

            series.getData().add(new XYChart.Data(i, val));
        }

        NumberAxis yAxis = new NumberAxis(minVal, maxVal, NUM_AXES_TICKS_Y);


        LineChart graph = new LineChart(xAxis, yAxis);


        // Preparing and loading the data

        graph.getData().addAll(series);
        graph.setCreateSymbols(false);
        return graph;
    }


}
