package com.example.suberduberuber.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class UsersRequestsAdapter extends RecyclerView.Adapter<UsersRequestsAdapter.UsersRequestsViewHolder> {

    private UsersRequestTouchListener usersRequestTouchListener;

    List<Request> dataset = new ArrayList<Request>();

    public UsersRequestsAdapter(UsersRequestTouchListener listener) {
        this.usersRequestTouchListener = listener;
    }

    @NonNull
    @Override
    public UsersRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_request, parent, false);
        return new UsersRequestsViewHolder(v, usersRequestTouchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersRequestsViewHolder holder, int position) {

        Request request = dataset.get(position);

        // set fields
        Path path = request.getPath();
        if (path.getDestination().getLocationName() != null) {
            holder.destination.setText(path.getDestination().getLocationName());
        } else if (path.getDestination().getAddress() != null) {
            holder.destination.setText(path.getDestination().getAddress());
        } else if (path.getDestination().getCoordinate() != null) {
            holder.destination.setText((path.getDestination().getCoordinate()));
        } else {
            holder.destination.setText("Unknown Location");
        }

        if (path.getStartLocation().getLocationName() != null) {
            holder.pickupLocation.setText(path.getStartLocation().getLocationName());
        } else if (path.getStartLocation().getAddress() != null) {
            holder.pickupLocation.setText(path.getStartLocation().getAddress());
        } else if (path.getStartLocation().getCoordinate() != null) {
            holder.pickupLocation.setText((path.getStartLocation().getCoordinate()));
        } else {
            holder.pickupLocation.setText("Unknown Location");
        }

        holder.time.setText(request.getTime().toString());
        if (Objects.equals(request.getStatus(), "PENDING_ACCEPTANCE")) {
            holder.driver_username.setText("None");
        } else {
            holder.driver_username.setText(request.getDriver().getUsername());
        }
        holder.status.setText(request.getStatus());
    }

    @Override
    public int getItemCount() {
        if(dataset != null) {
            return dataset.size();
        }
        else {
            return 0;
        }
    }

    public Request getRequestAtPosition(int position) {
        return dataset.get(position);
    }

    public static class UsersRequestsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView pickupLocation;
        private TextView destination;
        private TextView time;
        private TextView driver_username;
        private TextView status;
        private UsersRequestTouchListener usersRequestTouchListener;

        private ConstraintLayout popup;
        private Button cancelRideButton;
        private Button backButton;
        private View view;

        public UsersRequestsViewHolder(View v, UsersRequestTouchListener listener) {
            super(v);
            v.setOnClickListener(this);
            this.usersRequestTouchListener = listener;
            this.destination = v.findViewById(R.id.destinationField);
            this.pickupLocation = v.findViewById(R.id.pickupField);
            this.time = v.findViewById(R.id.pickupTimeField);
            this.driver_username = v.findViewById(R.id.driverField);
            this.status = v.findViewById(R.id.statusField);

            this.popup = v.findViewById(R.id.request_card_poppup_details);
            this.backButton = v.findViewById(R.id.back_button);
            this.cancelRideButton = v.findViewById(R.id.cancel_button);
            this.view = v;
        }

        @Override
        public void onClick(View v) {
            if (Objects.equals(this.status.getText(), "PENDING_ACCEPTANCE") | Objects.equals(this.status.getText(), "ACCEPTED")) {
                usersRequestTouchListener.shrinkAllPopups();
                togglePopupState();
                setButtonListeners();
            }
        }

        public void shrink() {
            popup.setVisibility(View.GONE);
            view.setBackgroundColor(Color.WHITE);
        }
        private void togglePopupState() {
            if(popup.getVisibility() == View.GONE) {
                popup.setVisibility(View.VISIBLE);
                view.setBackgroundColor(Color.GREEN);
            }
            else {
                popup.setVisibility(View.GONE);
                view.setBackgroundColor(Color.WHITE);
            }
        }

        private void setButtonListeners() {
            this.cancelRideButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    usersRequestTouchListener.onRequestCancel(getAdapterPosition());
                }
            });

            this.backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    togglePopupState();
                }
            });
        }
    }

    public void setRequestDataset(List<Request> requests) {
        dataset = requests;
        notifyDataSetChanged();
    }

    public interface UsersRequestTouchListener {
        void onRequestCancel(int position);
        void shrinkAllPopups();
    }
}
