package com.example.suberduberuber.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.suberduberuber.Adapters.RequestListAdapter;
import com.example.suberduberuber.Models.CustomLocation;
import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.GetRideViewModel;

import java.util.ArrayList;

public class requestFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Request> requestListAdapter;

    private GetRideViewModel getRideViewModel;

    public requestFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        listView = view.findViewById(R.id.requestList);

        setListView();
    }

    public void setListView() {
        getRideViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                getRideViewModel.getCurrentUsersRequests(user).observe(getViewLifecycleOwner(), new Observer<ArrayList<Request>>() {
                    @Override
                    public void onChanged(ArrayList<Request> requests) {
                        requestListAdapter = new RequestListAdapter(getParentFragment().getContext(), requests);
                        listView.setAdapter(requestListAdapter);
                    }
                });
            }
        });
    }
}
