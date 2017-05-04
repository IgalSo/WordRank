package com.igalsomech.score.client.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response implements Serializable {

    private final List<Result> results = new ArrayList<>();

    public List<Result> getResults() {
        return results;
    }
}
