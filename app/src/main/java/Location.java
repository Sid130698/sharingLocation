public class Location {
    String Latitude,Longitude,markerName;

    public Location() {
    }

    public Location(String Latitude, String Longitude, String markerName) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.markerName = markerName;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }
}
