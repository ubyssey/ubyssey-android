package ca.ubc.ubyssey.models;

import java.io.Serializable;

/**
 * Articles model object
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class Articles implements Serializable {

    public int count;
    public String next;
    public String previous;
    public Article[] results;

    public void setupNextArticles(){

        if (results != null) {
            if (results.length > 1) {
                for (int i = 0; i < results.length; i++) {
                    if (i + 1 < results.length) {
                        results[i].setNextArticle(results[i + 1]);
                    }
                }
            }
        }

    }

    public class Article implements Serializable {

        public int id;
        public int parent;
        public String long_headline;
        public String short_headline;
        public FeaturedImage featured_image;
        public Content[] content;
        public Author[] authors;
        public Section section;
        public String published_at;
        public int importance;
        public String slug;
        public int revision_id;
        public String url;

        private Article nextArticle = null;

        public Article getNextArticle() {
            return nextArticle;
        }

        public void setNextArticle(Article nextArticle) {
            this.nextArticle = nextArticle;
        }


        public class FeaturedImage implements Serializable {

            public int id;
            public String url;
            public String caption;
            public String normal;
            public int width;
            public int height;
        }

        public class Content implements Serializable {

            public Data data;
            public String type;

        }

        public class Author implements Serializable {

            public int id;
            public String full_name;
            public String first_name;
            public String last_name;
        }

        public class Section implements Serializable {

            public int id;
            public String name;
            public String slug;
        }

    }

}
