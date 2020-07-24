package com.example.sharinglocation;

public class userLocation {
    String Latitude;
    String Longitude;
    String markerName;
    public userLocation() {
    }



    public userLocation(String latitude, String longitude, String markerName) {
        Latitude = latitude;
        Longitude = longitude;
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
