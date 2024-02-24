package tz.co.fishhappy.app.model;

/**
 * Created by Simon on 03-May-17.
 */

public class SettingsModel {

    private String title;
    private int icon;

    public SettingsModel(){}

    public SettingsModel(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
