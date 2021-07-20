package com.example.cookbook.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookbook.R;
import com.example.cookbook.data.Account;
import com.example.cookbook.data.KEYS;
import com.example.cookbook.data.MySheredP;
import com.example.cookbook.data.Recipe;
import com.example.cookbook.data.allAccounts;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;


public class newRecipe extends AppCompatActivity {
    private Spinner new_SPN_type;
    private EditText new_EDIT_name;
    private EditText popupTXT_preparation_method;
    private Button new_BTN_add, new_add_one_ingr, back;
    private Recipe newRecipe;
    private ListView listView_ingredient;
    private ArrayList<String> stringArrayList;
    ArrayAdapter<String> stringArrayAdapter;
    private Spinner new_SPN_ingredient;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");
    private String uuid;

    private ArrayList<Account> allAccounts = new ArrayList<>();
    private allAccounts tempAllAccounts;
    private Account account = new Account();
    private int counter = 0;
    private MySheredP msp;
    private Gson gson = new Gson();
    public static final String KEY_Account = KEYS.KEY_Account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_recipe);
        msp = new MySheredP(this);
        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        findViews();

        getFromMSP();


        stringArrayList = new ArrayList<>();
        stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringArrayList);


        listView_ingredient.setAdapter(stringArrayAdapter);

        stringArrayList = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringArrayList);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        new_SPN_ingredient.setAdapter(adapter);

        new_SPN_ingredient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                counter++;
                if (counter > 1) {
                    String text = parent.getItemAtPosition(position).toString();
                    listView_ingredient.setCacheColorHint(Color.WHITE);
                    stringArrayList.add(text);
                    stringArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        new_add_one_ingr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlert();
            }
        });

        listView_ingredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                openEditAlert(position);
            }
        });


        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this, R.array.type, R.layout.sppiner_style);
        adapterType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        new_SPN_type.setAdapter(adapterType);
        new_SPN_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new_BTN_add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (checkNullName() == 0) {
                    buildRecipe();
                    openNewActivityMain();
                    //  showInterstitial();
                } else if (checkNullName() == 1)
                    new_EDIT_name.setError("שדה חובה");

                else {
                    new_EDIT_name.setError(" כותרת קיימת ");
                }
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityMain();
            }
        });
    }

    private void openEditAlert(int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(newRecipe.this).create();
        alertDialog.setTitle("עריכה");

        final EditText input = new EditText(newRecipe.this);
        String all = stringArrayList.get(position);


        input.setText(all);
        alertDialog.setView(input);

        alertDialog.setMessage(" האם ברצונך לערוך את המצרך? ");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "שמור",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String newNameStr = input.getText().toString();
                        if (newNameStr.length() > 0) {
                            stringArrayList.set(position, newNameStr);
                        } else
                            stringArrayList.remove(position);

                        stringArrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ביטול",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "מחיקה",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        stringArrayList.remove(position);
                        stringArrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

        alertDialog.show();

    }

    private void openAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(newRecipe.this);
        alertDialog.setTitle("הוספת מרכיב חדש");
        alertDialog.setMessage("הכנס את שם הרכיב");

        final EditText input = new EditText(newRecipe.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);


        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String ingredient = input.getText().toString();
                        if (ingredient.length()>0) {
                            listView_ingredient.setCacheColorHint(Color.WHITE);
                            stringArrayList.add(ingredient.toString());
                            stringArrayAdapter.notifyDataSetChanged();
                        }
                        putOnMSP();
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void getFromMSP() {

        String data = msp.getString(KEY_Account, "NA");

        account = new Account(data);

    }


    private void findViews() {
        new_SPN_type = findViewById(R.id.new_SPN_type);
        new_EDIT_name = findViewById(R.id.new_EDIT_name);
        new_BTN_add = findViewById(R.id.new_BTN_add);
        new_add_one_ingr = findViewById(R.id.new_add_one_ingr);
        listView_ingredient = findViewById(R.id.listView_ingredient);
        new_SPN_ingredient = findViewById(R.id.new_SPN_ingredient);
        popupTXT_preparation_method = findViewById(R.id.popupTXT_preparation_method);
        back = findViewById(R.id.back);
    }


    private void buildRecipe() {
        int typeNew = (int) new_SPN_type.getSelectedItemId();
        newRecipe = new Recipe(new_EDIT_name.getText().toString(), typeNew, stringArrayList, popupTXT_preparation_method.getText().toString());
        if (typeNew == 3)
            account.addToDessert(newRecipe);

        if (typeNew == 1)
            account.addToMain(newRecipe);

        if (typeNew == 0)
            account.addToFirsts(newRecipe);


        if (typeNew == 2)
            account.addToAdds(newRecipe);

        putOnMSP();
    }

    @Override
    public void onBackPressed() {
        openNewActivityMain();
    }

    private void openNewActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void putOnMSP() {
        String accountTemp = gson.toJson(account);
        msp.putString(KEY_Account, accountTemp);
        myRef.child("Users").child(uuid).setValue(account);

    }

    private int checkNullName() {
        if (new_EDIT_name.getText().length() == 0)
            return 1;
        String typeSelected = new_SPN_type.getSelectedItem().toString();
        if (typeSelected.equals("ראשונות"))
            for (int i = 0; i < account.getRecipesFirsts().size(); i++) {
                if (account.getRecipesFirsts().get(i).getName().equals(new_EDIT_name.getText().toString()))
                    return 2;
            }
        if (typeSelected.equals("עיקריות"))
            for (int i = 0; i < account.getRecipesMain().size(); i++) {
                if (account.getRecipesMain().get(i).getName().equals(new_EDIT_name.getText().toString()))
                    return 2;
            }

        if (typeSelected.equals("תוספות"))
            for (int i = 0; i < account.getRecipesAdds().size(); i++) {
                if (account.getRecipesAdds().get(i).getName().equals(new_EDIT_name.getText().toString()))
                    return 2;
            }


        if (typeSelected.equals("קינוחים"))
            for (int i = 0; i < account.getRecipesDessert().size(); i++) {
                if (account.getRecipesDessert().get(i).getName().equals(new_EDIT_name.getText().toString()))
                    return 2;
            }

        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //   getMenuInflater().inflate(R.menu.menu_ads, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

    /*    if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

}
