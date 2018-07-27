package controller;


import db.DBOperations;
import entity.NewsDetails;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import parser.ITCParser;
import entity.NewsItem;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller initializes particular news GUI.
 * Contains actions which provide main behavior.
 */
public class NewsController implements Initializable {

    @FXML
    private VBox vBox;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnAdd;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * <tt>null</tt> if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or <tt>null</tt> if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    /**
     * Called to initialize main GUI elements.
     * @param newsItem
     * Provides content of particular news.
     */
    public void init(NewsItem newsItem) {

        if (ITCParser.getInstance().isAvailable()) {
            if(DBOperations.getInstance().isNewsStored(newsItem)) {
                btnAdd.setGraphic(new ImageView(new Image("saved_44.png")));
                btnAdd.setDisable(true);
            }
            else btnAdd.setVisible(true);
            btnAdd.setOnAction(event -> {
                btnAdd.setGraphic(new ImageView(new Image("saved_44.png")));
                btnAdd.setDisable(true);
                DBOperations.getInstance().addNews(newsItem);
            });
        } else btnAdd.setVisible(false);

        vBox.setStyle("-fx-background-color: white");

        ScrollPane sp = new ScrollPane(vBox);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-insets: 1;");
        sp.setVvalue(0);
        borderPane.setCenter(sp);

        Label lblHeader = new Label();
        lblHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        lblHeader.setWrapText(true);
        lblHeader.setPadding(new Insets(10, 0, 0, 0));
        lblHeader.setPrefWidth(770);
        lblHeader.setAlignment(Pos.CENTER_LEFT);
        lblHeader.setText(newsItem.getEntryTitle());



        Label lblDate = new Label();
        lblDate.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 14));
        lblDate.setText(newsItem.getDate());
        lblDate.setAlignment(Pos.CENTER_LEFT);
        lblDate.setPrefWidth(770);
        lblDate.setPadding(new Insets(10, 0, 10, 0));

        vBox.getChildren().add(new StackPane(lblHeader));
        vBox.getChildren().add(new StackPane(lblDate));

        List<NewsDetails> details;
        if(ITCParser.getInstance().isAvailable()) details = ITCParser.getInstance().getNewsDetails(newsItem);
        else details = DBOperations.getInstance().getNewsDetails(newsItem.getHref());
        for (NewsDetails d : details) {
            if(d.getImage() != null) {
                StackPane stp = new StackPane(new ImageView(d.getImage()));
                vBox.getChildren().add(stp);
            }

            if(d.getText() != null) {
                Label l = new Label(d.getText());
                l.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 16));
                l.setWrapText(true);
                l.setPrefWidth(770);
                l.setPadding(new Insets(10, 0, 10, 0));
                l.setAlignment(Pos.CENTER_LEFT);
                StackPane stp = new StackPane(l);
                vBox.getChildren().add(stp);
            }
        }
    }

}
