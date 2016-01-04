package razrsword.campuspulse;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import razrsword.main.R;

/**
 * Created by razrs on 03-Jan-16.
 */
public class EventCard implements Parcelable {
    String eventTitle;
    String eventDate;
    String eventDescription;
    String eventImageURL;



    public EventCard(String name, String date, String description, String event){
        eventTitle = name;
        eventDate = date;
        eventDescription = description;
        eventImageURL = event;
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
