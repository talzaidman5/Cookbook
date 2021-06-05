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

public class MainActivity extends AppCompatActivity {
    private Button main_BTN_new_recipe, main_BTN_view_all_recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        main_BTN_view_all_recipe = findViewById(R.id.main_BTN_view_all_recipe);
        main_BTN_new_recipe = findViewById(R.id.main_BTN_new_recipe);




        main_BTN_new_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openNewActivityNewRecipe();
                openAlert();
            }
        });

        main_BTN_view_all_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityAllRecipes();
            }
        });
    }


    private void openAlert(){
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                // A null listener allows the button to dismiss the dialog and take no further action.
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

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
    private void openNewActivityAllRecipes() {
        Intent intent = new Intent(this, allRecipe.class);
        startActivity(intent);
        finish();
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
