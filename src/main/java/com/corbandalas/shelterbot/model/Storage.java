package com.corbandalas.shelterbot.model;

import io.micronaut.context.env.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Slf4j
@Singleton
public class Storage {

    private static final String BASIC_PROPERTY_NAME = "shelterbot.db.shelters";

    private Set<Country> dataRoot;

    @Inject
    private Environment environment;

    private void init() {
        try {

            dataRoot = new HashSet<>();

            environment.getPropertyEntries(BASIC_PROPERTY_NAME)
                    .stream().forEach(s -> {

                        String fileName = environment.getProperty(BASIC_PROPERTY_NAME + "." + s, String.class).orElseThrow();

                        String[] split = StringUtils.split(s, "-");

                        String countryName = StringUtils.capitalize(split[0]);
                        String regionName = StringUtils.capitalize(split[1]);
                        String cityName = StringUtils.capitalize(split[2]);

                        Country country = getCountry(countryName).orElseGet(() -> {
                                    Country newCountry = new Country(countryName, new HashSet<>());
                                    dataRoot.add(newCountry);
                                    return newCountry;
                                }
                        );

                        Region region = getRegion(regionName, countryName)
                                .orElseGet(() -> {
                                            Region newRegion = new Region(UUID.randomUUID(), regionName, new HashSet<>());
                                            country.getRegions().add(newRegion);
                                            return newRegion;
                                        }
                                );

                        City city = getCity(cityName, regionName, countryName)
                                .orElseGet(() -> {
                                    City newCity = new City(UUID.randomUUID(), cityName, "", new HashSet<>());
                                    region.getCities().add(newCity);
                                    return newCity;
                                });

                        readCSVEntry(country, region, city, fileName);

                    });

        } catch (Exception e) {
            log.error("Shelter db error", e);
        }
    }


    public Set<Country> root() {

        if (dataRoot == null || dataRoot.isEmpty()) {
            init();
        }

        return dataRoot;
    }

    private boolean checkIfCountryExist(String countryName) {
        return getCountry(countryName).isPresent();
    }

    private Optional<Country> getCountry(String countryName) {
        return dataRoot.parallelStream().filter(c -> c.getName().equalsIgnoreCase(countryName)).findFirst();
    }

    private Optional<Region> getRegion(String regionName, String countryName) {
        return getCountry(countryName)
                .flatMap(t -> t.getRegions().parallelStream().filter(r -> r.getName().equalsIgnoreCase(regionName)).findFirst());
    }

    private boolean checkIfRegionExist(String regionName, String countryName) {
        return getRegion(regionName, countryName).isPresent();
    }

    private Optional<City> getCity(String cityName, String regionName, String countryName) {
        return getRegion(regionName, countryName).flatMap(t -> t.getCities().parallelStream().filter(c -> c.getName().equalsIgnoreCase(cityName)).findFirst());
    }

    private boolean checkIfCityExist(String cityName, String regionName, String countryName) {
        return getCity(cityName, regionName, countryName).isPresent();
    }


    private void readCSVEntry(Country country, Region region, City city, String fileName) {

        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("shelters/" + fileName);
             InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(streamReader)) {

            br.lines().forEach(s -> parseCSVString(s, city));

        } catch (Exception e) {
            log.error("Error while parsing CSV file for City: ".concat(city.toString()), e);
        }
    }

    private void parseCSVString(String cvsLine, City city) {

        String[] split = cvsLine.split(";");

        String districtName = split[0];
        String shelterOwner = split[1];
        String shelterAddress = split[2];
        String shelterResponsible = split[3];
        String shelterCapacity = split[4];
        String shelterDescription = split[5];
        String shelterGeo = split[6];

        String[] coords = StringUtils.split(shelterGeo, " ");
        String longitude = coords[0];
        String latitude = coords[1];

        District district = city.getDistricts().parallelStream()
                .filter(d -> d.getName().equalsIgnoreCase(districtName)).findFirst()
                .orElse(District.builder()
                        .id(UUID.randomUUID())
                        .name(districtName)
                        .shelters(new HashSet<>())
                        .build());

        if (!city.getDistricts().contains(district)) {
            city.getDistricts().add(district);
        }

        Shelter shelter = Shelter.builder()
                .owner(shelterOwner)
                .address(shelterAddress)
                .capacity(shelterCapacity)
                .responsible(shelterResponsible)
                .id(UUID.randomUUID())
                .lat(latitude)
                .lng(longitude)
                .description(shelterDescription)
                .build();

        district.getShelters().add(shelter);


    }

}
