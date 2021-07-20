package com.example.cookbook.data;


import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeAPI {

    @GET("ddCErJnH")
    Call<Recipe[]> loadRecipe();
}
