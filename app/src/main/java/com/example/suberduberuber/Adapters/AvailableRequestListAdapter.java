package com.example.suberduberuber.Adapters;

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

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a custom adapter that is used to bind a list of data to the recycler view elements, and \
 * enables live updates of the data they contain.
 */
public class AvailableRequestListAdapter extends RecyclerView.Adapter<AvailableRequestListAdapter.RequestViewHolder> {

    private RequestCardTouchListener cardTouchListener;

    public AvailableRequestListAdapter(RequestCardTouchListener listener) {
        this.cardTouchListener = listener;
    }

    List<Request> dataset = new ArrayList<Request>();

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card, parent, false);
        return new RequestViewHolder(v, cardTouchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {

        Request request = dataset.get(position);

        holder.origin.setText(request.getPath().getStartLocation().getLocationName());
        holder.destination.setText(request.getPath().getDestination().getLocationName());
        holder.riderName.setText(request.getRequestingUser().getUsername());
        holder.price.setText(Double.toString(request.getPath().getEstimatedFare()));
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

    public static class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView origin;
        private TextView destination;
        private TextView riderName;
        private TextView price;
        private RequestCardTouchListener requestCardTouchListener;
        private ConstraintLayout popup;
        private Button acceptButton;
        private Button cancelButton;
        private View view;

        public RequestViewHolder(View v, RequestCardTouchListener listener) {
            super(v);
            v.setOnClickListener(this);
            this.requestCardTouchListener = listener;
            this.destination = v.findViewById(R.id.destination);
            this.origin = v.findViewById(R.id.origin);
            this.riderName = v.findViewById(R.id.rider_name);
            this.price = v.findViewById(R.id.suggested_price);
            this.popup = v.findViewById(R.id.request_card_poppup_details);
            this.acceptButton = v.findViewById(R.id.cancel_button);
            this.cancelButton = v.findViewById(R.id.back_button);
            this.view = v;
        }

        @Override
        public void onClick(View v) {
            requestCardTouchListener.shrinkAllPopups();
            togglePopupState();
            setButtonListeners();
        }

        public void shrink() {
            popup.setVisibility(View.GONE);
            view.setBackgroundColor(Color.WHITE);
        }
        private void togglePopupState() {
            if(popup.getVisibility() == View.GONE) {
                popup.setVisibility(View.VISIBLE);
            }
            else {
                popup.setVisibility(View.GONE);
                view.setBackgroundColor(Color.WHITE);
            }
        }

        private void setButtonListeners() {
            this.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestCardTouchListener.onRequestAccept(getAdapterPosition());
                }
            });

            this.cancelButton.setOnClickListener(new View.OnClickListener() {
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

    public interface RequestCardTouchListener {
        void onRequestAccept(int position);
        void shrinkAllPopups();
    }
}
