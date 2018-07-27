package stage;

import controller.NewsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import entity.NewsItem;

import java.io.IOException;

/**
 * NewsStage is a GUI form of particular news.
 */
public class NewsStage {

    private NewsItem newsItem;

    public NewsStage(NewsItem newsItem) {
        this.newsItem = newsItem;
    }

    /**
     * Initialize main components on NewsStage.
     */
    public void start() {
        Stage newsStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/itcua_news_details.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewsController newsController = fxmlLoader.getController();
        newsController.init(newsItem);
        newsStage.setTitle(newsItem.getEntryTitle());
        newsStage.getIcons().add(new Image("/itc_icon.png"));
        newsStage.setScene(new Scene(root, 1150, 700));
        newsStage.setMinWidth(600);
        newsStage.setMinHeight(400);
        newsStage.show();
    }
}