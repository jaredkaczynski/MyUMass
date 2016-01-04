package razrsword.campuspulse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by razrs on 03-Jan-16.
 */
public class EventCard implements Parcelable {
    String eventTitle;
    String eventDate;
    String eventDescription;

    int imageID;


    public EventCard(String name, String date, String description){
        eventTitle = name;
        eventDate = date;
        eventDescription = description;
    }

    protected EventCard(Parcel in) {
        eventTitle = in.readString();
    }

    public static final Creator<EventCard> CREATOR = new Creator<EventCard>() {
        @Override
        public EventCard createFromParcel(Parcel in) {
            return new EventCard(in);
        }

        @Override
        public EventCard[] newArray(int size) {
            return new EventCard[size];
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
