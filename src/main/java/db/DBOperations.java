package db;

import entity.NewsDetails;
import entity.NewsEntity;
import entity.NewsItem;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Provide method to operate with database.
 */
public class DBOperations {

    private static final Logger LOG = Logger.getLogger(DBOperations.class.getName());
    
    private static DBOperations instance;

    private static Connection con = null;

    public static DBOperations getInstance() {
        if (instance == null)
            instance = new DBOperations();
        return instance;
    }

    private DBOperations() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/itc_parser_db";
            con = DriverManager.getConnection(url, "postgres", "postgres");
        } catch (ClassNotFoundException | SQLException e) {
            LOG.warning(e.getMessage());
        }
    }

    /**
     * Add particular news to database.
     */
    public void addNews(NewsItem newsItem) {

        PreparedStatement prst;

        for (NewsDetails d : newsItem.getNewsDetails()) {
            Image image = d.getImage();
            String text = d.getText();
            ByteArrayOutputStream baos = null;

            if (image != null) {

                baos = new ByteArrayOutputStream();
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(d.getImage(), null);

                try {
                    ImageIO.write(bufferedImage, "jpg", baos);
                } catch (IOException e) {
                    LOG.warning(e.getMessage());
                }
            }
            try {
                prst = con.prepareStatement("INSERT INTO news_details (url,news_text,image,idx) VALUES(?,?,?,?)");
                prst.setString(1, d.getUrl());
                if (text != null)
                    prst.setString(2, d.getText());
                else
                    prst.setString(2, null);
                if (image != null)
                    prst.setBytes(3, baos.toByteArray());
                else
                    prst.setBytes(3, null);
                prst.setInt(4, d.getIdx());
                prst.execute();
            } catch (SQLException e) {
                LOG.warning(e.getMessage());
            }
        }

        PreparedStatement st = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(newsItem.getImage(), null);

        try {
            ImageIO.write(bufferedImage, "jpg", baos);
        } catch (IOException e) {
            LOG.warning(e.getMessage());
        }

        try {
            st = con.prepareStatement("INSERT INTO news (entry_title,entry_excerpt,news_date,url,title_image) VALUES(?,?,?,?,?)");
            st.setString(1, newsItem.getEntryTitle());
            st.setString(2, newsItem.getEntryExcerpt());
            st.setString(3, newsItem.getDate());
            st.setString(4, newsItem.getHref());
            st.setBytes(5, baos.toByteArray());
            st.execute();
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException e) {
                LOG.warning(e.getMessage());
            }
        }
    }

    /**
     * Check if news is already stored in DB.
     */
    public boolean isNewsStored(NewsItem newsItem) {

        PreparedStatement st;
        ResultSet rs;

        try {
            st = con.prepareStatement("SELECT url FROM news WHERE url = ?");
            st.setString(1, newsItem.getHref());
            rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
        }
        return false;
    }

    /**
     * Get all news from DB.
     */
    public List<NewsItem> getNews() {

        List<NewsItem> newsItems = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT entry_title,entry_excerpt,news_date,url,title_image FROM news");

            while (rs.next()) {
                NewsItem newsItem = new NewsItem();
                newsItem.setEntryTitle(rs.getString(1));
                newsItem.setEntryExcerpt(rs.getString(2));
                newsItem.setDate(rs.getString(3));
                newsItem.setHref(rs.getString(4));
                ByteArrayInputStream baos = new ByteArrayInputStream(rs.getBytes(5));
                try {
                    convertImage(newsItem, baos);
                } catch (IOException e) {
                    LOG.warning(e.getMessage());
                }
                newsItem.setNewsDetails(getNewsDetails(newsItem.getHref()));
                newsItems.add(newsItem);
            }
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
        } finally {
            closeResources(st, rs);
        }

        return newsItems;
    }

    /**
     * Get content of particular news by its URL
     *
     * @param url
     */
    public List<NewsDetails> getNewsDetails(String url) {

        List<NewsDetails> newsDetails = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = con.prepareStatement("SELECT news_text,image,idx FROM news_details WHERE url = ?");
            st.setString(1, url);
            rs = st.executeQuery();

            while (rs.next()) {
                NewsDetails newsDetail = new NewsDetails();
                newsDetail.setText(rs.getString(1));
                newsDetail.setIdx(rs.getInt(3));

                ByteArrayInputStream baos = null;
                if (rs.getBytes(2) != null) {
                    baos = new ByteArrayInputStream(rs.getBytes(2));
                    try {
                        convertImage(newsDetail, baos);
                    } catch (IOException e) {
                        LOG.warning(e.getMessage());
                    }
                } else newsDetail.setImage(null);
                newsDetails.add(newsDetail);
            }
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
        } finally {
            closeResources(st, rs);
        }
        return newsDetails;
    }

    private void closeResources(Statement st, ResultSet rs) {
        try {
            if (st != null) st.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
        }
    }

    private void convertImage(NewsEntity newsEntity, ByteArrayInputStream baos) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(baos);
        BufferedImage bufferedImage = ImageIO.read(iis);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        newsEntity.setImage(image);
    }

}
