package com.igalsomech.score.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import javax.annotation.PostConstruct;

import com.igalsomech.score.client.dto.Response;
import com.igalsomech.score.client.dto.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class ITunesClient {

    private static final Logger LOG = LogManager.getLogger(ITunesClient.class);
    private static final String ITUNE_LOOKUP_PATH = "https://itunes.apple.com/lookup?id={id}";

    @PostConstruct
    public void init() {
        // Configuring json serialization mapper
        Unirest.setObjectMapper(new ObjectMapper() {

            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public String getDescriptionById(final String iTuneId) {
        String desc = "";
        try {
            HttpResponse<Response> httpResponse = Unirest.get(ITUNE_LOOKUP_PATH).routeParam("id", iTuneId).asObject(Response.class);
            if (httpResponse.getStatus() != HttpURLConnection.HTTP_OK) {
                LOG.warn("Failed to fetch iTuneId: %s from: %s with status code: %s", iTuneId, ITUNE_LOOKUP_PATH, httpResponse.getStatus());
            }
            Response body = httpResponse.getBody();
            List<Result> results = body.getResults();
            if (results.isEmpty()) {
                LOG.debug("Not found any iTune id: {}", iTuneId);
            }

            // Getting the first one (ignoring if there is more than one result)
            desc = results.iterator().next().getDescription();
        } catch (final UnirestException e) {
            LOG.warn("Failed to fetch iTuneId: {} from: {}", iTuneId, ITUNE_LOOKUP_PATH, e);
        }
        return desc;
    }
}
