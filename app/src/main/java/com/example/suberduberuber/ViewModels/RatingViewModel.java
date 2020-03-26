package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Repositories.RatingRepository;
import com.example.suberduberuber.Repositories.UserRepository;

public class RatingViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private RatingRepository ratingRepository;

    public RatingViewModel (Application application) {
        super(application);
        userRepository = new UserRepository();
        ratingRepository = new RatingRepository();
    }

    public void updateDriverRating(String userUID, Driver driver, double rating) {
        // calculation for the new rating value
        double newRating = ((driver.getRating() * driver.getNumberOfRatings()) + rating) / (driver.getNumberOfRatings() + 1);

        ratingRepository.saveRating(userUID, newRating);
        ratingRepository.saveNumOfRatings(userUID, driver.getNumberOfRatings() + 1);
    }

}
