package com.example.tank.mytrimetpro.data;

/**
 * Created by jmiller on 7/21/2016.
 */

public class NetworkConstants {

    /* These constants are used to build the google url for requesting route points.  */
    public static final String SCHEME = "https";
    public static final String GOOGLE_MAPS_BASE_URL = "maps.googleapis.com";
    public static final String GOOGLE_API_PATH = "/maps/api/directions/json";

    public static final String WAYPOINTS = "waypoints";
    public static final String WAYPOINTS_VALUE = "optimize:true|";

    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destination";

    public static final String SENSOR = "sensor";
    public static final String SENSOR_VALUE = "false";

    public static final String MODE = "mode"; //walking, driving, transit
    public static final String DEPARTURE_TIME = "departure_time";

    /* mode variables */
    public static final String WALKING = "walking";
    public static final String DRIVING = "driving";
    public static final String TRANSIT = "transit";

    /*TriMet API constants go here*/
    public static final String VERSION_ONE = "v1";
    public static final String VERSION_TWO = "v2";
    public static final String TRIMET_BASE_URL = "developer.trimet.org";
    public static final String TRIMET_API_PATH = "/ws";

    /* Trimet end points the app requires */
    public static final String STOPS_ENDPOINT = "stops";
    public static final String ARRIVALS_ENDPOINT = "arrivals";
    public static final String VEHICLES_ENDPOINT = "vehicles";

    /*Url variables and arguments*/
    //TODO: Find a better, safer spot to put the API key.
    public static final String API_KEY_URL_VARIABLE = "appid";
    public static final String API_KEY = "83AB90208AC376D6021D8D1C6";

    public static final String VEHICLE_IDS = "ids";

    public static final String JSON = "json";
    public static final String TRUE = "true";

    public static final String ARRIVALS_LOCATION_IDS = "locids";
    public static final String ARRIVALS_MINUTES = "minutes";
    public static final String ARRIVALS_MINUTES_VALUE = "60";

    public static final String STOPS_LONGITUDE_LATITUDE = "ll";
    public static final String STOPS_SEARCH_METERS = "meters";
    public static final String STOPS_BUS_STOP_SEARCH_RADIUS = "5";
}
