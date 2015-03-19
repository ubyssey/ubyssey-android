package ca.ubc.ubyssey.models;

import java.io.Serializable;

/**
 * Featured Image model object
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class FeaturedImage implements Serializable {

    public int id;
    public Image image;
    public String caption;
    public String type;

}
