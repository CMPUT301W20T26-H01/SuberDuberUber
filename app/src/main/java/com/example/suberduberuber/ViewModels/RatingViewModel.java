package com.example.suberduberuber.ViewModels;

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
