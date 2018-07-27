package controller;

import db.DBOperations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import listview.MainListCell;
import parser.ITCParser;
import entity.NewsItem;
import stage.NewsStage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller initializes main GUI.
 * Contains actions which provide main behavior.
 */
public class MainController implements Initializable {

    @FXML
    private ListView<NewsItem> listView;

    @FXML
    private Button btnMain;

    @FXML
    private Button btnNews;

    @FXML
    private Button btnArticles;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnPrev;

    @FXML
    private Button btnUp;

    @FXML
    private MenuItem notebookCategory;

    @FXML
    private MenuButton menuBtnCategory;

    @FXML
    private MenuButton menuBtnBookmarks;

    @FXML
    private MenuItem smartfonCategory;

    private ObservableList<NewsItem> newsItems;

    private NewsItem currentNewsItem;

    private String type = "main";

    private int newsPageNum = 1;

    private int articlePageNum = 1;

    private int mainPageNum = 1;

    private int notebookPageNum = 1;

    private int smartfonPageNum = 1;

    private ITCParser parser;


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
    public void initialize(URL location, ResourceBundle resources) {

        parser = ITCParser.getInstance();

        if(ITCParser.getInstance().isAvailable()) {

            showNewsTitles();

            btnMain.setOnAction(event -> {
                type = "main";
                mainPageNum = 1;
                parser.setPage("http://itc.ua/", "div.type-post, div.type-articles");
                showNewsTitles();
                btnPrev.setDisable(true);
            });

            btnNews.setOnAction(event -> {
                type = "news";
                newsPageNum = 1;
                parser.setPage("http://itc.ua/news/", "div.type-post");
                showNewsTitles();
                btnPrev.setDisable(true);
            });

            btnArticles.setOnAction(event -> {
                type = "articles";
                articlePageNum = 1;
                parser.setPage("http://itc.ua/articles/", "div.type-articles");
                showNewsTitles();
                btnPrev.setDisable(true);
            });

            notebookCategory.setOnAction(event -> {
                type = "notebook";
                notebookPageNum = 1;
                parser.setPage("http://itc.ua/tag/notebook/", "tag-notebook");
                showNewsTitles();
                btnPrev.setDisable(true);
            });

            smartfonCategory.setOnAction(event -> {
                type = "smartfon";
                smartfonPageNum = 1;
                parser.setPage("http://itc.ua/tag/smartfoni/", "tag-smartfoni");
                showNewsTitles();
                btnPrev.setDisable(true);
            });

            btnNext.setOnAction(event -> {
                if (type.equals("main")) {
                    parser.setPage("http://itc.ua/page/" + ++mainPageNum + "/", "div.type-post, div.type-articles");
                    showNewsTitles();
                }
                if (type.equals("news")) {
                    parser.setPage("http://itc.ua/news/page/" + ++newsPageNum + "/", "div.type-post");
                    showNewsTitles();
                }
                if (type.equals("articles")) {
                    parser.setPage("http://itc.ua/articles/page/" + ++articlePageNum + "/", "div.type-articles");
                    showNewsTitles();
                }
                if(type.equals("notebook")) {
                    parser.setPage("http://itc.ua/tag/notebook/page/" + ++notebookPageNum + "/", "tag-notebook");
                    showNewsTitles();
                }

                if(type.equals("smartfon")) {
                    parser.setPage("http://itc.ua/tag/smartfoni/page/" + ++smartfonPageNum + "/", "tag-smartfoni");
                    showNewsTitles();
                }

                btnPrev.setDisable(false);
            });


            btnUp.setOnAction(event -> {
                listView.scrollTo(0);
                btnUp.setVisible(false);
            });

            listView.setOnScrollTo(event -> {
                for (Node node : listView.lookupAll(".scroll-bar")) {
                    if (node instanceof ScrollBar) {
                        final ScrollBar bar = (ScrollBar) node;
                        bar.valueProperty().addListener((value, oldValue, newValue) -> {
                            Double pos = newValue.doubleValue() * 100;
                            if (pos.intValue() >= 5) btnUp.setVisible(true);
                            if (pos.intValue() < 5) btnUp.setVisible(false);
                        });
                    }
                }
            });

            btnPrev.setOnAction(event -> {
                if (type.equals("main")) {
                    mainPageNum--;
                    if (mainPageNum == 1) {
                        parser.setPage("http://itc.ua/", "div.type-post, div.type-articles");
                        showNewsTitles();
                        btnPrev.setDisable(true);
                    } else {
                        parser.setPage("http://itc.ua/page/" + mainPageNum + "/", "div.type-post, div.type-articles");
                        showNewsTitles();
                    }
                }

                if (type.equals("news")) {
                    newsPageNum--;
                    if (newsPageNum == 1) {
                        parser.setPage("http://itc.ua/news/", "div.type-post");
                        showNewsTitles();
                        btnPrev.setDisable(true);
                    } else {
                        parser.setPage("http://itc.ua/news/page/" + newsPageNum + "/", "div.type-post");
                        showNewsTitles();
                    }
                }

                if (type.equals("articles")) {
                    articlePageNum--;
                    if (articlePageNum == 1) {
                        parser.setPage("http://itc.ua/articles/", "div.type-articles");
                        showNewsTitles();
                        btnPrev.setDisable(true);
                    } else {
                        parser.setPage("http://itc.ua/articles/page/" + articlePageNum + "/", "div.type-articles");
                        showNewsTitles();
                    }
                }

                if (type.equals("notebook")) {
                    notebookPageNum--;
                    if (notebookPageNum == 1) {
                        parser.setPage("http://itc.ua/tag/notebook/", "tag-notebook");
                        showNewsTitles();
                        btnPrev.setDisable(true);
                    } else {
                        parser.setPage("http://itc.ua/tag/notebook/page/" + notebookPageNum + "/", "tag-notebook");
                        showNewsTitles();
                    }
                }

                if (type.equals("smartfon")) {
                    smartfonPageNum--;
                    if (smartfonPageNum == 1) {
                        parser.setPage("http://itc.ua/tag/smartfoni/", "tag-smartfoni");
                        showNewsTitles();
                        btnPrev.setDisable(true);
                    } else {
                        parser.setPage("http://itc.ua/tag/smartfoni/page/" + smartfonPageNum + "/", "tag-smartfoni");
                        showNewsTitles();
                    }
                }
            });
        }
        else {
            btnPrev.setDisable(true);
            btnNext.setDisable(true);
            btnArticles.setDisable(true);
            btnNews.setDisable(true);
            menuBtnCategory.setDisable(true);
            menuBtnBookmarks.setDisable(true);
            showNewsOfflineTitles();
        }

        if(newsItems.size() > 0) {
            listView.setOnMouseClicked(event -> {
                currentNewsItem = listView.getFocusModel().getFocusedItem();
                NewsStage newsStage = new NewsStage(currentNewsItem);
                newsStage.start();
            });
        }
    }

    /**
     * Called to initialize and show news in online mode
     **/
    private void showNewsTitles() {
        newsItems = FXCollections.observableList(parser.getNewsTitles());
        listView.setItems(newsItems);
        listView.setCellFactory(param -> new MainListCell());
        listView.scrollTo(0);
    }

    /**
     * Called to initialize and show news in offline mode.
     * News are loaded from database.
     **/
    private void showNewsOfflineTitles() {
        newsItems = FXCollections.observableList(DBOperations.getInstance().getNews());
        if(newsItems.size() > 0) {
            listView.setItems(newsItems);
            listView.setCellFactory(param -> new MainListCell());
            listView.scrollTo(0);
        }
    }

}
