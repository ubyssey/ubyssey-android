package ca.ubc.ubyssey.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Articles model object
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class Articles implements Serializable {

    public int count;
    public String next;
    public String previous;
    public ArrayList<Article> results;
}
