package razrsword.campuspulse;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.drive.query.internal.FullTextSearchFilter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
public class CampusPulseStackOverflowXmlParser {

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
                        } else if (tagname.equalsIgnoreCase("enclosure")) {
                            if (entry != null) {
                                entry.setImageLink(parser.getAttributeValue(null, "url"));
                                Log.v("enclosureTag", "Log before url is added" + parser.getAttributeValue(null, "url") );
                            }
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
                        } else if (tagname.equalsIgnoreCase("eventLink")) {
                            if (entry != null)
                                entry.setEventLink(text);
                        } else if (tagname.equalsIgnoreCase("textField")) {
                            if (entry != null)
                                entry.setTextField(text);
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

    public static class Entry implements Parcelable {
        public String title;
        public String textField;
        public String dateStart;
        public String dateEnd;
        public String eventLink;
        public String imageLink;
        public String description;

        protected Entry(Parcel in) {
            title = in.readString();
            textField = in.readString();
            dateStart = in.readString();
            dateEnd = in.readString();
            eventLink = in.readString();
            imageLink = in.readString();
            description = in.readString();
        }

        public static final Creator<Entry> CREATOR = new Creator<Entry>() {
            @Override
            public Entry createFromParcel(Parcel in) {
                return new Entry(in);
            }

            @Override
            public Entry[] newArray(int size) {
                return new Entry[size];
            }
        };

        public Entry() {

        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTextField() {
            return textField;
        }

        public void setTextField(String textField) {
            this.textField = textField;
            this.extractDate(this.textField);
            this.extractDescription(this.textField);
        }
        public void setImageLink(String imageLink1) {
            this.imageLink = imageLink1;
        }

        public String getEventLink() {
            return eventLink;
        }
        public void setEventLink(String eventLink) {
            this.eventLink = eventLink;
        }
        public String getDateStart() {
            return dateStart;
        }
        public String getDateEnd() {
            return dateEnd;
        }
        public String extractDate(String description){
            //Spanned ss = Html.fromHtml(Html.fromHtml(textField).toString());
            //String html = ss.toString();

            Document doc = Jsoup.parse(description);
            Elements dateStart = doc.select("span.dtstart");
            Elements dateEnd = doc.select("span.dtend");
            this.dateStart = dateStart.get(0).text();
            this.dateEnd = dateEnd.get(0).text();
            Log.v("HTML", this.dateStart);
            return null;
        }
        public String extractDescription(String description){
            //Spanned ss = Html.fromHtml(Html.fromHtml(textField).toString());
            //String html = ss.toString();

            Document doc = Jsoup.parse(description);
            Elements descriptionElement = doc.select("div.description");
            this.description = descriptionElement.get(0).text();
            Log.v("Description", this.description);
            return null;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(textField);
            dest.writeString(dateStart);
            dest.writeString(dateEnd);
            dest.writeString(eventLink);
            dest.writeString(imageLink);
            dest.writeString(description);
        }
    }
}
