package kr.mamo.travelpoint.db.domain;

/**
 * Created by alucard on 2015-07-17.
 */
public class TravelPoint {
    private int no;
    private String name;
    private double latitude;
    private double longitude;
    private String description;

    public TravelPoint(int no, String name, double latitude, double longitude, String description) {
        this.no = no;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
