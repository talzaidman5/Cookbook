package com.example.cookbook.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cookbook.R;
import com.example.cookbook.data.Account;
import com.example.cookbook.data.MySheredP;
import com.example.cookbook.data.Recipe;
import com.google.gson.Gson;


public class newRecipeImg extends AppCompatActivity {
    private Spinner type;
    private Button back, new_BTN_add;
    private Context context;
    private EditText new_TXT_name;
    private ImageView imageView;
    private MySheredP msp;
    private Gson gson = new Gson();
    Bitmap selectedImage;

    public static final String KEY_Account = "account";
    private Account account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_recipe_image);

        msp = new MySheredP(this);
        getFromMSP();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        findViews();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivityMain();
            }
        });

        new_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int typeNew = (int) type.getSelectedItemId();
                Recipe recpie = new Recipe(new_TXT_name.getText().toString(), typeNew, selectedImage);
                if (typeNew == 3)
                    account.addToDessert(recpie);

                if (typeNew == 1)
                    account.addToMain(recpie);

                if (typeNew == 0)
                    account.addToFirsts(recpie);


                if (typeNew == 2)
                    account.addToAdds(recpie);
                putOnMSP();
                openActivityMain();

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, R.layout.sppiner_style);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectImage();

    }

    private void openActivityMain() {
        Intent intent = new Intent(this, Recipes_Adds.class);
        startActivity(intent);
        finish();
    }

    private void putOnMSP() {
        String accountTemp = gson.toJson(account);
        msp.putString(KEY_Account, accountTemp);

    }

    private void getFromMSP() {

        String data = msp.getString(KEY_Account, "NA");

        account = new Account(data);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                         selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void findViews() {
        imageView = findViewById(R.id.imageView);
        back = findViewById(R.id.back);
        type = findViewById(R.id.type);
        new_BTN_add = findViewById(R.id.new_BTN_add);
        new_TXT_name = findViewById(R.id.new_TXT_name);
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
