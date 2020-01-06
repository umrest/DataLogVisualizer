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
    private Button updateButton = new Button("Update Chart");
    private ComboBox<String> motorSelector = new ComboBox<String>();
    private LineChart VBUSGraph;
    private LineChart currentDrawGraph;
    private LineChart encoderPositionGraph;
    private LineChart encoderVelocityGraph;
    private Scene scene;


    //Data Fields
    private ArrayList<Motor> motorList;
    private Motor currentMotor;


    public MotorAnalyzer(ArrayList<Motor> motorList) {
        this.motorList = motorList;

        for (Motor motor : motorList)
            if (motor.isActive()) motorSelector.getItems().add(COMBO_BOX_MOTOR_NAME_PREFIX + motor.getCAN_ID());


        optionsLayout.getChildren().addAll(backButton, updateButton, motorSelector);
        optionsLayout.setAlignment(Pos.TOP_CENTER);
        optionsLayout.setSpacing(Main.SPACING);
        optionsLayout.setPadding(new Insets(Main.SPACING));

        root.getChildren().add(optionsLayout);

        newMotorSelected(motorSelector.getItems().get(0));

        //Create and add the chart
        VBUSGraph = generateGraph(VBUS);




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

        VBUSGraph = generateGraph(VBUS);
        root.getChildren().add(VBUSGraph);

    }

    private LineChart generateGraph(String graphTitle) {

        ArrayList<Float> graphData = new ArrayList();

        switch (graphTitle) {
            case VBUS:
                graphData = currentMotor.getPercentVBusList();
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
