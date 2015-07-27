package kr.mamo.travelpoint.db.domain;

/**
 * Created by alucard on 2015-07-22.
 */
public class TravelHistory {
    private int no;
    private int userNo;
    private int travelNo;
    private int travelPointNo;
    private String imagePath;
    private double latitude;
    private double longitude;
    private String diary;


    public TravelHistory(int no, int userNo, int travelNo, int travelPointNo, String imagePath, double latitude, double longitude, String diary) {
        this.no = no;
        this.userNo = userNo;
        this.travelNo = travelNo;
        this.travelPointNo = travelPointNo;
        this.imagePath = imagePath;
        this.latitude = latitude;
        this.longitude = longitude;
        this.diary = diary;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getTravelNo() {
        return travelNo;
    }

    public void setTravelNo(int travelNo) {
        this.travelNo = travelNo;
    }

    public int getTravelPointNo() {
        return travelPointNo;
    }

    public void setTravelPointNo(int travelPointNo) {
        this.travelPointNo = travelPointNo;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public String getDiary() {
        return diary;
    }

    public void setDiary(String diary) {
        this.diary = diary;
    }
}
