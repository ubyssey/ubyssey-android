package ca.ubc.ubyssey.models;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Chris Li on 4/30/2015.
 */
public class DataTypeAdapter extends TypeAdapter<Data> {

    @Override
    public void write(JsonWriter out, Data value) throws IOException {

    }

    @Override
    public Data read(JsonReader in) throws IOException {

        JsonToken token = in.peek();
        Data data = new Data();

        if (token.equals(JsonToken.STRING)) {
            String paragraph = in.nextString();
            data.paragraph = paragraph;
        } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
            in.beginObject();
            while (!in.peek().equals(JsonToken.END_OBJECT)) {
                if (in.peek().equals(JsonToken.NAME)) {
                    String name = in.nextName();
                    if (name.equals("width")) {
                        data.width = in.nextInt();
                    } else if (name.equals("height")) {
                        data.height = in.nextInt();
                    } else if (name.equals("url")) {
                        data.url = in.nextString();
                    } else if (name.equals("title")) {
                        data.title = in.nextString();
                    } else if (name.equals("source")) {
                        data.source = in.nextString();
                    } else if (name.equals("credit")) {
                        data.credit = in.nextString();
                    } else if (name.equals("caption")) {
                        data.caption = in.nextString();
                    } else if (name.equals("content")) {
                        data.content = in.nextString();
                    } else if (name.equals("size")) {
                        data.size = in.nextString();
                    } else if (name.equals("id")) {
                        try {
                            data.id = in.nextInt();
                        } catch (NumberFormatException e) {
                            data.videoId = in.nextString();
                        }
                    } else {
                        in.skipValue();
                    }
                }
            }
            in.endObject();
        } else if (token.equals(JsonToken.NULL)) {
            in.nextNull();
            return null;
        }

        return data;
    }
}