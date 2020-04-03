package com.example.suberduberuber.Fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.example.suberduberuber.Adapters.AvailableRequestListAdapter;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Services.PermissionsService;
import com.example.suberduberuber.ViewModels.ViewRequestsViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;


public class DriverSearchRequests extends GoogleMapsFragment implements GoogleMap.OnMarkerClickListener,  AvailableRequestListAdapter.RequestCardTouchListener {

    private ViewRequestsViewModel viewRequestsViewModel;
    protected NavController navController;
    private RecyclerView requestRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AvailableRequestListAdapter adapter;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private double DISTANCE_FACTOR = 0.16;

    private CardView noRequestsMessage;
    private TextView refreshLocation;


    public DriverSearchRequests() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_driver_search_requests, container, false);
        mMapView = view.findViewById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        permissionsService = new PermissionsService(getContext(), getActivity());
        initMap(savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        viewRequestsViewModel = new ViewModelProvider(requireActivity()).get(ViewRequestsViewModel.class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        requestRecyclerView = view.findViewById(R.id.request_list);
        configureRecyclerView();

        noRequestsMessage = view.findViewById(R.id.no_requests_message);
        refreshLocation = view.findViewById(R.id.refresh_location);

        refreshLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.getResult() != null) {
                            setBounds(task.getResult());
                        }
                    }
                });
                viewRequestsViewModel.removeObservers(getViewLifecycleOwner());
                getAllRequests();
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
        if (mMap != null) {
            setCameraOnBounds(getLargeGeoBounds(location, DISTANCE_FACTOR));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    private List<Request> displayRequests(List<Request> requests, Location location) {
        if (mMap != null) {
            mMap.setOnMarkerClickListener(this);
            for (Request request : requests) {
                if (location != null) {
                    if (getLargeGeoBounds(location, DISTANCE_FACTOR).contains(request.getPath().getStartLocation().getLatLng())) {
                        displayRequest(request);
                    }
                    else {
                        requests.remove(request);
                    }
                }
            }
        }
        return requests;
    }

    private void displayRequest(Request request) {
        dropMapMarker(request.getPath().getStartLocation().getLatLng(), request.getPath().getStartLocation().getLocationName(), null);
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

    private void getAllRequests() {
        viewRequestsViewModel.getAllRequests().observe(getViewLifecycleOwner(), new Observer<List<Request>>() {
            @Override
            public void onChanged(List<Request> requests) {
                if (requests.size() > 0) {
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.getResult() != null) {
                                mMap.clear();
                                //setBounds(task.getResult());
                                if (displayRequests(requests, task.getResult()).size() > 0) {
                                    noRequestsMessage.setVisibility(View.GONE);
                                    adapter.setRequestDataset(displayRequests(requests, task.getResult()));
                                } else {
                                    noRequestsMessage.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                } else {
                    mMap.clear();
                    noRequestsMessage.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    setBounds(location);
                    noRequestsMessage.setVisibility(View.VISIBLE);
                }
            }
        });
        getAllRequests();
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
}
