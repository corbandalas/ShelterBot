package com.corbandalas.shelterbot.geocode.yandex;

import com.corbandalas.shelterbot.geocode.GeoCoder;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.env.Environment;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Singleton
@Primary
@Slf4j
public class YandexGeoCoder implements GeoCoder {

    @Inject
    private Environment environment;

    @Override
    public String geocode(String address) throws Exception {

        URI uri = UriBuilder.of("https://geocode-maps.yandex.ru/1.x")
                .queryParam("geocode", address)
                .queryParam("apikey", environment.getProperty("yandex.geo.key", String.class).orElseThrow())
                .queryParam("format", "json")
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
        URI uri = UriBuilder.of("https://geocode-maps.yandex.ru/1.x")
                .queryParam("geocode", longitude + "," + latitude)
                .queryParam("kind", "district")
                .queryParam("apikey", environment.getProperty("yandex.geo.key", String.class).orElseThrow())
                .queryParam("format", "json")
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
