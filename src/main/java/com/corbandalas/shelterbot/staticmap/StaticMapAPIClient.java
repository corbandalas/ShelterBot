package com.corbandalas.shelterbot.staticmap;


public interface StaticMapAPIClient {

    byte[] getMapPicture(String centerLatitude, String centerLongitude, Object[] markers) throws Exception;

}
