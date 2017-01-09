package WHS_planner.Calendar;

import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by geoffrey_wang on 12/30/16.
 */
public class CalendarBox extends Pane{
    public static final int CALENDAR_BOX_MIN_HEIGHT = 80, CALENDAR_BOX_MIN_WIDTH = 110; //Constant that defines the min size of a CalendarBox
    public static final int HOMEWORK = 0; //List IDs (Default)
    private static final String[] ICONS_UNICODE = new String[]{"\uf0f6"}; //File Icon, Check Icon (Font UNICODE)

    private int date; //The date of the box
    private int week; //The week this box is in

    private ArrayList<ArrayList<Task>> tasks; //List of the lists of tasks
    private StackPane mainPane;
    private VBox taskBar;
    private VBox tasksPane;

    private JFXButton button;
    private VBox vBox;
    private StackPane dateLabelStackPane;
    private Circle dayCircle;
    private Label dateLabel;
    private HBox iconContainer;

    public CalendarBox(int date, int week, boolean active, ArrayList<Task> tasks){
        this.date = date; //This box's date
        this.week = week; //The week (row) this box is in

        if(tasks == null){
            this.tasks = new ArrayList<>(); //Used to hold lists of tasks (Ex. List of homeworks, list of tests, etc)

            //Creates and fills in tasks with correct amount of lists according to NUMBER_OF_TASKLISTS
            for (int taskListIndex = 0; taskListIndex < ICONS_UNICODE.length; taskListIndex++) {
                this.tasks.add(new ArrayList<>()); //Create a new list
            }
        }else{
            this.tasks = new ArrayList<>(); //Used to hold lists of tasks (Ex. List of homeworks, list of tests, etc)

            this.tasks.add(tasks);
        }

        //Creates the entire pane
        mainPane = new StackPane();
        mainPane.setId("stackPane");
        mainPane.setMinSize(CALENDAR_BOX_MIN_WIDTH,CALENDAR_BOX_MIN_HEIGHT);

        button = new JFXButton();
        button.setId("button");
        button.getStyleClass().setAll("box-button");
        button.prefHeightProperty().bind(mainPane.heightProperty());
        button.prefWidthProperty().bind(mainPane.widthProperty());

        mainPane.getChildren().add(button);

        vBox = new VBox();
        vBox.setId("vbox");
        vBox.setMouseTransparent(true);
        vBox.prefHeightProperty().bind(mainPane.heightProperty());
        vBox.prefWidthProperty().bind(mainPane.widthProperty());

        mainPane.getChildren().add(vBox);

        dateLabelStackPane = new StackPane();
        dateLabelStackPane.setId("dateLabelStackPane");
        dateLabelStackPane.setAlignment(Pos.TOP_LEFT);
        dateLabelStackPane.setMouseTransparent(true);

        vBox.getChildren().add(dateLabelStackPane);

        dayCircle = new Circle();
        dayCircle.setId("dayCircle");
        dayCircle.fillProperty().set(Color.WHITE);
        dayCircle.radiusProperty().setValue(10);
        dayCircle.setMouseTransparent(true);
        dayCircle.getStyleClass().setAll("date-circle");

        dateLabelStackPane.getChildren().add(dayCircle);

        dateLabel = new Label();
        dateLabel.setId("dateLabel");
        dateLabel.setMouseTransparent(true);
        dateLabel.getStyleClass().setAll("date-label");

        dateLabelStackPane.getChildren().add(dateLabel);
        dateLabelStackPane.setMargin(dateLabel,new Insets(-1,0,0,4));

        iconContainer = new HBox();
        iconContainer.setId("iconContainer");
        iconContainer.setAlignment(Pos.CENTER);
        iconContainer.setMouseTransparent(true);
        iconContainer.prefWidthProperty().bind(vBox.widthProperty());
        iconContainer.prefHeightProperty().bind(vBox.heightProperty());

        vBox.getChildren().add(iconContainer);

        this.getChildren().setAll(mainPane);
        mainPane.prefWidthProperty().bind(this.widthProperty());
        mainPane.prefHeightProperty().bind(this.heightProperty());

        //Set up the calendar box
        initFXMLBox();

        //Make the button inactive if required
        if(!active){
            button.setDisable(true);
            dateLabel.setText("");
        }
        update();
    }

    /*-----METHODS-----*/

    //Initializes this box
    public void initFXMLBox(){
        String dateString = date + ""; //Creates a string version of the date value
        dateLabel.setText(dateString); //Set the dateLabel text = to the date

        //Set the buttonClicked action
        button.setOnMouseClicked((event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                update();
                Calendar calendar = (Calendar)this.getParent().getParent().getParent();
                calendar.update(week,date);
                updateTaskBox();
            }
        }));
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        if (day == this.getDate()) {
            dayCircle.setFill(new Color(255/255, 152/255.0, 0, 100/100));
        } else {
            dayCircle.setFill(new Color(255/255, 152/255, 0, 0));
        }

        if (this.getDate() >= 10) {
            StackPane sp = dateLabelStackPane;
            StackPane.setMargin(dayCircle, new Insets(0, 0, 0, 4.5));
        }
//        else {
//            this.setStyle("-fx-background-color: #FFFFFF");
//        }

        this.getStyleClass().add("box"); //Set the CSS style class to be box
        this.getChildren().setAll(mainPane); //Set this pane to contain the mainPane
        this.setId("calendar-box"); //Set the id of this box to be "calendar-box"

        //Initiate update sequence
        update();
    }

    //Updates the iconContainer
    public void update(){
        ArrayList<Node> icons = new ArrayList<Node>(); //Create a list for all the icons

        //Loop through all the task lists and add icons according to the content
        for (int listID = 0; listID < tasks.size(); listID++) {
            if(getTaskCount(listID) != 0) { //Only add an icon if there is more than one task
                //Create the ICON
                Label icon = new Label();
                icon.getStyleClass().add("icon");
                icon.setId("icon");
                icon.setText(ICONS_UNICODE[listID]);

                //Create the badge on the Label
                JFXBadge badge = new JFXBadge(icon, Pos.TOP_RIGHT);
                badge.getStyleClass().add("icon-badge");
//                badge.getChildren().get(0).getStyleClass().setAll("testsefd");
                badge.setText("" + getTaskCount(listID)); //Set the badge number
                icons.add(badge);
            }
        }

        iconContainer.getChildren().setAll(icons); //Add all the "icons" into "iconContainer"
    }

    //Create a taskBox
    public Node getTaskBox(ReadOnlyDoubleProperty widthProperty){
        //If there is no taskBox create one
        if(taskBar == null) {
            FXMLLoader loader = new FXMLLoader(); //Create a new FXML Loader
            loader.setLocation(getClass().getResource("/Calendar/taskBox.fxml")); //Set location of taskbox FXML file

            taskBar = new VBox(); //Creates a return taskbox

            try {
                taskBar = loader.load(); //Load from FXML
                taskBar.prefWidthProperty().bind(widthProperty); //Set the width of the taskbox to be the same as the width passed in

                tasksPane = new VBox();
                tasksPane.prefWidthProperty().bind(widthProperty);

                if(taskBar.getChildren().size() != 2){
                    taskBar.getChildren().add(1,tasksPane);
                }

                //Get the JFXTextField and set the width to grow
                HBox hBox = (HBox) taskBar.getChildren().get(0);
                JFXTextField textBox = (JFXTextField) hBox.getChildren().get(0);
                HBox.setHgrow(textBox, Priority.ALWAYS);

                //Set pressing enter to clear the box text
                textBox.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        String textBoxText = textBox.getText();
                        if (textBoxText.trim().length() > 0){
                            addTask(HOMEWORK, new Task("","", textBoxText));
                            update();
                        }
                        textBox.clear();
                        updateTaskBox();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return taskBar;
    }

    public void updateTaskBox(){
        tasksPane.getChildren().clear();
        for (int i = 0; i < tasks.get(0).size(); i++) {
            if (tasks.get(0).get(i).doesExist()){
                tasksPane.getChildren().add(0, tasks.get(0).get(i).getPane(this));
            }
        }
    }

    /*-----ID RELATED-----*/
    //Get the date of this box
    public int getDate() {
        return date;
    }

    //Get the week this box is in (row)
    public int getWeek() {
        return week;
    }

    /*-----TASK RELATED-----*/
    //Used to get the number of tasks in a certain list
    public int getTaskCount(int listID){
        int returnValue = 0;
        for (int i = 0; i < tasks.get(listID).size(); i++) {
            if (tasks.get(listID).get(i).doesExist()) {
                returnValue++;
            }
        }
        return returnValue;
    }

    //Adds a task in a certain list based on the listID
    public void addTask(int listID, Task task){
        tasks.get(listID).add(task);
    }

    //Removes a task in a certain list based on the listID
    public void removeTask(int listID, Task task){
        tasks.get(listID).remove(task);
    }

    /*-----NODE RELATED-----*/
    //Get the button
    public JFXButton getButtonNode(){
        return button;
    }

    //Get the date Label
    public Label getDateLabel(){
        return dateLabel;
    }


    // Tzurs Code
    // Restart related

    public ArrayList<ArrayList<Task>> getTasks() {
        return tasks;
    }
}
