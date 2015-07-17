package kr.mamo.travelpoint.db.domain;

/**
 * Created by alucard on 2015-07-17.
 */
public class Travel {
    private int no;
    private String name;
    private String description;

    public Travel(int no, String name, String description) {
        this.no = no;
        this.name = name;
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
