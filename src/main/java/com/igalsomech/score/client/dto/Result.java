package com.igalsomech.score.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {

    private String releaseDate;
    private String wrapperType;
    private String version;
    private String kind;
    private String description;

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getWrapperType() {
        return wrapperType;
    }

    public String getVersion() {
        return version;
    }

    public String getKind() {
        return kind;
    }

    public String getDescription() {
        return description;
    }
}
