package razrsword.campuspulse;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by razrs on 03-Jan-16.
 */
public class StackOverflowXmlParser {

    private List<Entry> entries = new ArrayList<>();
    private Entry entry;
    private String text;

    public List<Entry> parse(InputStream is) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("item")) {
                            // create a new instance of item entry
                            entry = new Entry();
                            Log.v("Creating entry", " Entry Created for storage");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("item")) {
                            Log.v("Adding entry", " Entry Added to list");
                            // add entry object to list
                            entries.add(entry);
                        } else if (tagname.equalsIgnoreCase("title")) {
                            if (entry != null)
                                entry.setTitle(text);
                        } else if (tagname.equalsIgnoreCase("link")) {
                            if (entry != null)
                                entry.setLink(text);
                        } else if (tagname.equalsIgnoreCase("description")) {
                            if (entry != null)
                                entry.setDescription(text);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entries;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public class Entry {
        public String title;
        public String description;
        public String dateStart;
        public String dateEnd;
        public String link;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
            extractDate(description);
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
        public String getDateStart() {
            return dateStart;
        }
        public String getDateEnd() {
            return dateEnd;
        }
        public String extractDate(String description){
            //Spanned ss = Html.fromHtml(Html.fromHtml(description).toString());
            //String html = ss.toString();

            Document doc = Jsoup.parse(description);
            Elements dateStart = doc.select("span.dtstart");
            Elements dateEnd = doc.select("span.dtend");
            this.dateStart = dateStart.get(0).text();
            this.dateEnd = dateEnd.get(0).text();
            Log.v("HTML", this.dateStart);
            return null;
        }

    }
}
