package ca.ubc.ubyssey.models;

import java.io.Serializable;

/**
 * Created by Chris Li on 7/27/2015.
 */
public class Trending implements Serializable {

    public int count;
    public String next;
    public String previous;
    public TrendingItem[] results;

    public class TrendingItem implements Serializable {

        public String handle;
        public String name;
        public String url;
        public String timestamp;
        public String image;
        public String content;
        public String source;
        public String title;

    }

}
