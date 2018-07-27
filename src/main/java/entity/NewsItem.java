package entity;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class NewsItem extends NewsEntity {

    private String entryTitle;

    private String entryExcerpt;

    private String date;

    private String href;

    private List<NewsDetails> newsDetails = new ArrayList<>();

    public String getEntryTitle() {
        return entryTitle;
    }

    public void setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
    }

    public String getEntryExcerpt() {
        return entryExcerpt;
    }

    public void setEntryExcerpt(String entryExcerpt) {
        this.entryExcerpt = entryExcerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<NewsDetails> getNewsDetails() {
        return newsDetails;
    }

    public void setNewsDetails(List<NewsDetails> newsDetails) {
        this.newsDetails = newsDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsItem item = (NewsItem) o;

        if (entryTitle != null ? !entryTitle.equals(item.entryTitle) : item.entryTitle != null) return false;
        return href != null ? href.equals(item.href) : item.href == null;

    }

    @Override
    public int hashCode() {
        int result = entryTitle != null ? entryTitle.hashCode() : 0;
        result = 31 * result + (href != null ? href.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return entryTitle;
    }
}
