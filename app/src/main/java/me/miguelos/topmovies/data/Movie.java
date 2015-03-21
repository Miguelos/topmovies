package me.miguelos.topmovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Miguel González Pérez
 * @since 13 Aug 2013 11:12:22
 * <p/>
 * Stores the movie info.
 */
public class Movie {

    private String name = "";
    private String rights = "";
    private String summary = "";
    private String id = "";
    private String imageThumbnail = "";
    private String image = "";

    /**
     * Create the object Movie from a JSon Object
     *
     * @param jsonMovie
     * @throws JSONException
     */
    public Movie(JSONObject jsonMovie) throws JSONException {
        this(
                ((JSONObject) jsonMovie.get("im:name")).get("label").toString(),
                ((JSONObject) jsonMovie.get("rights")).get("label").toString(),
                ((JSONObject) jsonMovie.get("summary")).get("label").toString(),
                ((JSONObject) ((JSONObject) jsonMovie.get("id"))
                        .get("attributes")).get("im:id").toString(),
                ((JSONObject) ((JSONArray) jsonMovie.get("im:image")).get(0))
                        .get("label").toString(),
                ((JSONObject) ((JSONArray) jsonMovie.get("im:image")).get(2))
                        .get("label").toString());
    }

    /**
     * Constructor using fields
     *
     * @param name
     * @param rights
     * @param summary
     * @param id
     * @param imageThumbnail
     * @param image
     */
    public Movie(String name, String rights, String summary, String id,
                 String imageThumbnail, String image) {
        this.name = name;
        this.rights = rights;
        this.summary = summary;
        this.id = id;
        this.imageThumbnail = imageThumbnail;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public String getName() {
        return name;
    }

    public String getRights() {
        return rights;
    }

    public String getSummary() {
        return summary;
    }

}
