package ca.ubc.ubyssey.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chris Li on 7/29/2015.
 */
public class Topics implements Serializable {

    public int count;
    public String previous;
    public String next;
    public List<Topic> results;


    public class Topic implements Serializable {

        public int id;
        public String name;

    }

}
