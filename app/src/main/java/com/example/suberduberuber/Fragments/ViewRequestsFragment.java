package com.example.suberduberuber.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Adapters.AvailableRequestListAdapter;
import com.example.suberduberuber.ViewModels.ViewRequestsViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewRequestsFragment extends Fragment implements AvailableRequestListAdapter.RequestCardTouchListener {

    private ViewRequestsViewModel viewRequestsViewModel;
    private RecyclerView requestRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AvailableRequestListAdapter adapter;
    private NavController navController;

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

        navController = Navigation.findNavController(view);
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
}
