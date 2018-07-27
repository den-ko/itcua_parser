package parser;

import entity.NewsDetails;
import entity.NewsItem;
import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Contains main API to parse information from ITC.ua
 * */
public class ITCParser {

    private Document document;

    private String type = "div.type-post, div.type-articles";

    private boolean available = true;

    private static ITCParser instance;

    public static ITCParser getInstance() {
        if(instance == null)
            instance = new ITCParser();
        return instance;
    }

    private ITCParser() {
        try {
            document = Jsoup.connect("http://itc.ua/").get();
        } catch (IOException e) {
            available = false;
        }
    }

    /**
     * Check the internet connection. If not available then use offline mode.
     */
    public boolean isAvailable() {
        return available;
    }

    public void setPage(String url, String type) {
        if (url != null || "".equals(url))
            try {
                document = Jsoup.connect(url).get();
                this.type = type;
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * Parse the news headers and info about it.
     */
    public List<NewsItem> getNewsTitles() {
        List<NewsItem> newsItems = new ArrayList<>(24);
        if(type.equals("tag-notebook")) {
                Elements elements = document.select("body.tag-notebook > div.container > section#wrapper > div.container > div.row > main#content > div.type-articles, div.type-post > div.row");
                for (Element e : elements) {
                    NewsItem item = new NewsItem();
                    item.setEntryTitle(e.select("div.col-txt > h2.entry-title > a").first().text());
                    item.setHref(e.select("div.col-txt > h2.entry-title > a").first().attr("href"));
                    item.setEntryExcerpt(e.select("div.col-txt > div.entry-excerpt").first().text());
                    item.setImage(new Image(e.select("div.col-img > div.col-img-in > a").attr("data-original")));
                    item.setDate(e.select("div.col-txt > div.entry-header > div > span.date").first().text());
                    newsItems.add(item);
                }
        } else if(type.equals("tag-smartfoni")) {
            Elements elements = document.select("body.tag-smartfoni > div.container > section#wrapper > div.container > div.row > main#content > div.type-articles, div.type-post > div.row");
            for (Element e : elements) {
                NewsItem item = new NewsItem();
                item.setEntryTitle(e.select("div.col-txt > h2.entry-title > a").first().text());
                item.setHref(e.select("div.col-txt > h2.entry-title > a").first().attr("href"));
                item.setEntryExcerpt(e.select("div.col-txt > div.entry-excerpt").first().text());
                item.setImage(new Image(e.select("div.col-img > div.col-img-in > a").attr("data-original")));
                item.setDate(e.select("div.col-txt > div.entry-header > div > span.date").first().text());
                newsItems.add(item);
            }
        }
        else {
            Elements elements = document.select(type + " > div.row");
            for (Element e : elements) {
                NewsItem item = new NewsItem();
                item.setEntryTitle(e.select("div.col-txt > h2.entry-title > a").first().text());
                item.setHref(e.select("div.col-txt > h2.entry-title > a").first().attr("href"));
                item.setEntryExcerpt(e.select("div.col-txt > div.entry-excerpt").first().text());
                item.setImage(new Image(e.select("div.col-img > div.col-img-in > a").attr("data-bg")));
                item.setDate(e.select("div.col-txt > div.entry-header > div > span.date").first().text());
                newsItems.add(item);
            }
        }
        return newsItems;
    }

    /**
     * Parse a content of the particular news.
     */
    public List<NewsDetails> getNewsDetails(NewsItem newsItem) {
        List<NewsDetails> newsDetails = newsItem.getNewsDetails();
        if (!newsDetails.isEmpty()) newsDetails.clear();
        try {
            document = Jsoup.connect(newsItem.getHref()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = document.select("div.entry-content > div.post-txt > p");
        int idx = 0;
        for (Element e : elements) {
            NewsDetails detail = new NewsDetails();
            detail.setUrl(newsItem.getHref());
            if (e.select("p > img, p > a > img").hasAttr("src")) {
                Image img = new Image(e.select("p > img, p > a > img").attr("src"));
                detail.setIdx(idx++);
                detail.setImage(img);
                newsDetails.add(detail);
            } else {
                detail.setIdx(idx++);
                detail.setText(e.select("p").text());
                newsDetails.add(detail);
            }
        }
        return newsDetails;
    }
}
