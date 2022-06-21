package com.corbandalas.shelterbot.staticmap.google;


import com.corbandalas.shelterbot.staticmap.StaticMapAPIClient;
import io.micronaut.context.env.Environment;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Singleton
public class GoogleStaticMapAPIClient implements StaticMapAPIClient {

    @Inject
    private Environment environment;

    @Override
    public byte[] getMapPicture(String centerLatitude, String centerLongitude, Object[] markers) throws Exception {

        UriBuilder uriBuilder = UriBuilder.of("https://maps.googleapis.com/maps/api/staticmap")
                .queryParam("center", centerLatitude.concat("").concat(centerLongitude))
                .queryParam("zoom", "15")
                .queryParam("size", "500x500")
                .queryParam("language", "ru")
                .queryParam("key", environment.getProperty("google.geo.key", String.class).orElseThrow());

        if (markers != null && markers.length > 0) {
            uriBuilder.queryParam("markers", markers);
        }

        URI uri = uriBuilder.build();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .build();

        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        return response.body();
    }
}
