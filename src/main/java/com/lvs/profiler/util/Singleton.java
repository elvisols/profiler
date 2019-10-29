package com.lvs.profiler.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Singleton {

    private static ObjectMapper objectMapperInstance = null;
    private static HotelResponse hotelResponseInstance = null;
    private static AmenityResponse amenityResponseInstance = null;

    private Singleton(){}

    public static ObjectMapper getObjectMapper() {
        synchronized (Singleton.class) {
            if(objectMapperInstance == null) {
                objectMapperInstance = new ObjectMapper();
            }
        }

        return objectMapperInstance;
    }

    public static HotelResponse getHotelResponse() {
        synchronized (Singleton.class) {
            if(hotelResponseInstance == null) {
                hotelResponseInstance = new HotelResponse();
            }
        }

        return hotelResponseInstance;
    }

    public static AmenityResponse getAmenityResponse() {
        synchronized (Singleton.class) {
            if(amenityResponseInstance == null) {
                amenityResponseInstance = new AmenityResponse();
            }
        }

        return amenityResponseInstance;
    }

}
