package razrsword.campuspulse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by razrs on 03-Jan-16.
 */
public class CampusPulseEventCard implements Parcelable {
    String eventTitle;
    String eventDate;
    String eventDescription;
    String eventImageURL;



    public CampusPulseEventCard(String name, String date, String description, String event){
        eventTitle = name;
        eventDate = date;
        eventDescription = description;
        eventImageURL = event;
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
