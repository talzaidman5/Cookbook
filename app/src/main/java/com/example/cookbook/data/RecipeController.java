package com.example.cookbook.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeController {

    final String BASE_URL = "https://pastebin.com/raw/";
    private CallBack_Recipe callBack_Recipe;

    Callback<Recipe[]> RecipeCallBack = new Callback<Recipe[]>() {
        @Override
        public void onResponse(Call<Recipe[]> call, Response<Recipe[]> response) {
            if (response.isSuccessful()) {
                Recipe[] Recipe = response.body();

                if (callBack_Recipe != null) {
                    callBack_Recipe.Recipe(Recipe);
                }
            } else {
                System.out.println(response.errorBody());
            }
        }

        @Override
        public void onFailure(Call<Recipe[]> call, Throwable t) {
            t.printStackTrace();
        }
    };



    public void fetchAllRecipe(CallBack_Recipe callBack_Recipe) {
        this.callBack_Recipe = callBack_Recipe;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RecipeAPI RecipeAPI = retrofit.create(RecipeAPI.class);

        Call<Recipe[]> call = RecipeAPI.loadRecipe();
        call.enqueue(RecipeCallBack);
    }

    public interface CallBack_Recipe {
        void Recipe (Recipe[] Recipe1);
    }

}
