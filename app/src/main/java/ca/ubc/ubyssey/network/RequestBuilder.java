package ca.ubc.ubyssey.network;

/**
 * Created by Chris Li on 4/18/2015.
 */
public class RequestBuilder {

    public static final String DEV_URL_PREFIX = "http://dev.ubyssey.ca";
    public static final String ROOT_URL = "http://ubyssey.ca/";
    public static final String HOME_URL = "http://ubyssey.ca/api/articles/";
    public static final String CULTURE_URL = "http://ubyssey.ca/api/sections/culture/frontpage/";
    public static final String OPINION_URL = "http://ubyssey.ca/api/sections/opinion/frontpage/";
    public static final String FEATURES_URL = "http://ubyssey.ca/api/sections/features/frontpage/";
    public static final String DATA_URL = "http://ubyssey.ca/api/sections/data/frontpage/";
    public static final String SPORTS_URL = "http://ubyssey.ca/api/sections/sports/frontpage/";
    public static final String VIDEO_URL = "http://ubyssey.ca/api/sections/video/frontpage/";
    public static final String BLOG_URL = "http://ubyssey.ca/api/sections/blog/frontpage/";
    public static final String TOPICS_URL = "http://ubyssey.ca/api/topics/";
    public static final String GALLERIES_URL = "http://ubyssey.ca/api/galleries/";

    public static String getTopicUrl(int topicId){
        return "http://ubyssey.ca/api/topics/" + topicId + "/articles/";
    }

    public static String getSearchUrl(String searchTerm) {
        return "http://ubyssey.ca/api/articles/?q=" + searchTerm;
    }

}
