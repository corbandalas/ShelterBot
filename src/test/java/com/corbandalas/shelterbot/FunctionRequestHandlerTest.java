package com.corbandalas.shelterbot;

import com.corbandalas.shelterbot.geocode.GeoCoder;
import com.corbandalas.shelterbot.geocode.google.GoogleGeoCoder;
import com.corbandalas.shelterbot.geocode.google.dto.GoogleGeoCoderResponse;
import com.corbandalas.shelterbot.geocode.yandex.YandexGeoCoder;
import com.corbandalas.shelterbot.geocode.yandex.dto.Point__1;
import com.corbandalas.shelterbot.geocode.yandex.dto.YandexGeoCoderResponse;
import com.corbandalas.shelterbot.model.Country;
import com.corbandalas.shelterbot.model.Storage;
import com.corbandalas.shelterbot.utils.ShelterBotUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Set;

public class FunctionRequestHandlerTest {

    private static FunctionRequestHandler handler;

    @Inject
    private GeoCoder geoCoder;

    @BeforeAll
    public static void setupServer() {
        handler = new FunctionRequestHandler();
    }

    @AfterAll
    public static void stopServer() {
        if (handler != null) {
            handler.getApplicationContext().close();
        }
    }

    @Test
    public void testHandler() throws Exception {
        Storage storage = handler.getApplicationContext().getBean(Storage.class);

        Set<Country> root = storage.root();
//
//        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
//        request.setHttpMethod("POST");
//        request.setPath("/");
//
//
//
//
//        request.setBody("");
//        APIGatewayProxyResponseEvent response = handler.execute(request);
//        assertEquals(200, response.getStatusCode().intValue());
//        assertEquals("{\"message\":\"Hello World\"}", response.getBody());
//
        ObjectMapper objectMapper = new ObjectMapper();
//
//        ShelterBotState shelterBotState = new ShelterBotState(ShelterBotStateEnum.CITY_CHOOSE, new HashMap<>());
//
//        shelterBotState.setState(ShelterBotStateEnum.CITY_CHOOSE);
//        shelterBotState.getData().put("key", "value");
//
//        StringWriter stringWriter = new StringWriter();
//
//
//        objectMapper.writeValue(stringWriter, shelterBotState);
//        stringWriter.getBuffer().toString();
//
//        String CYRILLIC_TO_LATIN = "Latin-Cyrillic";
//        String st = "DNR-DONBASS-YASINOVATAYA";
//
//        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
//        String result = toLatinTrans.transliterate(st);
//        System.out.println(result);
        String address = "Донецкая область Моспино ул. Ново-Моспино, 20";

//        GoogleGeoCoder googleGeoCoder = handler.getApplicationContext().getBean(GoogleGeoCoder.class);
//        String geocode = googleGeoCoder.reverseGeocode("37.843328" ,"48.001657");
//        GoogleGeoCoderResponse googleGeoCoderResponse = objectMapper.readValue(geocode, GoogleGeoCoderResponse.class);
//
//        System.out.println("Google:" + geocode);


        Double aDouble = ShelterBotUtils.haversineDistance(new Double(48.001657), new Double(37.843328), new Double(48.01690), new Double(37.78967));

//        YandexGeoCoder yandexGeoCoder = handler.getApplicationContext().getBean(YandexGeoCoder.class);
//        geocode = yandexGeoCoder.geocode(address);
//        YandexGeoCoderResponse yandexGeoCoderResponse = objectMapper.readValue(geocode, YandexGeoCoderResponse.class);

//        Point__1 point = yandexGeoCoderResponse.getResponse().getGeoObjectCollection().getFeatureMember().get(0).getGeoObject().getPoint();

        System.out.println(aDouble);
//
        //

//
//
//        String s = go.reverseGeocode("37.790157", "48.016788");
//        yandexGeoCoderResponse = objectMapper.readValue(s, YandexGeoCoderResponse.class);
//
//        yandexGeoCoderResponse.getResults().get(0).getGeometry().getLocation().

//        String districtName = split[1];
//        String shelterOwner = split[2];
//        String shelterAddress = split[3];
//        String shelterResponsible = split[4];
//        String shelterCapacity = split[5];
//        String shelterDescription = split[6];

//        YandexGeoCoder yandexGeoCoder = handler.getApplicationContext().getBean(YandexGeoCoder.class);

        root.stream().forEach(c -> c.getRegions().stream().
                forEach(region -> region.getCities().stream()
                        .forEach(city -> city.getDistricts().stream()
                                .forEach(district -> district.getShelters().stream().forEach(shelter -> {

                                    Double distance = ShelterBotUtils.haversineDistance(new Double(48.001657), new Double(37.843328), Double.parseDouble(shelter.getLat()), Double.parseDouble(shelter.getLng()));

                                    if (distance < 3.0) {
                                        System.out.println(shelter.getAddress());
                                    }


//                                    String lat = "";
//                                    String lng = "";
//
//                                    String coord = "";
//                                    try {
//                                        String geocode = yandexGeoCoder.geocode("Донецкая область " + city.getName() + " " + shelter.getAddress());
//                                        YandexGeoCoderResponse yandexGeoCoderResponse = objectMapper.readValue(geocode, YandexGeoCoderResponse.class);
//
//                                        Point__1 point = yandexGeoCoderResponse.getResponse().getGeoObjectCollection().getFeatureMember().get(0).getGeoObject().getPoint();
//
//                                        coord = point.getPos();
//
//
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }

//                                    System.out.println(district.getName() + ";" + shelter.getOwner() + ";" + shelter.getAddress() + ";" +
//                                            shelter.getResponsible() + ";" +
//                                            shelter.getCapacity() + ";" + shelter.getDescription() + ";" + distance + ";");
                                })))));

        System.out.println("");

        UriBuilder of = UriBuilder.of(UriBuilder.of("https://maps.googleapis.com/maps/api/staticmap")
                .queryParam("center", "48.016949,37.789687")
                .queryParam("zoom", "14")
                .queryParam("size", "600x600")
                .queryParam("markers", "сolor:blue|label:S|48.015474,37.807135", "size:tiny|label:S|48.016673,37.806802")
                .build());

        URI uri = of.build();

        System.out.println(uri);
    }
}