package com.example.suberduberuber.Fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suberduberuber.Adapters.AvailableRequestListAdapter;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Services.LocationService;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.example.suberduberuber.ViewModels.ViewRequestsViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DriverSearchRequests extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,  AvailableRequestListAdapter.RequestCardTouchListener {

    private static final int DEFAULT_ZOOM = 150;
    private MapView mMapView;
    private GoogleMap mMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "Auto Complete Log";
    private GeoApiContext mGeoApiContext = null;

    private LatLngBounds mapBounds;

    private ViewRequestsViewModel viewRequestsViewModel;
    protected NavController navController;
    private RecyclerView requestRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AvailableRequestListAdapter adapter;

    private LatLngBounds bound;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double DISTANCE_FACTOR = 0.16;

    private TextView noRequestsMessage;


    public DriverSearchRequests() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_search_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.route_map);

        navController = Navigation.findNavController(view);

        viewRequestsViewModel = new ViewModelProvider(requireActivity()).get(ViewRequestsViewModel.class);

        initGoogleMap(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        requestRecyclerView = view.findViewById(R.id.request_list);
        configureRecyclerView();

        noRequestsMessage = view.findViewById(R.id.no_requests_message);


        viewRequestsViewModel.getAllRequests().observe(getViewLifecycleOwner(), new Observer<List<Request>>() {
            @Override
            public void onChanged(List<Request> requests) {
                if(requests.size() > 0) {
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            setBounds(location);
                            noRequestsMessage.setVisibility(View.GONE);
                            displayRequests(requests);
                            adapter.setRequestDataset(requests);
                        }
                    });

                }
                else {
                    noRequestsMessage.setVisibility(View.VISIBLE);
                }

            }
        });

        requestRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                shrinkAllPopups();
            }
        });
    }

    private void setBounds(Location location) {
        double latDist = DISTANCE_FACTOR;
        double longDist = DISTANCE_FACTOR/Math.cos(Math.toRadians(location.getLatitude()));
        LatLng min = new LatLng(location.getLatitude() - latDist, location.getLongitude() - longDist);
        LatLng max = new LatLng(location.getLatitude() + latDist, location.getLongitude() + longDist);
        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(min);
        builder.include(max);
        bound = builder.build();
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bound, DEFAULT_ZOOM));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        viewRequestsViewModel.getRequestByPickupName(marker.getTitle()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {
                    Request request = queryDocumentSnapshots.getDocuments().get(0).toObject(Request.class);
                    showRequestInList(request);
                }
            }
        });

        return false;
    }

    private void showRequestInList(Request request) {
        int position = getPositionInRecyclerView(request);
        requestRecyclerView.scrollToPosition(position);
    }

    private int getPositionInRecyclerView(Request request) {
        List<Request> requests = viewRequestsViewModel.getAllRequests().getValue();
        for(int i = 0; i < requests.size(); i++) {
            if(requests.get(i).getRequestID().equals(request.getRequestID())) {
                return i;
            }
        }
        return -1;
    }


    private void displayRequests(List<Request> requests) {
        if (mMap != null) {
            mMap.setOnMarkerClickListener(this);
            for (Request request : requests) {
                if (bound.contains(request.getPath().getStartLocation().getLatLng())) {
                    displayRequest(request);
                }
            }
        }
    }

    private void displayRequest(Request request) {
        mMap.addMarker(
                new MarkerOptions()
                .position(request.getPath().getStartLocation().getLatLng())
                .title(request.getPath().getStartLocation().getLocationName())
        );
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);


        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_map_api_key))
                    .build();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    private void configureRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new AvailableRequestListAdapter(this);

        requestRecyclerView.setLayoutManager(layoutManager);
        requestRecyclerView.setAdapter(adapter);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(requestRecyclerView.getContext(), R.anim.layout_anim_fall_down);
        requestRecyclerView.setLayoutAnimation(animationController);
    }

    @Override
    public void shrinkAllPopups() {
        int numItems = layoutManager.getItemCount();
        for(int i = 0 ; i < numItems; i++) {
            AvailableRequestListAdapter.RequestViewHolder viewHolder = (AvailableRequestListAdapter.RequestViewHolder) requestRecyclerView.findViewHolderForAdapterPosition(i);
            if(viewHolder != null) {
                viewHolder.shrink();
            }
        }
    }

    @Override
    public void onRequestAccept(int position) {
        viewRequestsViewModel.acceptRequest(adapter.getRequestAtPosition(position));
        navController.navigate(R.id.action_driverSearchRequests_to_driverNavigationFragment);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }
}
