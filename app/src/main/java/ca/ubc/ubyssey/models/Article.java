package ca.ubc.ubyssey.models;

import java.io.Serializable;

/**
 * Article model object
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class Article implements Serializable {

    public String long_headline;
    public String short_headline;
    public FeaturedImage featured_image;
    public String content;
    public Author[] authors;
    public String section;
    public String published_at;
    public int importance;
    public String slub;

}
