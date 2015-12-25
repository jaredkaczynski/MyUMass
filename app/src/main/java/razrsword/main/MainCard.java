package razrsword.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by razrs on 24-Dec-15.
 */
public class MainCard implements Parcelable {
    String locationName;
    int imageID;


    public MainCard(String name, int id){
        locationName = name;
        imageID = id;
    }

    protected MainCard(Parcel in) {
        locationName = in.readString();
    }

    public static final Creator<MainCard> CREATOR = new Creator<MainCard>() {
        @Override
        public MainCard createFromParcel(Parcel in) {
            return new MainCard(in);
        }

        @Override
        public MainCard[] newArray(int size) {
            return new MainCard[size];
        }
    };

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationName);
    }
}