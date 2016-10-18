package WHS_planner.News.model;

import org.jsoup.Jsoup;

public class FeedMessage {

    private String title;
    private String description;
    private String link;
    private String author;
    private String guid;

    public String getTitle() {
        escapeHTML();
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        escapeHTML();
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * Changes all normally unprintable characters (Such as "...")
     * into their respective appropriate characters in
     * the title and description. This is used to remove
     * the HTML codes in the RSS feed's description and title.
     */
    private void escapeHTML() {
        title = Jsoup.parse(title).text();
        description = Jsoup.parse(description).text();
    }

    @Override
    public String toString() {
        escapeHTML();
        return "FeedMessage [title=" + title + ", description=" + description
                + ", link=" + link + ", author=" + author + ", guid=" + guid
                + "]";
    }
}