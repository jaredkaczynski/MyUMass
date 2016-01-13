package razrsword.campuspulse;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by razrs on 03-Jan-16.
 * This holds the data in the cards in the main view
 */
public class CampusPulseEventCard implements Parcelable {
    String eventTitle;
    String eventDate;
    String eventDescription;
    String eventImageURL;
    String eventLocation;
    int vibrantColor;
    int lightVibrantColor;
    Bitmap mainImage;



    public CampusPulseEventCard(String name, String date, String description, String event,String location, int color, int lightcolor){
        eventTitle = name;
        eventDate = date;
        eventDescription = description;
        eventImageURL = event;
        eventLocation = location;
        vibrantColor = color;
        lightVibrantColor = lightcolor;
    }

    protected CampusPulseEventCard(Parcel in) {
        eventTitle = in.readString();
    }

    public static final Creator<CampusPulseEventCard> CREATOR = new Creator<CampusPulseEventCard>() {
        @Override
        public CampusPulseEventCard createFromParcel(Parcel in) {
            return new CampusPulseEventCard(in);
        }

        @Override
        public CampusPulseEventCard[] newArray(int size) {
            return new CampusPulseEventCard[size];
        }
    };

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventTitle);
    }
}
