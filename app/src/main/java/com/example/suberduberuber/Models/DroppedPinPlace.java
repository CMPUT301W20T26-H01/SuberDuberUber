package com.example.suberduberuber.Models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Parcel;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlusCode;

import java.util.List;
import java.util.Locale;

public class DroppedPinPlace {
    private String city;
    private String province;
    private String country;
    private String postalCode;
    private Place droppedPinPlace = null;
    private String address = "Dropped Pin Location";
    private String name = "Selected Location";
    private LatLng latLng = null;

    public DroppedPinPlace() { }

    public DroppedPinPlace(LatLng latLng, Context context, String newName) throws java.io.IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.CANADA);
        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        address = addresses.get(0).getAddressLine(0);
        if (!address.contains(addresses.get(0).getFeatureName())) {
            name = addresses.get(0).getFeatureName();
        }
        if (newName != null) {
            name = newName;
        }
        createPlace(latLng, address, name);
    }

    public Place getDroppedPinPlace() {
        return droppedPinPlace;
    }

    private void createPlace(LatLng newLatLng, @Nullable String newAddress, @Nullable String newName) {
        this.latLng = newLatLng;
        if (newAddress != null) {
            this.address = newAddress;
        }
        if (newName != null) {
            name = newName;
        }
        this.droppedPinPlace = new Place() {
            @Nullable
            @Override
            public String getAddress() { return address; }

            @Nullable
            @Override
            public AddressComponents getAddressComponents() {
                return null;
            }

            @Nullable
            @Override
            public List<String> getAttributions() {
                return null;
            }

            @Nullable
            @Override
            public String getId() {
                return null;
            }

            @Nullable
            @Override
            public LatLng getLatLng() {
                return latLng;
            }

            @Nullable
            @Override
            public String getName() {
                return name;
            }

            @Nullable
            @Override
            public OpeningHours getOpeningHours() {
                return null;
            }

            @Nullable
            @Override
            public String getPhoneNumber() {
                return null;
            }

            @Nullable
            @Override
            public List<PhotoMetadata> getPhotoMetadatas() {
                return null;
            }

            @Nullable
            @Override
            public PlusCode getPlusCode() {
                return null;
            }

            @Nullable
            @Override
            public Integer getPriceLevel() {
                return null;
            }

            @Nullable
            @Override
            public Double getRating() {
                return null;
            }

            @Nullable
            @Override
            public List<Type> getTypes() {
                return null;
            }

            @Nullable
            @Override
            public Integer getUserRatingsTotal() {
                return null;
            }

            @Nullable
            @Override
            public Integer getUtcOffsetMinutes() {
                return null;
            }

            @Nullable
            @Override
            public LatLngBounds getViewport() {
                return null;
            }

            @Nullable
            @Override
            public Uri getWebsiteUri() {
                return null;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };
    }
}
