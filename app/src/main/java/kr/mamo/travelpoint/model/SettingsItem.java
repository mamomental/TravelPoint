package kr.mamo.travelpoint.model;

/**
 * Created by alucard on 2015-07-21.
 */
public class SettingsItem {
    private int id;
    private String title;

    public SettingsItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
