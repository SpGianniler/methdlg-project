package gr.ict.ihu.metdologia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import org.json.simple.parser.ParseException;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;


public class Handlers{

    static boolean answer;

    public static void checkSuccess(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);
        Label label = new Label();
        label.setText(message);
        label.setAlignment(Pos.CENTER);

        Button btClose = new Button("OK");
        btClose.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(label,btClose);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static boolean confirmBox(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);
        Label label = new Label();
        label.setText(message);

        Button btYes = new Button("Yes");
        Button btNo = new Button("No");

        btYes.setOnAction(e->{
            answer = true;
            window.close();
        });

        btNo.setOnAction(e->{
            answer = false;
            window.close();
        });

        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(10));
        hBox.getChildren().addAll(btYes,btNo);
        hBox.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setBottom(hBox);
        borderPane.setCenter(label);
        borderPane.setPadding(new Insets(10));

        Scene scene = new Scene(borderPane,300,100);
        window.setScene(scene);
        window.showAndWait();
        window.centerOnScreen();

        return answer;
    }

    public static ObservableList<Ergazomenoi> getErgazomenoi(ArrayList<Ergazomenoi> erg) throws IOException, ParseException {

        ObservableList<Ergazomenoi> ergazomenois = FXCollections.observableArrayList();
        ergazomenois.addAll(erg);

        return ergazomenois;
    }

    public static ObservableList<Vardia> getVardies(ArrayList<Vardia> vrd) throws IOException, ParseException {

        ObservableList<Vardia> vardias = FXCollections.observableArrayList();
        vardias.addAll(vrd);

        return vardias;
    }

    public static ObservableList<Vardia> deleteVardies() throws IOException, ParseException {

        ObservableList<Vardia> vardias = FXCollections.observableArrayList();
        vardias.setAll((Vardia) null);

        return vardias;
    }

    public static TreeItem<String> makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);

        return item;
    }


}
