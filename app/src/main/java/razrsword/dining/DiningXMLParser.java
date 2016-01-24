package razrsword.dining;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by razrs on 24-Jan-16.
 */
public class DiningXMLParser {

    private List<DiningLocation> diningLocations = new ArrayList<>();
    private String text;

    public List<DiningLocation> parse(InputStream is, int maxEntries) throws XmlPullParserException {
        try {
            DiningLocation diningLocation = null;

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT && diningLocations.size() < maxEntries) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("dining_location")) {
                            // create a new instance of item diningLocation
                            diningLocation = new DiningLocation();
                            Log.v("Creating diningLocation", " Entry Created for storage");
                        } else if (tagname.equalsIgnoreCase("name")) {
                            if (diningLocation != null) {
                                diningLocation.setLocationName(text);
                                Log.v("setting locationName", " Entry Created for storage");
                            }
                        } else if (tagname.equalsIgnoreCase("image")) {
                            if (diningLocation != null)
                                diningLocation.setimageResourceID(Integer.valueOf(text));

                        } else if (tagname.equalsIgnoreCase("uuid")) {
                            if (diningLocation != null)
                                diningLocation.setUUID(Integer.valueOf(text));
                        } else if (tagname.equalsIgnoreCase("description")) {
                            //if (diningLocation != null) {
                            //diningLocation.setTextField(text);
                            //Log.v("HTML", diningLocation.description + " date start");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("dining_location")) {
                            diningLocations.add(diningLocation);
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

        return diningLocations;
    }

    public List<DiningLocation> getDiningLocations() {
        return diningLocations;
    }

    public static class DiningLocation implements Parcelable {
        private String locationName;
        private int imageResourceID;
        private int UUID;

        protected DiningLocation(Parcel in) {
            locationName = in.readString();
            imageResourceID = in.readInt();
            UUID = in.readInt();
        }

        public static final Creator<DiningLocation> CREATOR = new Creator<DiningLocation>() {
            @Override
            public DiningLocation createFromParcel(Parcel in) {
                return new DiningLocation(in);
            }

            @Override
            public DiningLocation[] newArray(int size) {
                return new DiningLocation[size];
            }
        };

        public DiningLocation() {
        }

        public String getLocationName() {
            return locationName;
        }
        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }
        public int getimageResourceID() {
            return imageResourceID;
        }
        public void setimageResourceID(int imageResourceID) {
            this.imageResourceID = imageResourceID;
        }
        public int getUUID() {
            return UUID;
        }
        public void setUUID(int uuid) {
            this.UUID = uuid;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(locationName);
            dest.writeInt(imageResourceID);
            dest.writeInt(UUID);
        }
    }
}
