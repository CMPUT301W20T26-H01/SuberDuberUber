package com.example.suberduberuber.Adapters;

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

/**
 * This is a custom adapter that is used to bind a list of data to the recycler view elements, and \
 * enables live updates of the data they contain.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    List<Request> dataset = new ArrayList<Request>();

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card, parent, false);
        return new RequestViewHolder(v);
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
        return dataset.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        private TextView origin;
        private TextView destination;
        private TextView riderName;
        private TextView price;
        public RequestViewHolder(View v) {
            super(v);
            this.destination = v.findViewById(R.id.destination);
            this.origin = v.findViewById(R.id.origin);
            this.riderName = v.findViewById(R.id.rider_name);
            this.price = v.findViewById(R.id.suggested_price);
        }
    }

    public void setRequestDataset(List<Request> requests) {

        dataset = requests;
        notifyDataSetChanged();
    }
}
