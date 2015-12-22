package razrsword.mapping;

/**
 * Created by razrs on 21-Dec-15.
 */
public class Place {
    private String name;
    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getName() {
        return name;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return " name= "+ name + "\n latitude= " + latitude + "\n longitude= " + longitude;
    }
}
