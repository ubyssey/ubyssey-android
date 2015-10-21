package ca.ubc.ubyssey.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chris Li on 10/20/2015.
 */
public class Galleries implements Serializable {

    public int count;
    public String next;
    public String previous;
    public List<Gallery> results;

    public class Gallery implements Serializable {

        public int id;
        public String title;
        public List<Image> images;

    }

    public class Image implements Serializable {

        public int id;
        public int attachment_id;
        public String url;
        public String thumb;
        public String caption;
        public String credit;
        public String custom_credit;
        public String type;
        public int width;
        public int height;

    }

}
