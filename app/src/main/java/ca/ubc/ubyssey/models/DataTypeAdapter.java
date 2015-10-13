package ca.ubc.ubyssey.models;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Custom type adapter for parsing the data object since the
 * "data" field type is inconsistent and can either be a
 * String, a Json array or a Json object.
 *
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
            data.paragraph = in.nextString();
        } else if (token.equals(JsonToken.BEGIN_OBJECT)) {
            in.beginObject();
            while (!in.peek().equals(JsonToken.END_OBJECT)) {
                if (in.peek().equals(JsonToken.NAME)) {
                    String name = in.nextName();
                    try {
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
                    } catch (IllegalStateException e) {
                        in.skipValue();
                    }
                }
            }
            in.endObject();
        } else if (token.equals(JsonToken.NULL)) {
            in.nextNull();
            return null;
        } else if (token.equals(JsonToken.BEGIN_ARRAY)) {
            in.beginArray();
            data.list = new ArrayList<String>();
            while (!in.peek().equals(JsonToken.END_ARRAY)) {
                String entry = in.nextString();
                data.list.add(entry);
            }
            in.endArray();
        }

        return data;
    }
}