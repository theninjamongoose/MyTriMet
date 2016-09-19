package com.example.tank.mytrimetpro.data;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.tank.mytrimetpro.data.googleroute.RouteRequest;
import com.example.tank.mytrimetpro.data.googleroute.Step;
import com.example.tank.mytrimetpro.data.gson.GsonRequest;
import com.example.tank.mytrimetpro.data.trimet.arrival.ArrivalData;
import com.example.tank.mytrimetpro.data.trimet.arrival.ArrivalResponse;
import com.example.tank.mytrimetpro.data.trimet.stoplocation.StopLocationIdResponse;
import com.example.tank.mytrimetpro.data.trimet.vehicle.VehicleData;
import com.example.tank.mytrimetpro.data.trimet.vehicle.VehicleResponse;
import com.example.tank.mytrimetpro.util.CalendarFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jmiller on 7/20/2016.
 * This class handles network calls. The methods within this class
 * get executed on a separate thread.
 */
public class NetworkHandler {
    /* Expected respones for the travel mode variable */
    public static final String WALKING = "WALKING";
    public static final String TRANSIT = "TRANSIT";
    private static final String DEBUG_URL_DESCRIPTION = "Generated URL: ";
    private static final String ILLEGAL_ARGUMENT_EXCEPTION = "Travel mode has to be TRANSIT, and not null or WALKING";
    /* Initializing the tag for the built-in android logger */
    private final String TAG = NetworkHandler.class.getSimpleName();

    /* Context for method calls that need a reference to it. */
    private Context mContext;
    private DataRequestQueue mDataRequestQueue;


    public NetworkHandler(Context context, DataRequestQueue dataRequestQueue) {
        this.mContext = context;
        this.mDataRequestQueue = dataRequestQueue;
    }

    public void directionPointsGson(LatLng start, LatLng end,
                                    final MapPresenterCallback mapPresenterCallback) {
        final String url = buildGoogleRouteUrl(start, end);
        GsonRequest<RouteRequest> req = new GsonRequest<>(url, RouteRequest.class,
                null, new Response.Listener<RouteRequest>() {
            @Override
            public void onResponse(RouteRequest response) {
                mapPresenterCallback.updateRoute(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        /* Dispatchs network request */
        mDataRequestQueue.addToRequestQueue(req);
    }

    /* The google API returns a latitude and longitude point when the route is built out.
     * If you pass the latitude and longitude values into this method, the callback
      * will give the ID number of the bust stop within a 5 meter radius of the lat lng point*/
    public void getStopLocationId(final Step transitStop, final Calendar arrivalTimeAtStop,
                                  final MapPresenterCallback mapPresenterCallback) throws IllegalArgumentException {
        if (transitStop == null || !transitStop.getTravelMode().equals(TRANSIT)) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION);
        }
        final String url = buildRetrieveStopIdUrl(transitStop.getStartLocation().latitude,
                transitStop.getStartLocation().longitude);

        GsonRequest<StopLocationIdResponse> req = new GsonRequest<>(url, StopLocationIdResponse.class,
                null, new Response.Listener<StopLocationIdResponse>() {
            @Override
            public void onResponse(StopLocationIdResponse response) {
                int trimetLocationId = response.getLocationId();
                transitStop.setTrimetLocationId(trimetLocationId);
                getBusStopArrivalDetails(transitStop, trimetLocationId, arrivalTimeAtStop, mapPresenterCallback);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "An error occured when the getting the bus Stop ID.", error);
            }
        });
        mDataRequestQueue.addToRequestQueue(req);
    }

    /* Once a Stop ID is acquired, pass the stop ID into this network call, and the response will
     * give a list of buses arriving at that stop. The list of buses that gets returned will be
     * accurate up to an hour away. Find the bus who's is arriving around the time
     * the user will be leaving, and get its ID if possible. */
    public void getBusStopArrivalDetails(final Step transitStop, int locationId, final Calendar arrivalTime,
                                         final MapPresenterCallback mapPresenterCallback) {
        final String url = buildBusArrivalUrl(locationId);

        GsonRequest<ArrivalResponse> req = new GsonRequest<>(url, ArrivalResponse.class, null, new Response.Listener<ArrivalResponse>() {
            @Override
            public void onResponse(ArrivalResponse response) {
                int id = getBusIdFromArrivalTime(arrivalTime, response);
                transitStop.setVehicleId(id);
                if (id > 0) {
                    mapPresenterCallback.queryForNextIncomingtransitVehicle(id);

                } else {
                    mapPresenterCallback.noVehicleAssigned();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "An error occured when the getting Bus Arrival data.", error);
            }
        });
        mDataRequestQueue.addToRequestQueue(req);
    }

    /* After getting the Bus's ID, call this method as many times as needed to get the information
 * about the bus. Information includes: Load capacity, current position as a Lat Lng point,
 * and other potentially useful information. */
    public  void getUpdatedVehicleDetails(final int locationId, final int vehicleId,
                                          final MapPresenterCallback mapPresenterCallback) {
        final String url = buildUpdateVehicleUrl(vehicleId);


        GsonRequest<VehicleResponse> req = new GsonRequest<>(url, VehicleResponse.class, null, new Response.Listener<VehicleResponse>() {
            @Override
            public void onResponse(VehicleResponse response) {
                getEstimatedVehicleArrivalTime(locationId,
                        getVehicleViaId(response.getVehicleResult().getVehicleDataList(), vehicleId), mapPresenterCallback);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "An error occured when the getting the bus Stop ID.", error);
            }
        });

        mDataRequestQueue.addToRequestQueue(req);
    }

    private VehicleData getVehicleViaId(List<VehicleData> vehicleDataList, int vehicleId) {
        for(VehicleData vehicleData : vehicleDataList){
            if(vehicleData.getVehicleId() == vehicleId){
                return vehicleData;
            }
        }
        return null;
    }

    public void getEstimatedVehicleArrivalTime(int locationId, final VehicleData vehicleData,
                                               final MapPresenterCallback mapPresenterCallback) {
        final String url = buildBusArrivalUrl(locationId);

        GsonRequest<ArrivalResponse> req = new GsonRequest<>(url, ArrivalResponse.class, null, new Response.Listener<ArrivalResponse>() {
            @Override
            public void onResponse(ArrivalResponse response) {
                vehicleData.setEstimatedArivalTime(getEstimatedArrivalTime(vehicleData.getVehicleId(), response));
                mapPresenterCallback.trimetLocationUpdate(vehicleData);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "An error occured when the getting Bus Arrival data.", error);
            }
        });
        mDataRequestQueue.addToRequestQueue(req);
    }

    private Calendar getEstimatedArrivalTime(int vehicleId, ArrivalResponse response) {
        for (ArrivalData data : response.getArrivalQueryResult().getArrivals()) {
            if (data.getVehicleId() == vehicleId) {
                return data.getEstimatedArrivalTime();
            }
        }
        return null;
    }



    /* This method takes a two points and builds a url request for Google */
    /* Example url: “...api/directions/json?origin=17.449797,78.373037&destination=17.47989,78.390095&%20waypoints=optimize:true|17.449797,78.373037||17.47989,78.390095&sensor=false” */
    private String buildGoogleRouteUrl(LatLng start, LatLng finish) {
        String startLatLng = TextUtils.concat(String.valueOf(start.latitude),
                ",", String.valueOf(start.longitude)).toString();

        String endLatLng = TextUtils.concat(String.valueOf(finish.latitude),
                ",", String.valueOf(finish.longitude)).toString();

        Uri.Builder builtUrl = new Uri.Builder();
        builtUrl.scheme(NetworkConstants.SCHEME)
                .authority(NetworkConstants.GOOGLE_MAPS_BASE_URL)
                .path(NetworkConstants.GOOGLE_API_PATH)
                .appendQueryParameter(NetworkConstants.ORIGIN, startLatLng)
                .appendQueryParameter(NetworkConstants.DESTINATION, endLatLng)
                .appendQueryParameter(NetworkConstants.SENSOR, NetworkConstants.SENSOR_VALUE)
                .appendQueryParameter(NetworkConstants.MODE, NetworkConstants.TRANSIT)
                .appendQueryParameter(NetworkConstants.DEPARTURE_TIME, getDepartureTime());

        /* The generated url as a string variable. */
        String url = builtUrl.build().toString();

        /* Displaying generated url for debugging purposes. */
        Log.d(TAG, DEBUG_URL_DESCRIPTION + url);
        return url;
    }

    /* Build the url for getting ID number of a stop */
    /* API documentation: https://developer.trimet.org/ws_docs/stop_location_ws.shtml */
    /* An example url: https://developer.trimet.org/ws/V1/stops?&json=true&ll=-122.842632,45.530264&meters=5&appid=KEY */
    private String buildRetrieveStopIdUrl(double latitude, double longitude) {
        String busStopLatLng = TextUtils.concat(String.valueOf(longitude), ",",
                String.valueOf(latitude)).toString();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkConstants.SCHEME)
                .authority(NetworkConstants.TRIMET_BASE_URL)
                .path(NetworkConstants.TRIMET_API_PATH)
                .appendPath(NetworkConstants.VERSION_ONE)
                .appendPath(NetworkConstants.STOPS_ENDPOINT)
                .appendQueryParameter(NetworkConstants.JSON, NetworkConstants.TRUE)
                .appendQueryParameter(NetworkConstants.STOPS_LONGITUDE_LATITUDE, busStopLatLng)
                .appendQueryParameter(NetworkConstants.STOPS_SEARCH_METERS,
                        NetworkConstants.STOPS_BUS_STOP_SEARCH_RADIUS)
                .appendQueryParameter(NetworkConstants.API_KEY_URL_VARIABLE, NetworkConstants.API_KEY);

        String url = builder.build().toString();
        Log.d(TAG, DEBUG_URL_DESCRIPTION + " for getting bus stop location id: " + url);
        return url;
    }

    /* Builds url for getting a list of buses arriving at the specified location */
    /* API documentation: https://developer.trimet.org/ws_docs/arrivals2_ws.shtml*/
    /* An example url: https://developer.trimet.org/ws/v2/arrivals?json=true&locids=6828&appid=KEY */
    private String buildBusArrivalUrl(int locationId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkConstants.SCHEME)
                .authority(NetworkConstants.TRIMET_BASE_URL)
                .path(NetworkConstants.TRIMET_API_PATH)
                .appendPath(NetworkConstants.VERSION_TWO)
                .appendPath(NetworkConstants.ARRIVALS_ENDPOINT)
                .appendQueryParameter(NetworkConstants.JSON, NetworkConstants.TRUE)
                .appendQueryParameter(NetworkConstants.ARRIVALS_MINUTES, NetworkConstants.ARRIVALS_MINUTES_VALUE)
                .appendQueryParameter(NetworkConstants.ARRIVALS_LOCATION_IDS, String.valueOf(locationId))
                .appendQueryParameter(NetworkConstants.API_KEY_URL_VARIABLE, NetworkConstants.API_KEY);

        String url = builder.build().toString();
        Log.d(TAG, DEBUG_URL_DESCRIPTION + " for getting bus id: " + url);
        return url;
    }

    /* Builds url for getting updated information on a specified bus. */
    /* API documentation: http://developer.trimet.org/ws_docs/vehicle_locations_ws.shtml */
    /* An example url: https://developer.trimet.org/ws/v2/vehicles?ids=2810&appid=KEY */
    private String buildUpdateVehicleUrl(int vehicleId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkConstants.SCHEME)
                .authority(NetworkConstants.TRIMET_BASE_URL)
                .path(NetworkConstants.TRIMET_API_PATH)
                .appendPath(NetworkConstants.VERSION_TWO)
                .appendPath(NetworkConstants.VEHICLES_ENDPOINT)
                .appendQueryParameter(NetworkConstants.VEHICLE_IDS, String.valueOf(vehicleId))
                .appendQueryParameter(NetworkConstants.API_KEY_URL_VARIABLE, NetworkConstants.API_KEY);

        String url = builder.build().toString();
        Log.d(TAG, DEBUG_URL_DESCRIPTION + " for updated bus information: " + url);
        return url;
    }

    private int getBusIdFromArrivalTime(Calendar minArrivalTime, ArrivalResponse response) {
        Collections.sort(response.getArrivalQueryResult().getArrivals(), new Comparator<ArrivalData>() {
            @Override

            public int compare(ArrivalData arrivalDataLeft, ArrivalData arrivalDataRight) {
                return getAvailableArrivalTime(arrivalDataLeft).compareTo(getAvailableArrivalTime(arrivalDataRight));
            }
        });

        for (ArrivalData data : response.getArrivalQueryResult().getArrivals()) {
            if (minArrivalTime.before(getAvailableArrivalTime(data))) {
                return data.getVehicleId();
            }
        }

        return 0;
    }

    private String getDepartureTime() {
        Calendar departureTime = CalendarFactory.getUTCCalendar();
        //todo introduce settings page
//        departureTime.add(Calendar.MINUTE, UserSettingsUtil.INSTANCE.getFirstStopNotificationStoredTime(mContext));

        /* Return departure time in seconds */
        return Long.toString(TimeUnit.MILLISECONDS.toSeconds(departureTime.getTimeInMillis()));
    }

    private Calendar getAvailableArrivalTime(ArrivalData data){
        if(data.getEstimatedArrivalTime() != null){
            return data.getEstimatedArrivalTime();
        }
        return data.getScheduledArrivalTime();
    }
}