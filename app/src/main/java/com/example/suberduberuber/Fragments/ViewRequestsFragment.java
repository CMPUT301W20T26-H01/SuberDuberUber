package com.example.suberduberuber.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Adapters.RequestAdapter;
import com.example.suberduberuber.ViewModels.ViewRequestsViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewRequestsFragment extends Fragment {

    private ViewRequestsViewModel viewRequestsViewModel;
    private RecyclerView requestRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RequestAdapter adapter;

    public ViewRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewRequestsViewModel = new ViewModelProvider(getActivity()).get(ViewRequestsViewModel.class);

        requestRecyclerView = view.findViewById(R.id.request_list);
        configureRecyclerView();

        viewRequestsViewModel.getAllRequests().observe(getViewLifecycleOwner(), new Observer<List<Request>>() {
            @Override
            public void onChanged(List<Request> requests) {
                adapter.setRequestDataset(requests);
            }
        });
    }

    private void configureRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new RequestAdapter();
        requestRecyclerView.setHasFixedSize(true);

        requestRecyclerView.setLayoutManager(layoutManager);
        requestRecyclerView.setAdapter(adapter);
    }
}
