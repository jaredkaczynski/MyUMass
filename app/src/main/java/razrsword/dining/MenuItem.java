package razrsword.dining;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by razrs on 23-Jan-16.
 */
public class MenuItem implements Parcelable{
    String foodName;
    int imageID;


    public MenuItem(String name, int id){
        foodName = name;
        imageID = id;
    }

    protected MenuItem(Parcel in) {
        foodName = in.readString();
    }

    public static final Parcelable.Creator<DiningLocation> CREATOR = new Parcelable.Creator<DiningLocation>() {
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
        dest.writeString(foodName);
    }
}

