package com.example.suberduberuber.Fragments;

/*
Copyright [2020] [SuberDuberUber]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

****************************************************************************************************

Fragment which displays the route selected by the user and displays information about the ride. Displays
the user with an estimated fare for this ride and allows to user to change the bid amount. Will not allow
a bid lower than estimated fare and enforces user has enough fund in their wallet for bid amount. Redirects
to RidePending fragment upon selecting "Let's do this!". Extends GoogleMapsFragment
Sources include:
    GoogleMaps SKD Android Documentation - https://developers.google.com/maps/documentation/android-sdk/intro
    GooglePlaces SDK Android Documentation - https://developers.google.com/places/android-sdk/intro
    GoogleMaps YouTube Playlist by User CodingWithMitch - https://www.youtube.com/watch?v=RQxY7rrZATU&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi
    Permission Services Code from GitHub User mitchtabian - https://gist.github.com/mitchtabian/2b9a3dffbfdc565b81f8d26b25d059bf
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Services.PermissionsService;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.example.suberduberuber.ViewModels.QRCodeViewModel;
import com.google.android.gms.location.LocationServices;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.text.SimpleDateFormat;

public class ConfirmRouteFragment extends GoogleMapsFragment {

    private Button submitButton;
    private TextView pickupTextView;
    private TextView pickupTimeTextView;
    private TextView destinationTextView;
    private EditText bidAmount;

    private GetRideViewModel getRideViewModel;
    private QRCodeViewModel qrCodeViewModel;

    private Request tempRequest;

    protected NavController navController;

    private double balance;
    private AlertDialog dialog;

    public ConfirmRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_confirm_route, container, false);
        mMapView = view.findViewById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        permissionsService = new PermissionsService(getContext(), getActivity());
        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        tempRequest = getRideViewModel.getTempRequest().getValue();
        initMap(savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        qrCodeViewModel = ViewModelProviders.of(this).get(QRCodeViewModel.class);
        qrCodeViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setBalance(user);
            }
        });

        pickupTextView = view.findViewById(R.id.pickupLocation);
        destinationTextView = view.findViewById(R.id.destination);
        setTextViews();

        pickupTimeTextView = view.findViewById(R.id.pickupTime);
        String timeFormat = "HH:mm - EEE MMM dd, YYYY";;
        pickupTimeTextView.setText(new SimpleDateFormat(timeFormat).format(tempRequest.getTime()));

        bidAmount = view.findViewById(R.id.bid_amount);

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Submit ride to database
                try {
                    bidAmount.setError(null);
                    double bid = Double.valueOf(bidAmount.getText().toString());
                    if (bid >= tempRequest.getPath().getEstimatedFare() & bid <= balance) {
                        tempRequest.setPrice(bid);
                        if (bid > tempRequest.getPath().getEstimatedFare()) {
                            Toast.makeText(getContext(), "Bid Updated", Toast.LENGTH_SHORT).show();
                        }
                        saveRequest();
                        getRideViewModel.commitTempRequest();
                        navController.navigate(R.id.action_confrimRouteFragment_to_ridePendingFragment2);
                    } else if (bid < tempRequest.getPath().getEstimatedFare()) {
                        bidAmount.setText(Double.toString(tempRequest.getPath().getEstimatedFare()));
                        bidAmount.setError("Bid is less than recommended amount");

                    } else if (tempRequest.getPath().getEstimatedFare() > balance) {
                        dialog = new AlertDialog.Builder(getActivity()).create();
                        dialog.setTitle("You do not have enough QRBucks in your wallet!");
                        dialog.setMessage("Your balance of $".concat(String.format("%.2f", balance).concat(" is not enough to cover the minimum fare. Please add funds to your wallet.")));
                        dialog.setCancelable(false);
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Add Funds", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                navController.navigate(R.id.action_to_wallet);
                            }
                        });
                        dialog.show();
                    } else if (bid > balance) {
                        dialog = new AlertDialog.Builder(getActivity()).create();
                        dialog.setTitle("You do not have enough QRBucks in your wallet!");
                        dialog.setMessage("Your balance of $".concat(String.format("%.2f", balance).concat(" is not enough to cover your bid. Please lower your bid or add funds to your wallet.")));
                        dialog.setCancelable(false);
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Add Funds", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                navController.navigate(R.id.action_to_wallet);
                            }
                        });
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Lower Bid", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }
                catch (Exception exception) {
                    Toast.makeText(getContext(), "Invalid Bid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setBalance(User user) {
        balance = user.getBalance();
    }

    private void setTextViews() {
        if (tempRequest.getPath().getStartLocation().hasUniqueName) {
            pickupTextView.setText(tempRequest.getPath().getStartLocation().getLocationName());
        }
        else {
            pickupTextView.setText(tempRequest.getPath().getStartLocation().getAddress());
        }

        if (tempRequest.getPath().getDestination().hasUniqueName) {
            destinationTextView.setText(tempRequest.getPath().getDestination().getLocationName());
        }
        else {
            destinationTextView.setText(tempRequest.getPath().getDestination().getAddress());
        }
    }

    private void saveRequest() {
        getRideViewModel.saveTempRequest(tempRequest);
    }

    @Override
    public void initMap(Bundle savedInstanceState) {
        super.initMap(savedInstanceState);
        calculateDirections(tempRequest);
    }

    @Override
    protected void drawPath(DirectionsResult result, Request request) {
        super.drawPath(result, request);
        if (result.routes.length >  0) {
            setRouteValues(result.routes[0]);
        }
    }

    public void setRouteValues(DirectionsRoute route) {
        long distance = route.legs[0].distance.inMeters;
        long duration = route.legs[0].duration.inSeconds;
        tempRequest.getPath().generateEstimatedFare(distance, duration);
        try {
            tempRequest.setPrice(tempRequest.getPath().getEstimatedFare());
            bidAmount.setText(String.valueOf(tempRequest.getPrice()));
        }
        catch (Exception exception) {
            Toast.makeText(getContext(), "Estimated Bid Error", Toast.LENGTH_SHORT).show();
        }
    }
}

