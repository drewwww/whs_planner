package WHS_planner.News.tests;

import WHS_planner.News.model.Feed;
import WHS_planner.News.model.FeedMessage;
import WHS_planner.News.read.RSSFeedParser;

public class ReadTest {
    public static void main(String[] args) {
        RSSFeedParser parser = new RSSFeedParser("http://waylandstudentpress.com/feed/");
        Feed feed = parser.readFeed();
//        System.out.println(feed);


        for (FeedMessage message : feed.getMessages()) {
            System.out.println(message.getTitle());
            System.out.print(" " + message.getDescription());
        }
    }
}