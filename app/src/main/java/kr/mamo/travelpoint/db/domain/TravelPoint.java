package kr.mamo.travelpoint.db.domain;

/**
 * Created by alucard on 2015-07-17.
 */
public class TravelPoint {
    private int no;
    private String name;

    public TravelPoint(int no, String name) {
        this.no = no;
        this.name = name;
    }
    public String getName() {
        return name;
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
