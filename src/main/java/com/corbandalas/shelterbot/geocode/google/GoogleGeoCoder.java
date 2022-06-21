package com.corbandalas.shelterbot.geocode.google;

import com.corbandalas.shelterbot.geocode.GeoCoder;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.context.env.Environment;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Singleton
@Secondary
public class GoogleGeoCoder implements GeoCoder {

    @Inject
    private Environment environment;

    @Override
    public String geocode(String address) throws Exception {

        URI uri = UriBuilder.of("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("address", address)
                .queryParam("language", "ru")
                .queryParam("key", environment.getProperty("google.geo.key", String.class).orElseThrow())
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        return response.body();
    }

    @Override
    public String reverseGeocode(String longitude, String latitude) throws Exception {
        URI uri = UriBuilder.of("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("latlng", latitude + "," + longitude)
                .queryParam("language", "ru")
                .queryParam("key", environment.getProperty("google.geo.key", String.class).orElseThrow())
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        return response.body();
    }
}
