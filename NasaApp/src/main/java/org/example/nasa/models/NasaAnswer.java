package org.example.nasa.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NasaAnswer {
    String copyright;
    String date;
    String explanation;
    String hdUrl;
    String mediaType;
    String serviceVersion;
    String title;
    String url;

    public NasaAnswer(@JsonProperty("copyright") String copyright,
                      @JsonProperty("date") String date,
                      @JsonProperty("explanation") String explanation,
                      @JsonProperty("hdurl") String hdurl,
                      @JsonProperty("media_type") String media_type,
                      @JsonProperty("service_version") String service_version,
                      @JsonProperty("title") String title,
                      @JsonProperty("url") String url) {
        this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.hdUrl = hdurl;
        this.mediaType = media_type;
        this.serviceVersion = service_version;
        this.title = title;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getPictureName() {
        return getFileNameFromUrl(url);
    }

    private String getFileNameFromUrl(String url) {
        if (url != null && !url.trim().isEmpty())
            return url.substring(url.lastIndexOf("/") + 1);

        return "";
    }
}
