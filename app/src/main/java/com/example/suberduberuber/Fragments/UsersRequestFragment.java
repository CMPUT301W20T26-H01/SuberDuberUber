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
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suberduberuber.Adapters.UsersRequestsAdapter;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import java.util.ArrayList;

public class UsersRequestFragment extends Fragment implements UsersRequestsAdapter.UsersRequestTouchListener {

    private UsersRequestsAdapter requestListAdapter;

    private GetRideViewModel getRideViewModel;
    private AuthViewModel authViewModel;

    private RecyclerView requestRecyclerView;
    private RecyclerView.LayoutManager layoutManager;


    public UsersRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        requestRecyclerView = view.findViewById(R.id.users_request_list);
        configureRecyclerView();

        notifyDataSetChange();
    }

    private void configureRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        requestListAdapter = new UsersRequestsAdapter(this);

        requestRecyclerView.setLayoutManager(layoutManager);
        requestRecyclerView.setAdapter(requestListAdapter);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(requestRecyclerView.getContext(), R.anim.layout_anim_fall_down);
        requestRecyclerView.setLayoutAnimation(animationController);
    }

    @Override
    public void shrinkAllPopups() {
        int numItems = layoutManager.getItemCount();
        for(int i = 0 ; i < numItems; i++) {
            UsersRequestsAdapter.UsersRequestsViewHolder viewHolder = (UsersRequestsAdapter.UsersRequestsViewHolder) requestRecyclerView.findViewHolderForAdapterPosition(i);
            if(viewHolder != null) {
                viewHolder.shrink();
            }
        }
    }

    @Override
    public void onRequestCancel(int position) {
        getRideViewModel.cancelRequest(requestListAdapter.getRequestAtPosition(position));
        notifyDataSetChange();
    }

    public void notifyDataSetChange() {
        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                getRideViewModel.getCurrentUsersRequests(user).observe(getViewLifecycleOwner(), new Observer<ArrayList<Request>>() {
                    @Override
                    public void onChanged(ArrayList<Request> requests) {
                        requestListAdapter.setRequestDataset(requests);
                    }
                });
            }
        });
    }
}
