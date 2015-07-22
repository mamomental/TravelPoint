package kr.mamo.travelpoint.constant;

import android.content.res.Resources;

import kr.mamo.travelpoint.R;

/**
 * Created by alucard on 2015-07-21.
 */
public enum Settings {
    ACCOUNT(R.id.action_settings, R.string.activity_settings_item_account);
    private int menuId;
    private int stringId;

    Settings(int menuId, int stringId) {
        this.menuId = menuId;
        this.stringId = stringId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getStringId() {
        return stringId;
    }

    public void setStringId(int stringId) {
        this.stringId = stringId;
    }

    public String getName(Resources r){
        return r.getString(stringId);
    }


}
