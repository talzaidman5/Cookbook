package com.example.cookbook.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookbook.R;
import com.example.cookbook.data.Account;
import com.example.cookbook.data.KEYS;
import com.example.cookbook.data.MySheredP;
import com.example.cookbook.data.Recipe;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ReadRecipeAdds extends AppCompatActivity {
    private static final String KEY_RECIPE = KEYS.KEY_RECIPE;
    public static final String KEY_Account = KEYS.KEY_Account;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    private ListView read_LIST_all_ingredints;
    private TextView read_TNT_name;
    private ImageView read_title_lavels;
    private String s;
    private Recipe recipe;
    private List listNew = new ArrayList();
    private ArrayAdapter arrayAdapter;

    private Button list_remove, back;
    private MySheredP msp;
    private Gson gson = new Gson();
    private Account account;
    private ArrayList<Recipe> allRecipesAdds = new ArrayList<>();
    private int recipeIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_read_recipe);
        msp = new MySheredP(this);
        findViews();
        s = getIntent().getStringExtra(ReadRecipeAdds.KEY_RECIPE);

        recipe = new Gson().fromJson(s, Recipe.class);
        getIndex();

        read_TNT_name.setText(recipe.getName());
        if (recipe.getImage()!=null) {
            byte[] b = Base64.decode(recipe.getImage(), Base64.DEFAULT);

            Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);


            read_title_lavels.setImageBitmap(bitmapImage);
        } else
            read_title_lavels.setImageBitmap(recipe.textAsBitmap(read_title_lavels.getLayoutParams().width,
                    read_title_lavels.getLayoutParams().height));

        list_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRecipe();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityAdds();
            }
        });
    }



    private void removeRecipe() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("מחיקה");

        alertDialog.setMessage(" האם ברצונך למחוק את המתכון? ");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        account.getRecipesAdds().remove(recipeIndex);
                        putOnMSP();
                        openActivityAdds();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ביטול",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();

    }

    private void openActivityAdds() {
        Intent intent = new Intent(this, Recipes_Adds.class);
        startActivity(intent);
        finish();
    }


    private void findViews() {
        read_title_lavels = findViewById(R.id.read_title_lavels);
        list_remove = findViewById(R.id.list_remove);
        read_TNT_name = findViewById(R.id.read_TNT_name);
        back = findViewById(R.id.back);
    }

    private void getFromMSP() {

        String data = msp.getString(KEY_Account, "NA");

        account = new Account(data);
    }

    private void putOnMSP() {

        String accountTemp = gson.toJson(account);
        msp.putString(KEY_Account, accountTemp);

    }

    private void getIndex() {
        getFromMSP();
        for (int i = 0; i < account.getRecipesAdds().size(); i++) {
            if (account.getRecipesAdds().get(i).getName().equals(recipe.getName()))
                recipeIndex =  i;
        }
        recipeIndex =  -1;
    }

    @Override
    public void onBackPressed() {
        openActivityAdds();
    }
}