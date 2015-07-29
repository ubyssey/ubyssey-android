package ca.ubc.ubyssey.models;

import java.io.Serializable;

/**
 * Created by Chris Li on 7/29/2015.
 */
public class Topics implements Serializable {

    public int count;
    public String previous;
    public String next;
    public Topic[] results;


    public class Topic implements Serializable {

        public int id;
        public String name;

    }

}
