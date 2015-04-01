package ca.ubc.ubyssey.models;

/**
 * Created by Chris Li on 3/28/2015.
 */
public class DrawerItem {

    private boolean isSection = false;
    private String mTitle;
    private int mTag;

    public DrawerItem(String title, int tag){
        this.mTitle = title;
        this.mTag = tag;
    }

    public DrawerItem(boolean isSection, String title){
        this.isSection = isSection;
        this.mTitle = title;
    }

    public int getTag() {
        return mTag;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isSection(){
        return isSection;
    }
}
