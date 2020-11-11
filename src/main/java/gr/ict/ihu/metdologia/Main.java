package gr.ict.ihu.metdologia;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Main extends Application {

    Stage window;
    Scene scMenu;
    Button btWorkers, btSchedule, btReturnToMenu, btSchCreate, btExit, btSchSave, btSchDelete;
    public static BorderPane borderPaneMain;
    VBox vboxMenu, vBoxWorkers, vBoxSchedule, vBoxToMenu, vBoxTreeErg, vBoxTreeSch;
    TableView<Ergazomenoi> tvErg;
    TableView<Vardia> tvSch;
    TreeView<String> trvErg,trvSch;
    ChoiceBox<String> cbCategories;
    TextField tfFName, tfLName, tfEid, tfOffDays, tfIFName, tfILName, tfIEid, tfIAge, tfIHours;
    public static DatePicker dpSchStart, dpSchEnd, dpOffStart;
    public static ArrayList<Ergazomenoi> ergazomenoi;
    public static ArrayList<Vardia> vardiaArrayList;
    public static ArrayList<RestDay> restDayArrayList;
    public static Matrix matrix;
    public static ObservableList<Person> people;
    ListToMap ltm;
    public static JsonReader jsonReader;
    public static TableView<Person> tvPerson;
    boolean schCreated=false, mtrTblCreated=false;
    public static long diff;
    GridPane gpDaysOff, gpIndividuals;
    public static LocalDate start;



    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, ParseException {
        jsonReader = new JsonReader();
        ergazomenoi = new ArrayList<>();
        restDayArrayList = new ArrayList<>();
        ltm = new ListToMap();
        ltm.transition(ergazomenoi);
        Validity validity = new Validity();

        window = primaryStage;
        window.setTitle("Shift Manager");
        window.setOnCloseRequest(e->{
            e.consume();
            closeProgram();
        });

        //File Menu
        Menu fileMenu = new Menu("File");

        MenuItem m_clear_data = new MenuItem("Clear Data");
        fileMenu.getItems().add(m_clear_data);
        MenuItem m_load_data = new MenuItem("Load Data");
        m_load_data.setOnAction(e ->{
            try {
                jsonReader.readFile();
                jsonReader.readDepends();
                ergazomenoi = (JsonReader.getErg());

                ltm.transition(ergazomenoi);

                Handlers.checkSuccess("Success!", "Data loaded successfully!");
            }
            catch (IOException | ParseException ioException) {
                Handlers.checkSuccess("Error!", "File did not load correctly!");
                ioException.printStackTrace();
            }
        });
        fileMenu.getItems().add(m_load_data);
        MenuItem m_export = new MenuItem("Export All");
        m_export.setOnAction(e->{
            try {
                DataExport.export();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Handlers.checkSuccess("Success!", "Data saved successfully!");
        });
        fileMenu.getItems().add(m_export);
        fileMenu.getItems().add(new SeparatorMenuItem());
        MenuItem m_settings = new MenuItem("Settings...");
        fileMenu.getItems().add(m_settings);
        fileMenu.getItems().add(new SeparatorMenuItem());
        MenuItem m_exit = new MenuItem("Exit");
        m_exit.setOnAction(e->closeProgram());
        fileMenu.getItems().add(m_exit);

        //Edit Menu
        Menu editMenu = new Menu("Edit");

        MenuItem m_copy = new MenuItem("Copy");
        editMenu.getItems().add(m_copy);
        MenuItem m_paste = new MenuItem("Paste");
        editMenu.getItems().add(m_paste);
        MenuItem m_delete = new MenuItem("Delete");
        editMenu.getItems().add(m_delete);

        //Help Menu
        Menu helpMenu = new Menu("Help");

        MenuItem m_about = new MenuItem("About");
        helpMenu.getItems().add(m_about);

        //Main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,editMenu,helpMenu);

        //Create Buttons
        btWorkers = new Button("Workers");
        btWorkers.setPrefWidth(80);
        btWorkers.setOnAction(e-> {

            //Create ChoiceBox
            cbCategories = new ChoiceBox<>();
            cbCategories.getItems().addAll(ltm.getFields());
            cbCategories.getItems().add("All");
            cbCategories.setValue("All");

            //Create ErgazomenoiTable
            TableColumn<Ergazomenoi, String> clmErgName = new TableColumn<>("Όνομα");
            clmErgName.setMinWidth(150);
            clmErgName.setCellValueFactory(new PropertyValueFactory<>("onoma"));
            TableColumn<Ergazomenoi, String> clmErgSur = new TableColumn<>("Επίθετο");
            clmErgSur.setMinWidth(150);
            clmErgSur.setCellValueFactory(new PropertyValueFactory<>("epitheto"));
            TableColumn<Ergazomenoi, String> clmErgPost = new TableColumn<>("Ειδικότητα");
            clmErgPost.setMinWidth(100);
            clmErgPost.setCellValueFactory(new PropertyValueFactory<>("eidikotita"));
            TableColumn<Ergazomenoi, String> clmErgAge = new TableColumn<>("Ηλικία");
            clmErgAge.setMinWidth(50);
            clmErgAge.setCellValueFactory(new PropertyValueFactory<>("hlikia"));
            TableColumn<Ergazomenoi, String> clmErgHours = new TableColumn<>("Ώρες");
            clmErgHours.setMinWidth(50);
            clmErgHours.setCellValueFactory(new PropertyValueFactory<>("evWres"));

            tvErg = new TableView<>();
            tvErg.getColumns().addAll(clmErgName, clmErgSur, clmErgPost, clmErgAge, clmErgHours);
            try {
                tvErg.setItems(Handlers.getErgazomenoi(ergazomenoi));
            } catch (IOException | ParseException ioException) {
                ioException.printStackTrace();
            }
            ObservableList<Ergazomenoi> ergCat = FXCollections.observableArrayList();
            cbCategories.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
                tvErg.getItems().clear();
                if(newValue.equals("All")){
                    try {
                        tvErg.setItems(Handlers.getErgazomenoi(ergazomenoi));
                    } catch (IOException | ParseException e2) {
                        e2.printStackTrace();
                    }
                }
                else {
                    for (Ergazomenoi erg : ergazomenoi) {
                        if (erg.eidikotita.equals(newValue)) {
                            ergCat.add(erg);
                        }
                    }
                    tvErg.setItems(ergCat);
                }
            });

            //Create WorkersScene
            vBoxWorkers = new VBox(10);
            vBoxWorkers.setPadding(new Insets(10));
            HBox hBoxWork = new HBox(8);
            hBoxWork.setPadding(new Insets(0));
            hBoxWork.setAlignment(Pos.CENTER_RIGHT);
            Button btWorkExp = new Button("Export Workers Data");
            btWorkExp.setOnAction(e1->{
                try {
                    DataExport.export_erg();
                }
                catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Handlers.checkSuccess("Success!", "Workers data exported successfully!");
            });
            hBoxWork.getChildren().add(btWorkExp);
            vBoxWorkers.getChildren().addAll(cbCategories, tvErg, hBoxWork);

            borderPaneMain.setBottom(vBoxToMenu);
            borderPaneMain.setRight(null);
            borderPaneMain.setLeft(vBoxTreeErg);
            borderPaneMain.setCenter(vBoxWorkers);
            if(!window.isMaximized()){
                window.setWidth(690);
                window.setHeight(500);
                            }
        });
        btSchedule = new Button("Schedule");
        btSchedule.setPrefWidth(80);
        btSchedule.setOnAction(e -> {
            //Create ScheduleTable
            TableColumn<Vardia, String> clmVarDate = new TableColumn<>("Ημέρα");
            clmVarDate.setMinWidth(80);
            clmVarDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            TableColumn<Vardia, String> clmVarShift = new TableColumn<>("Βάρδια");
            clmVarShift.setMinWidth(80);
            clmVarShift.setCellValueFactory(new PropertyValueFactory<>("shift"));
            TableColumn<Vardia, String> clmVarName = new TableColumn<>("Όνομα");
            clmVarName.setMinWidth(150);
            clmVarName.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn<Vardia, String> clmVarSur = new TableColumn<>("Επίθετο");
            clmVarSur.setMinWidth(150);
            clmVarSur.setCellValueFactory(new PropertyValueFactory<>("surname"));
            TableColumn<Vardia, String> clmVarPost = new TableColumn<>("Πόστο");
            clmVarPost.setMinWidth(100);
            clmVarPost.setCellValueFactory(new PropertyValueFactory<>("post"));
            tvSch = new TableView<>();
            tvSch.getColumns().addAll(clmVarDate,clmVarShift,clmVarName,clmVarSur,clmVarPost);
            if(schCreated) {
                try {
                    tvSch.setItems(Handlers.getVardies(vardiaArrayList));
                } catch (IOException | ParseException ioException) {
                    ioException.printStackTrace();
                }
            }
            //Create Schedule Scene
            vBoxSchedule = new VBox(10);
            GridPane gridPaneDates = new GridPane();
            gridPaneDates.setVgap(15);
            gridPaneDates.setHgap(8);
            dpSchStart = new DatePicker();
            Label lbStart = new Label("From Date");
            dpSchStart.setValue(LocalDate.now());
            dpSchEnd = new DatePicker();
            LocalDate nextWeek = LocalDate.now().plus(6, ChronoUnit.DAYS);
            dpSchEnd.setValue(nextWeek);
            Label lbEnd = new Label("To Date");
            gridPaneDates.add(lbStart,0,0,1,1);
            gridPaneDates.add(dpSchStart,1,0,1,1);
            gridPaneDates.add(lbEnd,0,1,1,1);
            gridPaneDates.add(dpSchEnd,1,1,1,1);
            vBoxSchedule.setPadding(new Insets(10,10,10,5));
            HBox hBoxSchEditors = new HBox(10);
            hBoxSchEditors.setPadding(new Insets(0));
            hBoxSchEditors.getChildren().addAll(btSchCreate, btSchSave, btSchDelete);
            hBoxSchEditors.setAlignment(Pos.CENTER_RIGHT);
            vBoxSchedule.getChildren().addAll(gridPaneDates,tvSch, hBoxSchEditors);

            borderPaneMain.setBottom(vBoxToMenu);
            borderPaneMain.setRight(null);
            borderPaneMain.setLeft(vBoxTreeSch);
            borderPaneMain.setCenter(vBoxSchedule);
                        if (!window.isMaximized()){
                window.setWidth(700);
                window.setHeight(450);
            }
        });
        btExit = new Button("Exit");
        btExit.setPrefWidth(80);
        btExit.setOnAction(e->closeProgram());
        btReturnToMenu = new Button("Return to menu");
        btReturnToMenu.setOnAction(e -> returnToMenu());

        //Create Matrix TableView
        VBox vBoxMatrix = new VBox(10);
        vBoxMatrix.setPadding(new Insets(10));

        btSchCreate = new Button("Create Schedule");
        btSchCreate.setPrefWidth(105);
        btSchCreate.setOnAction(e-> {
            matrix= new Matrix(ergazomenoi.size());
            try {
                LocalDate start = dpSchStart.getValue();
                LocalDate end = dpSchEnd.getValue();
                diff = ChronoUnit.DAYS.between(start,end);
                if(validity.hoursOK(ergazomenoi,jsonReader)){
                    vardiaArrayList = Schedule.createSchedule(ergazomenoi);
                    tvSch.setItems(Handlers.getVardies(vardiaArrayList));
                    schCreated = true;
                    if(!mtrTblCreated){
                        matrix.getMatrixList();
                        tvPerson = new TableView<>();
                        people = matrix.getMatrixList();
                        matrix.createMatrixTable(vBoxMatrix);
                        mtrTblCreated=true;
                    }
                }
                else
                    Handlers.checkSuccess("Error!","Ελέγξτε τις ώρες εργαζομένων!");
            }
            catch (IOException | ParseException ioException) {
                ioException.printStackTrace();
            }
        });
        btSchSave = new Button("Export Schedule");
        btSchSave.setPrefWidth(105);
        btSchSave.setOnAction((e-> {
            try {
                DataExport.export_vardia();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Handlers.checkSuccess("Success!", "Schedule exported successfully!");
        }));
        btSchDelete = new Button("Delete Schedule");
        btSchDelete.setOnAction(e-> {
            boolean delete = Handlers.confirmBox("Delete Schedule", "Are you sure you want to delete this schedule?");
            if(delete) {
                try {
                    tvSch.setItems(Handlers.deleteVardies());
                    matrix.nullMatrix();
                    schCreated = false;
                }
                catch (IOException | ParseException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        btSchDelete.setPrefWidth(105);

        //Create WorkersTreeView
        TreeItem<String> rootErg;
        rootErg = new TreeItem<>("View");
        rootErg.setExpanded(true);

        trvErg = new TreeView<>(rootErg);
        trvErg.setPrefWidth(90);
        trvErg.setPrefHeight(346);
        Handlers.makeBranch("List",rootErg);
        Handlers.makeBranch("Individuals",rootErg);
        trvErg.setShowRoot(false);
        vBoxTreeErg = new VBox(10);
        vBoxTreeErg.setPadding(new Insets(45,0,5,5));
        vBoxTreeErg.getChildren().add(trvErg);

        //Create ScheduleTreeView
        TreeItem<String> rootSch;
        rootSch = new TreeItem<>("View");
        rootSch.setExpanded(true);

        trvSch = new TreeView<>(rootSch);
        trvSch.setPrefWidth(110);
        trvSch.setPrefHeight(296);
        Handlers.makeBranch("Schedule", rootSch);
        Handlers.makeBranch("Worker Hours", rootSch);
        Handlers.makeBranch("Individual Days Off", rootSch);
        trvSch.setShowRoot(false);


        vBoxTreeSch = new VBox(10);
        vBoxTreeSch.setPadding(new Insets(10,0,5,5));
        vBoxTreeSch.getChildren().add(trvSch);

        //Create MenuScene
        vboxMenu = new VBox(10);
        vboxMenu.setPadding(new Insets(20,20,20,20));
        vboxMenu.getChildren().addAll(btWorkers,btSchedule,btExit);
        vboxMenu.setAlignment(Pos.CENTER);
        borderPaneMain = new BorderPane();
        borderPaneMain.setTop(menuBar);
        borderPaneMain.setCenter(vboxMenu);
        scMenu = new Scene(borderPaneMain, 300,200);

        //Create Individuals Scene
        gpIndividuals = new GridPane();
        gpIndividuals.setHgap(5);
        gpIndividuals.setVgap(8);
        gpIndividuals.setPadding(new Insets(45,10,10,15));
        Label lbIFName = new Label("Όνομα");
        Label lbILName = new Label("Επίθετο");
        Label lbIEidik = new Label("Ειδικότητα");
        Label lbIAge = new Label("Ηλικία");
        Label lbIHours = new Label("Εβδομαδιαίες Ώρες");
        tfIFName = new TextField();
        tfIFName.setPromptText("Όνομα");
        tfILName = new TextField();
        tfILName.setPromptText("Επίθετο");
        tfIEid = new TextField();
        tfIEid.setPromptText("Ειδικότητα");
        tfIAge = new TextField();
        tfIAge.setPromptText("Ηλικία");
        tfIHours = new TextField();
        tfIHours.setPromptText("Ώρες/Εβδομάδα");

        Button btSaveInd = new Button("Save");
        btSaveInd.setOnAction(e->{
            boolean found = false;
            Ergazomenoi individual = new Ergazomenoi(tfIFName.getText(), tfILName.getText(),tfIEid.getText(), tfIAge.getText(), Integer.parseInt(tfIHours.getText()));
            for (Ergazomenoi ergazomenoi1 : ergazomenoi){
                if(ergazomenoi1.onoma.equals(tfIFName.getText()) && ergazomenoi1.epitheto.equals(tfILName.getText()) && ergazomenoi1.eidikotita.equals(tfIEid.getText()) && ergazomenoi1.hlikia.equals(tfIAge.getText())){
                    found=true;
                }
            }
            if(found){
                Handlers.checkSuccess("Error!","Αυτός ο εργαζόμενος υπάρχει ήδη!");
            }
            else {
                ergazomenoi.add(individual);
                ltm.HashErg.put(individual, individual.eidikotita);
                ltm.fields.add(individual.eidikotita);
                try {
                    tvErg.setItems(Handlers.getErgazomenoi(ergazomenoi));
                } catch (IOException | ParseException ioException) {
                    ioException.printStackTrace();
                }
            }
            tfIFName = null;
            tfILName = null;
            tfIEid = null;
            tfIAge = null;
            tfIHours = null;
        });

        gpIndividuals.add(lbIFName,0,0,2,1);
        gpIndividuals.add(lbILName,0,1,2,1);
        gpIndividuals.add(lbIEidik,0,2,2,1);
        gpIndividuals.add(lbIAge,0,3,2,1);
        gpIndividuals.add(lbIHours,0,4,2,1);
        gpIndividuals.add(tfIFName,3,0);
        gpIndividuals.add(tfILName,3,1);
        gpIndividuals.add(tfIEid,3,2);
        gpIndividuals.add(tfIAge,3,3);
        gpIndividuals.add(tfIHours,3,4);
        gpIndividuals.add(btSaveInd,3,6);


        //Create Days Off Scene
        gpDaysOff = new GridPane();
        gpDaysOff.setHgap(5);
        gpDaysOff.setVgap(8);
        gpDaysOff.setPadding(new Insets(10,10,10,15));

        Label lbFName = new Label("Όνομα");
        Label lbLName = new Label("Επίθετο");
        Label lbEidik = new Label("Ειδικότητα");
        Label lbSDate = new Label("Αρχή Άδειας");
        Label lbOffDays = new Label("Ημέρες Άδειας");
        tfFName = new TextField();
        tfFName.setPromptText("Όνομα");
        tfLName = new TextField();
        tfLName.setPromptText("Επίθετο");
        tfEid = new TextField();
        tfEid.setPromptText("Ειδικότητα");
        dpOffStart = new DatePicker();
        dpOffStart.setValue(LocalDate.now());
        tfOffDays = new TextField();
        tfOffDays.setPromptText("Πλήθος Ημερών");
        Button btSaveDOff = new Button("Save");
        btSaveDOff.setOnAction(e->{
            boolean success = false;
            for(Ergazomenoi erg : ergazomenoi){
                if(erg.onoma.equals(tfFName.getText()) && erg.epitheto.equals(tfLName.getText()) && erg.eidikotita.equals(tfEid.getText()) ){
                    success = true;
                    long diff = ChronoUnit.DAYS.between(dpSchStart.getValue(),dpOffStart.getValue());
                    RestDay temp = new RestDay(erg,(int)diff,Integer.parseInt(tfOffDays.getText()));
                    restDayArrayList.add(temp);
                }
            }
            if (!success)
                Handlers.checkSuccess("Error!","Λάθος στοιχεία εργαζομένου!");
            tfFName.setText(null);
            tfLName.setText(null);
            tfEid.setText(null);
            dpOffStart.setValue(LocalDate.now());
            tfOffDays.setText(null);
        });
        gpDaysOff.add(lbFName,0,0,2,1);
        gpDaysOff.add(lbLName,0,1,2,1);
        gpDaysOff.add(lbEidik,0,2,2,1);
        gpDaysOff.add(lbSDate,0,3,2,1);
        gpDaysOff.add(lbOffDays,0,4,2,1);
        gpDaysOff.add(tfFName,3,0);
        gpDaysOff.add(tfLName,3,1);
        gpDaysOff.add(tfEid,3,2);
        gpDaysOff.add(dpOffStart,3,3);
        gpDaysOff.add(tfOffDays,3,4);
        gpDaysOff.add(btSaveDOff,3,6);

        //Create ReturnToMenu Scene
        vBoxToMenu = new VBox(10);
        vBoxToMenu.getChildren().addAll(btReturnToMenu);
        vBoxToMenu.setPadding(new Insets(0,0,10,5));

        trvErg.getSelectionModel().selectedIndexProperty().addListener((v, oldValue, newValue)->{
            if(newValue.equals(0)){
                borderPaneMain.setCenter(vBoxWorkers);
            }
            if(newValue.equals(1)){
                borderPaneMain.setCenter(gpIndividuals);
            }
        });

        trvSch.getSelectionModel().selectedIndexProperty().addListener((v, oldValue, newValue)-> {
            if(newValue.equals(0)){
                borderPaneMain.setCenter(vBoxSchedule);
            }
            if(newValue.equals(1)){
                borderPaneMain.setCenter(vBoxMatrix);
            }
            if(newValue.equals(2)){
                borderPaneMain.setCenter(gpDaysOff);
            }
        });

        //Show window
        window.setScene(scMenu);
        window.show();
        window.centerOnScreen();
    }

    public void closeProgram(){
        boolean result = Handlers.confirmBox("Confirm Exit","Are you sure you want to exit?");
        if (result)
            window.close();
    }
    private void returnToMenu(){
        borderPaneMain.setCenter(vboxMenu);
        borderPaneMain.setBottom(null);
        borderPaneMain.setRight(null);
        borderPaneMain.setLeft(null);
        if (!window.isMaximized()){
            window.setWidth(300);
            window.setHeight(200);
        }
    }


}






























































