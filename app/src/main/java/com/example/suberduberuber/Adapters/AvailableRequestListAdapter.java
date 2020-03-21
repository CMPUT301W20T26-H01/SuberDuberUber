package com.example.suberduberuber.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class AvailableRequestListAdapter extends ArrayAdapter<Request> {

    private ArrayList<Request> listOfRequests;
    private Context context;

    public AvailableRequestListAdapter(Context context, ArrayList<Request> listOfRequests) {
        super(context, 0, listOfRequests);
        this.listOfRequests = listOfRequests;
        this.context = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_request, parent, false);
        }

        Request request = listOfRequests.get(position);

        TextView destination = view.findViewById(R.id.destinationField);
        TextView pickup = view.findViewById(R.id.pickupField);
        TextView pickupTime = view.findViewById(R.id.pickupTimeField);
        TextView driver = view.findViewById(R.id.driverField);
        TextView status = view.findViewById(R.id.statusField);

        // set fields
        Path path = request.getPath();
        if (path.getDestination().getLocationName() != null) {
            destination.setText(path.getDestination().getLocationName());
        } else if (path.getDestination().getAddress() != null) {
            destination.setText(path.getDestination().getAddress());
        } else if (path.getDestination().getCoordinate() != null) {
            destination.setText((path.getDestination().getCoordinate()));
        } else {
            destination.setText("Unknown Location");
        }

        if (path.getStartLocation().getLocationName() != null) {
            pickup.setText(path.getStartLocation().getLocationName());
        } else if (path.getStartLocation().getAddress() != null) {
            pickup.setText(path.getStartLocation().getAddress());
        } else if (path.getStartLocation().getCoordinate() != null) {
            pickup.setText((path.getStartLocation().getCoordinate()));
        } else {
            pickup.setText("Unknown Location");
        }

        pickupTime.setText(request.getTime().toString());
        status.setText(request.getStatus());
        if (Objects.equals(request.getStatus(), "initiated")) {
            driver.setText("waiting..");
        } else {
            // get driver username
            driver.setText("not implemented");
        }

        return view;
    }
}
