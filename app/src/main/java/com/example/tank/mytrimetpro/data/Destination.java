package com.example.tank.mytrimetpro.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.example.tank.mytrimetpro.data.googleroute.Leg;
import com.example.tank.mytrimetpro.data.googleroute.RouteRequest;
import com.example.tank.mytrimetpro.data.gson.serializer.RouteRequestSerializer;
import com.example.tank.mytrimetpro.util.CalendarFactory;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by tank on 8/29/16.
 */
@Table(name = "Destinations")
public class Destination extends Model implements Parcelable {
    public static final Integer TRUE = 1;
    public static final Integer FALSE = 0;
    private static final String TAG = Destination.class.getSimpleName();
    public static final int MAX_NUMBER_OF_RECORDS = 42;
    private static final long DEACTIVE_TIME_BUFFER = 600000;
    public static final String DELETE_EXTRA_RECORDS_WHERE_CLAUSE = String.format("id = (SELECT id FROM Destinations ORDER BY date_queried DESC LIMIT -1 OFFSET %d)", MAX_NUMBER_OF_RECORDS);

    @Column(name = "destination_id", unique = true)
    private String mDestinationId;

    @Column(name = "latitude")
    private double mLatitude;

    @Column(name = "longitude")
    private double mLongitude;

    @Column(name = "address")
    private String mAddress;

    @Column(name = "place_name")
    private String mPlaceName;

    @Column(name = "date_queried")
    private Long mDateQueried;

    @Column(name = "is_active")
    private Integer mActive;

    @Column(name = "route_points")
    private String mRoute;

    public Destination() {
        super();
    }

    public Destination(Parcel in) {
        mDestinationId = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mAddress = in.readString();
        mPlaceName = in.readString();
        mDateQueried = in.readLong();
        mActive = in.readInt();
        mRoute = in.readString();
    }

    public Destination(Place place) {
        this();
        // get current time of location selection in Unix time
        Long currentTimeInSeconds = (new Date().getTime()) / 1000;

        this.mDestinationId = place.getId();
        this.mPlaceName = place.getName().toString();
        this.mLatitude = place.getLatLng().latitude;
        this.mLongitude = place.getLatLng().longitude;
        this.mDateQueried = currentTimeInSeconds;
        this.mRoute = null;
        this.mActive = FALSE;

        /*
            If destination is not a named location (i.e, just an mAddress), then create a destination object with a null mAddress, as the place
            name will already be the mAddress.
            This way, in the destination history list, the mAddress for this location won't be repeated twice.
         */
        if (doesLocationHaveName(place.getPlaceTypes())) {
            mAddress = place.getAddress().toString();
        }
    }

    public Destination(String destinationId, String address, String placeName, double latitude, double longitude, Long dateQueried, Integer isActive, RouteRequest request) {
        this();
        this.mDestinationId = destinationId;
        this.mAddress = address;
        this.mPlaceName = placeName;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mDateQueried = dateQueried;
        this.mActive = isActive;
        this.mRoute = new RouteRequestSerializer().serialize(request);
    }

    public RouteRequest getRoute() {
        return new RouteRequestSerializer().deserialize(mRoute);
    }

    public void setRoute(RouteRequest route) {
        this.mRoute = new RouteRequestSerializer().serialize(route);
    }

    public String getDestinationId() {
        return mDestinationId;
    }

    public void setId(String destinationId) {
        this.mDestinationId = destinationId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public void setPlaceName(String placeName) {
        this.mPlaceName = placeName;
    }

    public Long getDateQueried() {
        return mDateQueried;
    }

    public void setDateQueried(Long dateQueried) {
        this.mDateQueried = dateQueried;
    }

    public boolean isActive() {
        return TRUE.equals(mActive);
    }

    public void setActive(Boolean active) {
        if (active) {
            this.mActive = TRUE;
        } else {
            this.mActive = FALSE;
        }
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mDestinationId);
        out.writeDouble(mLatitude);
        out.writeDouble(mLongitude);
        out.writeString(mAddress);
        out.writeString(mPlaceName);
        out.writeLong(mDateQueried);
        out.writeInt(mActive);
        out.writeString(mRoute);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean hasRoute() {
        return this.mRoute != null;
    }

    public LatLng destinationLatLng() {
        return new LatLng(mLatitude, mLongitude);
    }

    /*
        checks whether the place is a named location based on its placeType integers
        - integers under 1000 reference places of business or other public locations with names other than their addresses
        - integers over 1000 reference street addresses or town names
        If the location has no integer types under 1000, then it has no name separate from its mAddress
     */
    public boolean doesLocationHaveName(List<Integer> placeTypes) {
        for (Integer i : placeTypes) {
            if (i < GOOGLE_NAMED_LOCATION_CUTOFF) {
                return true;
            }
        }
        return false;
    }

    private static final int GOOGLE_NAMED_LOCATION_CUTOFF = 1000;
    public static final Parcelable.Creator<Destination> CREATOR = new Parcelable.Creator<Destination>() {
        public Destination createFromParcel(Parcel in) {
            return new Destination(in);
        }

        public Destination[] newArray(int size) {
            return new Destination[size];
        }
    };

    public static Integer getDestinationCount() {
        return new Select()
                .from(Destination.class)
                .count();
    }

    /* return all destinations in reverse order by query date */
    public static List<Destination> getDestinations() {
        return new Select()
                .from(Destination.class)
                .orderBy("date_queried DESC")
                .where("is_active = ?", FALSE)
                .execute();
    }

    /* retrieve a destination by its google location ID if it exists in the database */
    public static Destination getDestinationByGoogleId(Destination destination) {
        return new Select()
                .from(Destination.class)
                .where("destination_id = ?", destination.getDestinationId())
                .executeSingle();
    }

    /* update the query date on an existing destination */
    public static void updateDestinationWithNewQueryDate(Destination destination) {
        Destination destinationToUpdate = new Select().from(Destination.class)
                .where("destination_id = ?", destination.getDestinationId())
                .executeSingle();

        destinationToUpdate.setDateQueried(destination.getDateQueried());
        destinationToUpdate.setActive(destination.isActive());
        destinationToUpdate.setRoute(destination.getRoute());

        destinationToUpdate.save();
    }

    /* only currently used for testing purposes */
    public static void deleteAllDestinations() {
        new Delete()
                .from(Destination.class)
                .execute();
    }

    public static void stopCurrentlyActiveRoute() {
        Destination activeDestination = getActiveRoute();
        if(activeDestination != null) {
//            Leg leg = activeDestination.getRoute().getLeg();
            deactivateRoute(activeDestination);
            //todo add dependcy
//            NotificationEventReceiver.removeNotificationAlarm(context,
//                    context.getResources().getString(R.string.notification_message, Calculation.getMinToFirstStop(context, leg)));
        }
    }

    private static void deactivateRoute(Destination activeDestination){
        activeDestination.setActive(false);
        activeDestination.save();
    }

    public static Destination getActiveRoute() {
        Destination activeDestination = new Select()
                .from(Destination.class)
                .where("is_active = ?", TRUE)
                .executeSingle();
        if(activeDestination != null && isRouteInThePast(activeDestination)){
            deactivateRoute(activeDestination);
            return null;
        }
        return activeDestination;
    }

    private static boolean isRouteInThePast(Destination activeRoute) {
        Calendar deactivateRouteTime = activeRoute.getRoute().getLeg().getDestinationArrivalTime().getValue();
        deactivateRouteTime.setTimeInMillis(deactivateRouteTime.getTimeInMillis() + DEACTIVE_TIME_BUFFER);
        return deactivateRouteTime.before(CalendarFactory.getUTCCalendar());
    }

    /* if destination already exists in the database, update its query date with the current timestamp.
      Otherwise, add it to the database */
    public static void createOrUpdateDestination(Destination destination) {
        if (getDestinationByGoogleId(destination) != null) {
            Log.i(TAG, "updating destination in database");
            updateDestinationWithNewQueryDate(destination);
        } else {
            Log.i(TAG, "creating new destination in database");
            destination.save();

            int count = getDestinationCount();
            Log.i(TAG, String.format("found [%d] destinations in the database", count));
            if (count > MAX_NUMBER_OF_RECORDS) {
                deleteExtraRecords();
            }
        }
    }

    private static void deleteExtraRecords() {
        Log.i(TAG, "deleting extra records from the database");
        new Delete()
                .from(Destination.class)
                .where(DELETE_EXTRA_RECORDS_WHERE_CLAUSE)
                .execute();
    }

    public static void updateQueryDate(Destination destination) {
        Long currentTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime());
        destination.setDateQueried(currentTimeInSeconds);
        destination.save();
    }

}