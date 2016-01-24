package razrsword.dining;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by razrs on 23-Dec-15.
 */
public class DiningLocation implements Parcelable{
    String locationName;
    String openTime;
    String closeTime;
    int imageID;


    public DiningLocation(String name, int id){
        locationName = name;
        imageID = id;
    }

    protected DiningLocation(Parcel in) {
        locationName = in.readString();
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

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationName);
    }
}
