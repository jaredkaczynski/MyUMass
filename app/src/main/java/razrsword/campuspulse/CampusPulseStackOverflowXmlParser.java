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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by razrs on 03-Jan-16.
 */
public class CampusPulseStackOverflowXmlParser {

    private List<Entry> entries = new ArrayList<>();
    private String text;

    public List<Entry> parse(InputStream is, int maxEntries) {
        try {
            Entry entry = null;

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT && entries.size() < maxEntries) {
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
                                Log.v("enclosureTag", "Log before url is added" + parser.getAttributeValue(null, "url"));
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("item")) {
                            //Log.v("Adding entry", " Entry Added to list");
                            // add entry object to list
                            entries.add(entry);
                        } else if (tagname.equalsIgnoreCase("title")) {

                            if (entry != null) {
                                entry.setTitle(text);
                                Log.v("setting title", " Entry Created for storage");
                            }
                        } else if (tagname.equalsIgnoreCase("link")) {
                            if (entry != null)
                                entry.setEventLink(text);
                        } else if (tagname.equalsIgnoreCase("description")) {
                            if (entry != null) {
                                entry.setTextField(text);
                                entry.extractDate(text);
                                entry.extractDescription(text);
                                entry.extractLocation(text);
                                Log.v("HTML", entry.description + " date start");
                            }
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
        private String title;
        private String textField;
        private long dateStart;
        private long dateEnd;
        private String eventLink;
        private String imageLink;
        private String description;
        private String eventLocation;

        protected Entry(Parcel in) {
            title = in.readString();
            textField = in.readString();
            dateStart = in.readLong();
            dateEnd = in.readLong();
            eventLink = in.readString();
            imageLink = in.readString();
            description = in.readString();
            eventLocation = in.readString();
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
        }

        public void setImageLink(String imageLink1) {
            this.imageLink = imageLink1;
        }

        public String getImageLink() {
            return this.imageLink;
        }

        public String getEventLink() {
            return this.eventLink;
        }

        public void setEventLink(String eventLink) {
            this.eventLink = eventLink;
        }

        public long getDateStart() {
            return this.dateStart;
        }

        public String getEventLocation() {
            return this.eventLocation;
        }
        public String getDescription(){
            return this.description;
        }

        public long getDateEnd() {
            return this.dateEnd;
        }

        /**
         *
         * @param description
         * @return
         * I convert the date and time to a time in seconds since epoch to keep consistent data and avoid difficult conversions
         */
        public String extractDate(String description) {
            //Spanned ss = Html.fromHtml(Html.fromHtml(textField).toString());
            //String html = ss.toString();
            String start;
            String end;
            Document doc = Jsoup.parse(description);
            Elements dateStart = doc.select("span.dtstart");
            Elements dateEnd = doc.select("span.dtend");
            start = dateStart.get(0).text();
            start = start.replaceAll("[()]", "");
            end = dateEnd.get(0).text();
            end = end.replaceAll("[()]", "");
            DateFormat formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy h:mm a", Locale.US);
            try {
                this.dateStart = formatter.parse(start).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(!end.contains(",")){
                Date date = new Date(this.dateStart);
                formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy ");
                end = formatter.format(date) + " " + end;
                formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy h:mm a", Locale.US);
                try {
                    this.dateEnd = formatter.parse(end).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    this.dateEnd = formatter.parse(end).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.v("HTML", this.dateStart + " date start");
            return null;
        }

        public String extractLocation(String description) {
            //Spanned ss = Html.fromHtml(Html.fromHtml(textField).toString());
            //String html = ss.toString();

            Document doc = Jsoup.parse(description);
            Elements descriptionElement = doc.select("span.location");
            this.eventLocation = descriptionElement.get(0).text();
            Log.v("Location", this.eventLocation + " location");
            return null;
        }

        public String extractDescription(String description) {
            //Spanned ss = Html.fromHtml(Html.fromHtml(textField).toString());
            //String html = ss.toString();

            Document doc = Jsoup.parse(description);
            Elements descriptionElement = doc.select("div.description");
            this.description = descriptionElement.get(0).text();
            Log.v("Description", this.description + " description");
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
            dest.writeLong(dateStart);
            dest.writeLong(dateEnd);
            dest.writeString(eventLink);
            dest.writeString(imageLink);
            dest.writeString(description);
            dest.writeString(eventLocation);
        }
    }
}
