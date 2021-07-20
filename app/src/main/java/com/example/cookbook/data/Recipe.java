package com.example.cookbook.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Recipe {
    private String name;
    private int type;
    private ArrayList<String> ingredient = new ArrayList<>();
    private String preparation;
    private Bitmap imageRecipe;
    private String image;
    private boolean isImage;

    public Recipe(String name, int type, ArrayList<String> ingredient, String preparation) {
        this.name = name;
        this.type = type;
        this.ingredient = ingredient;
        this.preparation = preparation;
        this.isImage = false;
    }

    public Recipe(Recipe other) {
        this.name = other.name;
        this.type = other.type;
        this.ingredient = other.ingredient;
        this.preparation = other.preparation;
        this.imageRecipe = other.imageRecipe;

    }

    public Recipe() {
    }

    public Recipe(String name, int type, Bitmap image) {
        this.name = name;
        this.type = type;
        this.imageRecipe = image;
        this.isImage = true;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] compressImage = baos.toByteArray();
        String sEncodedImage = Base64.encodeToString(compressImage, Base64.DEFAULT);

        this.image = sEncodedImage;
    }

//    public boolean isImage() {
//        return isImage;
//    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<String> getIngredient() {
        return ingredient;
    }

    public void setIngredient(ArrayList<String> ingredient) {
        this.ingredient = ingredient;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(name, recipe.name);
    }


    public Bitmap textAsBitmap(int width, int height) {

        if (this.isImage == false) {
            List<String> text = toStringOr();
            int textSize = 100;
            int textColor = Color.parseColor("#00ff00");
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(textSize);
            paint.setColor(textColor);
            paint.setTextAlign(Paint.Align.LEFT);
            float baseline = -paint.ascent(); // ascent() is negative

            Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(image);
            for (int i = 0; i < text.size()-1; i++) {
                canvas.drawText(text.get(i), 0, baseline + (i * 120), paint);
            }
            return image;

        } else
            return imageRecipe;
    }

    public Bitmap getImageRecipe() {
        return imageRecipe;
    }

    public void setImageRecipe(Bitmap imageRecipe) {
        this.imageRecipe = imageRecipe;
    }

    public List toStringOr() {
        List res = new ArrayList();
        res.add("מצרכים: ");
        for (int i = 0; i < ingredient.size(); i++) {
            res.add(ingredient.get(i));

        }
        res.add("שלבי הכנה:");
        res.add(this.preparation);

        return res;
    }


    @NonNull
    @Override
    public String toString() {
        List res = new ArrayList();

        String s = "מצרכים: \n";
        for (int i = 0; i < ingredient.size(); i++) {
            s += ingredient.get(i) + "\n";

        }
        s += "\nשלבי הכנה:\n" + this.preparation;

        return s;
    }
}
