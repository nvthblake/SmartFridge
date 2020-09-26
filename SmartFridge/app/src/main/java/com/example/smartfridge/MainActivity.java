package com.example.smartfridge;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.smartfridge.TaskProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TaskProvider taskProvider = null;

        // Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.ingredientFragment, R.id.profileFragment, R.id.recipeFragment, R.id.scanFragment, R.id.shoppingFragment).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Initiate database
        try {
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("smartfridge", MODE_PRIVATE, null);
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS FactFridge (ID INTEGER PRIMARY KEY, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, IngredientName VARCHAR, Amount INT(5), Unit VARCHAR, ImageID VARCHAR)");
            if (taskProvider.checkForTableNotExists(sqLiteDatabase, "FactFridge"))
            {
                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageID) VALUES ('steak',2, 'lbs', 'steak.png')");
                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageID) VALUES ('asparagus',1, 'bunch', 'asparagus.png')");
            }

            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM FactFridge", null);
            int IngredientNameIndex = c.getColumnIndex("IngredientName");
            int ImageIndex = c.getColumnIndex("ImageID");
            int IDIndex = c.getColumnIndex("ID");
            c.moveToFirst();

            while (c != null) {
                Log.i("IngredientName ", c.getString(IngredientNameIndex));
                Log.i("Image ", c.getString(ImageIndex));
                Log.i("ID ", Integer.toString(c.getInt(IDIndex)));

                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}