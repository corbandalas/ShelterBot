
package com.corbandalas.shelterbot.geocode.yandex.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Point",
    "request",
    "results",
    "found"
})
@Generated("jsonschema2pojo")
public class GeocoderResponseMetaData {

    @JsonProperty("Point")
    private Point point;
    @JsonProperty("request")
    private String request;
    @JsonProperty("results")
    private String results;
    @JsonProperty("found")
    private String found;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Point")
    public Point getPoint() {
        return point;
    }

    @JsonProperty("Point")
    public void setPoint(Point point) {
        this.point = point;
    }

    @JsonProperty("request")
    public String getRequest() {
        return request;
    }

    @JsonProperty("request")
    public void setRequest(String request) {
        this.request = request;
    }

    @JsonProperty("results")
    public String getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(String results) {
        this.results = results;
    }

    @JsonProperty("found")
    public String getFound() {
        return found;
    }

    @JsonProperty("found")
    public void setFound(String found) {
        this.found = found;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
