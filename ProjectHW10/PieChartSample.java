package ProjectHW10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.chart.*;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Main class for Program 3
 * April 3, 2020
 * @author Luis Gonzalez
 */
public class PieChartSample extends Application {

    public static void main(String[] args) {
        Application.launch(PieChartSample.class, args);
    }

    /**
     * main method for Program 3
     * will call start and build the stage from that class
     * @param args unused
     */
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createBorderPane(), 650, 500);
        stage.setTitle("Wheel of Decision");
        stage.setScene(scene);
        stage.show();
    }

    // ArrayList of Data to make a copy and reload it back later
    ArrayList<Data> copyData = new ArrayList<>();

    /**
     * This method creates and organizes all the nodes in the 
     * scene.
     * @return BorderPane for the scene to
     */
    private BorderPane createBorderPane() {
        // HBox for Bottom border
            HBox hb = new HBox();
            hb.setPadding(new Insets(12, 45, 12, 45));
            hb.setSpacing(25);
            hb.setStyle("-fx-background-color: #336699;");

            // Text box for the Hbox in the Bottom border pane
                final TextField tbox = new TextField();
                tbox.setPromptText("Enter an Item");
                hb.getChildren().add(tbox);

            // Buttons for the Hbox in the Bottom border pane
                Button btn1 = new Button();
                btn1.setText("Add");
                hb.getChildren().add(btn1);
                Button btn2 = new Button();
                btn2.setText("Clear");
                hb.getChildren().add(btn2);
                Button btn3 = new Button();
                btn3.setText("Reload");
                hb.getChildren().add(btn3);
                hb.setAlignment(Pos.CENTER);

        // VBox for Right border
            VBox RightPane = new VBox();
            RightPane.setPadding(new Insets(20, 5, 0, 5));
            RightPane.setStyle("-fx-background-color: lightGrey;");

            // Top VBox in right border
                VBox VboxInRPane = new VBox();
                Label labelNames = new Label("Results");
                VboxInRPane.setAlignment(Pos.BOTTOM_CENTER);
                VboxInRPane.getChildren().addAll(labelNames);

            // ListView for Vbox in Right  border
                ListView<String> listView = new ListView<>();
                listView.autosize();
                listView.getMaxHeight();

        // add all together to VBox in right pane
        RightPane.getChildren().addAll(VboxInRPane, listView);

        // VBox for Center border
            VBox vb = new VBox();
            vb.setPadding(new Insets(0, 10, 10, 0));
            vb.setSpacing(25);
            vb.setAlignment(Pos.CENTER);

            // Custom Menu Items for Settings
                Menu SettingsMenu = new Menu("Settings");
                CustomMenuItem customMenuItem = new CustomMenuItem();
                TextField nameWheelTxt = new TextField();
                nameWheelTxt.setPromptText("Enter New Name: ");
                customMenuItem.setContent(nameWheelTxt);
                customMenuItem.setHideOnClick(false);
                SettingsMenu.getItems().add(customMenuItem);

                CustomMenuItem customMenuItem2 = new CustomMenuItem();
                Button button = new Button("Update Wheel Name");
                customMenuItem2.setContent(button);
                customMenuItem2.setHideOnClick(false);
                SettingsMenu.getItems().add(customMenuItem2);

            // Results Menu and Items
                Menu menu = new Menu("Results");
                MenuItem menuItem1 = new MenuItem("Reverse List");
                MenuItem menuItem2 = new MenuItem("Reset");
                menu.getItems().add(menuItem1);
                menu.getItems().add(menuItem2);

            // Menu Bar adds everything together
                MenuBar menuBar = new MenuBar();
                menuBar.getMenus().addAll(SettingsMenu, menu);

            // label in Center Vbox
                Label label_top = new Label("Wheel of Decision");
                label_top.setStyle("-fx-font-weight: bold");

            // the chart for the center border
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Grapefruit", 25),
                    new PieChart.Data("Oranges", 25), 
                    new PieChart.Data("Plums", 25), 
                    new PieChart.Data("Pears", 25),
                    new PieChart.Data("Apples", 25));
                PieChart chart = new PieChart(pieChartData);
                chart.setLabelsVisible(false);
                chart.setLegendSide(Side.LEFT);

        // Add all the item in the Xbox
        vb.getChildren().addAll(menuBar, label_top, chart);

        // All the Event handelers
        btn1.setOnAction(e -> {
            String newName = tbox.getText();
            if (!newName.isBlank()) {
                Data newData = new PieChart.Data(newName, 25);
                pieChartData.add(newData);
                tbox.clear();
            }
        });

        btn2.setOnAction(e -> {
            pieChartData.clear();
            listView.getItems().clear();
        });

        btn3.setOnAction(e -> {
            pieChartData.addAll(copyData);
            listView.getItems().clear();
            copyData.clear();
        });

        button.setOnAction(e -> {
            String wheelName = nameWheelTxt.getText();
            if (!wheelName.isBlank()) {
                label_top.setText(wheelName);
                nameWheelTxt.clear();
            }
        });
        
        menuItem1.setOnAction(e ->{
            listView.getItems().sort(Collections.reverseOrder());
        });

        menuItem2.setOnAction(e ->{
            Collections.sort(listView.getItems(), Comparator.naturalOrder());
        });

        chart.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                chart.setLegendVisible(false);
                rotateMe(chart, listView, pieChartData);
            }
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vb);
        borderPane.setRight(RightPane);
        borderPane.setBottom(hb);
        return borderPane;
    }

    protected void rotateMe(PieChart chart, ListView<String> listView, ObservableList<Data> pieChartData) {
        RotateTransition rotate = new RotateTransition();
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setCycleCount(50);
        rotate.setByAngle(360);
        rotate.setDuration(Duration.millis(75));
        rotate.setAutoReverse(false);
        rotate.setNode(chart);
        rotate.play();
        rotate.setOnFinished(e -> theEasyWay(chart, listView, pieChartData));
    }

    private void theEasyWay(PieChart chart, ListView<String> listView, ObservableList<Data> pieChartData) {
        Random rand = new Random();
        int randInt = rand.nextInt(chart.getData().size());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chart.setLegendVisible(true);
        listView.getItems().addAll( ( 1 + listView.getItems().size()) + ": " + chart.getData().get(randInt).getName());
        copyData.add(chart.getData().get(randInt));
        pieChartData.remove(randInt);
    }
}
// https://docs.oracle.com/javafx/2/charts/PieChartSample.java.html
