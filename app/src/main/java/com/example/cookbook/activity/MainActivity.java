package com.example.cookbook.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookbook.R;
import com.example.cookbook.data.KEYS;
import com.example.cookbook.data.MySheredP;
import com.example.cookbook.data.Recipe;
import com.example.cookbook.data.RecipeController;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button main_BTN_new_recipe, main_BTN_view_all_recipe;
    private String uuid;
    public ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    private MySheredP msp;
    private Gson gson = new Gson();
    public static final String KEY_RecipeList = "recipe_list";
    public static final String KEY_Account = KEYS.KEY_Account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msp = new MySheredP(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        main_BTN_view_all_recipe = findViewById(R.id.main_BTN_view_all_recipe);
        main_BTN_new_recipe = findViewById(R.id.main_BTN_new_recipe);

        downloadRecipes();
        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);


        main_BTN_new_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlertNewRecipe();
            }
        });

        main_BTN_view_all_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityAllRecipes();
            }
        });
    }


    private void openAlertNewRecipe(){
        new AlertDialog.Builder(this)
                .setTitle("New Recipe")
                .setPositiveButton("New recipe",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        openNewActivityNewRecipe();
                    }
                })

                .setNeutralButton(" new recipe from image ",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        openNewActivityNewRecipeImg();
                    }
                })

                .setNegativeButton(android.R.string.no,  null)

                .setIcon(android.R.drawable.ic_input_add)
                .show();

    }
    private void openNewActivityAllRecipes() {
        Intent intent = new Intent(this, allRecipe.class);
        startActivity(intent);
        finish();
    }
    private void putOnMSP() {
        String accountTemp = gson.toJson(recipeArrayList);
        msp.putString(KEY_RecipeList, accountTemp);
    }


    private void downloadRecipes() {

        new RecipeController().fetchAllRecipe(new RecipeController.CallBack_Recipe() {
            @Override
            public void Recipe(com.example.cookbook.data.Recipe[] Recipe1) {
                for (Recipe recipe : Recipe1) {
                    recipeArrayList.add(recipe);
                }
                putOnMSP();
            }
        });
    }
    private void openNewActivityNewRecipe() {
        Intent intent = new Intent(this, newRecipe.class);
        startActivity(intent);
        finish();
    }


    private void openNewActivityNewRecipeImg() {
        Intent intent = new Intent(this, newRecipeImg.class);
        startActivity(intent);
        finish();
    }
}
