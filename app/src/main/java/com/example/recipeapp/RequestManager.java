package com.example.recipeapp;

import android.content.Context;

import com.example.recipeapp.Listeners.RandomRecipeResponseListener;
import com.example.recipeapp.Models.RandomRecipeApiResponse;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    // Data Members
    Context context;

    // Retrofit is used for displaying the images on the screen
    // In a concise manner
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // This is for setting the Context
    public RequestManager(Context context) {
        this.context = context;
    }

    // This method actually does the calling of the API
    public void getRandomRecipe(RandomRecipeResponseListener listener, List<String> tags) {

        // Call the retrofit service to create the call random recipe class
        CallRandomRecipe callRandomRecipe = retrofit.create(CallRandomRecipe.class);

        // Call the random recipe endpoint and pass the number of recipes to return
        Call<RandomRecipeApiResponse> call = callRandomRecipe.callRandomRecipe(context.getString(R.string.api_key), "10", tags);

        // This part actually calls the API and displays an error message if the API call fails
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    // When this interface is used it passes
    // 1. the API key
    // 2. the number of calls to make
    // 3. The tag to be called
    private interface CallRandomRecipe {
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags") List<String> tags

        );
    }
}
