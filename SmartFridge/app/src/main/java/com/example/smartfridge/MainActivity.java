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
        TaskProvider taskProvider = new TaskProvider();

        // Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.ingredientFragment, R.id.profileFragment, R.id.recipeFragment, R.id.scanFragment, R.id.shoppingFragment).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Initiate database
        try {
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("smartfridge", MODE_PRIVATE, null);

            // Create schema for table that saves user's food inventory
            sqLiteDatabase.execSQL("DROP TABLE FactFridge");
            if (taskProvider.checkForTableNotExists(sqLiteDatabase, "FactFridge"))
            {
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS FactFridge (ID INTEGER PRIMARY KEY, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, IngredientName VARCHAR, Amount INT(5), Unit VARCHAR, ImageID VARCHAR, InFridge INT(1), ExpirationDate VARCHAR, Category VARCHAR)");
                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageID, InFridge, ExpirationDate, Category) VALUES ('strawberry',3, 'box', 'strawberry.png', 1, '30/09/2020', 'Fruit')");
                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageID, InFridge, ExpirationDate, Category) VALUES ('steak',2, 'lbs', 'steak.png', 1, '30/09/2020', 'Meat')");
                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageID, InFridge, ExpirationDate, Category) VALUES ('asparagus',1, 'bunch', 'asparagus.png', 1, '30/09/2020', 'Vegetable')");
                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageID, InFridge, ExpirationDate, Category) VALUES ('peach',1, 'fruit', 'peach.png', 1, '30/09/2020', 'Fruit')");
            }

            // Create schema and data for table that saves ingredients within app's inventory
            if (taskProvider.checkForTableNotExists(sqLiteDatabase, "DimIngredient"))
            {
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS DimIngredient (ID INTEGER PRIMARY KEY, IngredientName VARCHAR, Category VARCHAR, Perishable INT, EstimatedPerishDay INT(4))");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('ground cinnamon', 'Condiment', 0, NULL)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('eggs', 'Meat', 1, 14)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('ground beef', 'Meat', 1, 14)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('strawberry', 'Fruit', 1, 5)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('steak', 'Meat', 1, 14)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('asparagus', 'Vegetable', 1, 5)");
                sqLiteDatabase.execSQL("INSERT INTO DimIngredient (IngredientName, Category, Perishable, EstimatedPerishDay) VALUES ('peach', 'Fruit', 1, 10)");
            }

            // Create schema and data for table that saves recipes within app's inventory
            if (taskProvider.checkForTableNotExists(sqLiteDatabase, "DimRecipe")) {
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS DimRecipe (ID INTEGER PRIMARY KEY, Title VARCHAR, SourceName VARCHAR, ReadyInMinutes INT(3), Servings INT(2), SourceUrl VARCHAR)");
                sqLiteDatabase.execSQL("INSERT INTO DimRecipe (Title, SourceName, ReadyInMinute, Servings, SourceUrl) VALUES ('400-Calorie Breakfasts Peach Parfait ','Martha Stewart',7,1,'http://www.prevention.com/food/healthy-recipes/400-calorie-breakfasts?s=6')");
                sqLiteDatabase.execSQL("INSERT INTO DimRecipe (Title, SourceName, ReadyInMinute, Servings, SourceUrl) VALUES ('Sloppy Joe Casserole','Cravings of a Lunatic',30,6,'http://www.cravingsofalunatic.com/2013/10/sloppy-joe-casserole.html')");
            }

            // Create view showing all items expiring within 3 days
//            sqLiteDatabase.execSQL("DROP VIEW ItemsExpDays");
            sqLiteDatabase.execSQL("CREATE VIEW IF NOT EXISTS ItemsExpDays (IngredientName, TimeDelta, ImageID, Amount, Category) AS SELECT IngredientName, JulianDay(substr(ExpirationDate, 7) || \"-\" || substr(ExpirationDate,4,2)  || \"-\" || substr(ExpirationDate, 1,2)) - JulianDay('now'), ImageID, Amount, Category FROM FactFridge WHERE InFridge = 1;");

            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM ItemsExpDays", null);
            int IngredientNameIndex = c.getColumnIndex("IngredientName");
            int TimeDeltaIndex = c.getColumnIndex("TimeDelta");
            c.moveToFirst();

            while (c != null) {
                Log.i("IngredientName ", c.getString(IngredientNameIndex));
                Log.i("TimeDelta ", Integer.toString(c.getInt(TimeDeltaIndex)));

                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}