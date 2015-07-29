package ca.ubc.ubyssey.models;

/**
 * Created by Chris Li on 3/28/2015.
 */
public class DrawerItem {

    private boolean isSection = false;
    private boolean isTopic = false;
    private String mTitle;
    private int mTag;
    private int mId;

    public DrawerItem(String title, int tag) {
        this.mTitle = title;
        this.mTag = tag;
        this.isTopic = false;
        this.isSection = false;
    }

    public DrawerItem(boolean isSection, String title) {
        this.isSection = isSection;
        this.mTitle = title;
        this.isTopic = false;
        this.isTopic = false;
    }

    public DrawerItem(boolean isTopic, String title, int id) {
        this.isTopic = isTopic;
        this.mTitle = title;
        this.mId = id;
        this.isSection = false;
    }

    public int getTag() {
        return mTag;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isSection() {
        return isSection;
    }

    public boolean isTopic() {
        return isTopic;
    }

    public int getId() {
        return mId;
    }
}
