package ca.ubc.ubyssey.models;

import java.io.Serializable;

/**
 * Image model object
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class Image implements Serializable {

    public int id;
    public String title;
    public Author[] authors;
    public String url;
    public String thumb;
    public String created_at;
}
