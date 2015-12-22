package razrsword.mapping;

/**
 * Created by razrs on 21-Dec-15.
 */

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class XMLPullParseHandler {
    private List<Place> places = new ArrayList<>();
    private Place place;
    private String text;

    public List<Place> getPlace() {
        return places;
    }

    public List<Place> parse(InputStream is) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser  parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("place")) {
                            // create a new instance of employee
                            place = new Place();

                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("place")) {
                            // add employee object to list
                            places.add(place);
                        }else if (tagname.equalsIgnoreCase("name")) {
                            place.setName(text);
                        }  else if (tagname.equalsIgnoreCase("latitude")) {
                            place.setLatitude(text);
                        } else if (tagname.equalsIgnoreCase("longitude")) {
                            place.setLongitude(text);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException | IOException e) {e.printStackTrace();}
        Log.d("UMassApp", "Places size " + places.size() + " " + places.get(0).getName());
        return places;
    }
}