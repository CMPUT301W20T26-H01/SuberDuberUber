package com.example.suberduberuber.Models;

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

public class DroppedPinPlace {
    Place droppedPinPlace = null;

    public DroppedPinPlace() { }

    public DroppedPinPlace(LatLng latLng) {
        createPlace(latLng);
    }

    public Place getDroppedPinPlace() {
        return droppedPinPlace;
    }

    private void createPlace(LatLng latLng) {
        this.droppedPinPlace = new Place() {
            @Nullable
            @Override
            public String getAddress() {
                return "Dropped Pin Location";
            }

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
                return "Dropped Pin";
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
