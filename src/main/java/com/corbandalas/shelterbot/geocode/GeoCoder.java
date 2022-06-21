package com.corbandalas.shelterbot.geocode;


public interface GeoCoder {

    String geocode(String address) throws Exception;

    String reverseGeocode(String longitude, String latitude) throws Exception;

}
