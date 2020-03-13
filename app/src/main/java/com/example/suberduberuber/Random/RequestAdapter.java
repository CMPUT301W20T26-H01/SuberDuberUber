package com.example.suberduberuber.Random;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    List<Request> dataset;
    private RequestCardTouchListener cardTouchListener;

    public RequestAdapter(RequestCardTouchListener listener) {
        this.cardTouchListener = listener;
    }

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

    public static class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView origin;
        private TextView destination;
        private TextView riderName;
        private TextView price;
        private RequestCardTouchListener requestCardTouchListener;

        public RequestViewHolder(View v, RequestCardTouchListener listener) {
            super(v);
            v.setOnClickListener(this);
            this.requestCardTouchListener = listener;
            this.destination = v.findViewById(R.id.destination);
            this.origin = v.findViewById(R.id.origin);
            this.riderName = v.findViewById(R.id.rider_name);
            this.price = v.findViewById(R.id.suggested_price);
        }

        @Override
        public void onClick(View v) {
            requestCardTouchListener.onRequestClick(getAdapterPosition());
        }
    }
    public void setRequestDataset(List<Request> requests) {
        dataset = requests;
    }

    public interface RequestCardTouchListener {
        void onRequestClick(int position);
    }
}
