package listview;

import db.DBOperations;
import entity.NewsItem;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import parser.ITCParser;

public class MainListCell extends ListCell<NewsItem> {

/**
 * The updateItem method should not be called by developers, but it is the
 * best method for developers to override to allow for them to customise the
 * visuals of the cell.
 * @param item
 * Contains information about particular news to fill list cell with it.
 **/
    @Override
    public void updateItem(NewsItem item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            Text entryTitle = new Text(item.getEntryTitle());
            entryTitle.setWrappingWidth(600);
            entryTitle.setFont(Font.font("Segoe UI",FontWeight.BOLD,18));
            entryTitle.setStyle("-fx-font-smoothing-type:lcd;");

            Text entryExcerpt = new Text(item.getEntryExcerpt());
            entryExcerpt.setWrappingWidth(600);
            entryExcerpt.setFont(Font.font("Segoe UI",FontWeight.LIGHT,14));
            entryExcerpt.setStyle("-fx-font-smoothing-type:lcd;");

            Text date = new Text(item.getDate());
            date.setFont(Font.font("Segoe UI",FontWeight.LIGHT,14));
            date.setStyle("-fx-font-smoothing-type:lcd;");

            ImageView imageView = new ImageView(item.getImage());

            Button btnAdd = new Button();
            btnAdd.setGraphic(new ImageView(new Image("download_32.png")));

            if(ITCParser.getInstance().isAvailable()) {
                if(DBOperations.getInstance().isNewsStored(item)) {
                    btnAdd.setGraphic(new ImageView(new Image("saved_32.png")));
                    btnAdd.setDisable(true);
                }
                else btnAdd.setVisible(true);
                btnAdd.setFocusTraversable(false);
                btnAdd.getStylesheets().add(this.getClass()
                        .getResource("/itcstyle_button.css").toExternalForm());
                btnAdd.getStyleClass().add("btn-up");

                btnAdd.setOnAction(event -> {
                    btnAdd.setGraphic(new ImageView(new Image("saved_32.png")));
                    btnAdd.setDisable(true);
                    item.setNewsDetails(ITCParser.getInstance().getNewsDetails(item));
                    DBOperations.getInstance().addNews(item);
                });
            } else btnAdd.setVisible(false);

            GridPane gridPane = new GridPane();
            gridPane.setVgap(10);
            gridPane.add(entryTitle,2,1);
            gridPane.add(date,2,2);
            gridPane.add(entryExcerpt,2,3);
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            Pane emptyPane = new Pane();
            emptyPane.setPrefWidth(20);
            hBox.getChildren().addAll(imageView,gridPane,emptyPane,btnAdd);
            setGraphic(hBox);
        }
    }
}