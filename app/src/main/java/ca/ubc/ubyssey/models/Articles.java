package ca.ubc.ubyssey.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public void setupNextArticles() {

        if (results != null) {
            if (results.size() > 1) {
                for (int i = 0; i < results.size(); i++) {
                    if (i + 1 < results.size()) {
                        results.get(i).setNextArticle(results.get(i + 1));
                    }
                }
            }
        }
    }

    public void setPageNumbers(int pageNumber){
        for (Article article : results) {
            article.setPageNumber(pageNumber);
        }
    }

    public class Article implements Serializable {

        public int id;
        public int parent;
        public String headline;
        public String short_headline;
        public FeaturedImage featured_image;
        public List<Content> content;
        public List<Author> authors;
        public Section section;
        public String published_at;
        public int importance;
        public String slug;
        public int revision_id;
        public String url;
        public int est_reading_time;

        private Article nextArticle = null;
        private int pageNumber = 1;
        private String pageUrl;

        public Article getNextArticle() {
            return nextArticle;
        }

        public void setNextArticle(Article nextArticle) {
            this.nextArticle = nextArticle;
        }

        public int getPageNumber(){
            return pageNumber;
        }

        public void setPageNumber(int pageNumber){
            this.pageNumber = pageNumber;
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
