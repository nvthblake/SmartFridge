package com.example.smartfridge;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
//    OkHttpClient client = new OkHttpClient();

    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TaskProvider taskProvider = new TaskProvider();
        DbBitmapUtility byteFromDrawableConverter = new DbBitmapUtility();
        String[] ingredientNameStr = new String[]{"strawberry", "steak", "asparagus", "peach"};
        int[] quantityStr = new int[]{1, 2, 1, 4};
        String[] unitStr = new String[]{"Boxes", "Pounds", "Null", "Null"};
        byte[] imageBPArr;
        String[] expStr = new String[]{"30/09/2020", "30/09/2020", "30/09/2020", "30/09/2020"};
        String[] categoryStr = new String[]{"Fruit", "Meat", "Vegetable", "Fruit"};


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
//            sqLiteDatabase.execSQL("DROP TABLE FactFridge");
//            sqLiteDatabase.execSQL("DELETE FROM FactFridge WHERE ID >= 3");
            if (taskProvider.checkForTableNotExists(sqLiteDatabase, "FactFridge")) {
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS FactFridge (ID INTEGER PRIMARY KEY, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, IngredientName VARCHAR, Amount INT(5), Unit VARCHAR, ImageBP BLOB, InFridge INT(1), ExpirationDate VARCHAR, Category VARCHAR)");
                ContentValues cv = new ContentValues();
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_fastfood_50);
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                byte[] img = bos.toByteArray();

                for (int i = 0; i < 5; i++) {
                    cv.put("IngredientName", ingredientNameStr[i]);
                    cv.put("Amount", quantityStr[i]);
                    cv.put("Unit", unitStr[i]);
                    cv.put("InFridge", 1);
                    cv.put("ExpirationDate", expStr[i]);
                    cv.put("Category", categoryStr[i]);
//                    cv.put("ImageBP", img);
                    sqLiteDatabase.insert("FactFridge", null, cv);
                }

//                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageBP, InFridge, ExpirationDate, Category) VALUES ('strawberry',3, 'box', NULL, 1, '30/09/2020', 'Fruit')");
//                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageBP, InFridge, ExpirationDate, Category) VALUES ('steak',2, 'lbs', NULL, 1, '30/09/2020', 'Meat')");
//                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageBP, InFridge, ExpirationDate, Category) VALUES ('asparagus',1, 'bunch', NULL, 1, '30/09/2020', 'Vegetable')");
//                sqLiteDatabase.execSQL("INSERT INTO FactFridge (IngredientName, Amount, Unit, ImageBP, InFridge, ExpirationDate, Category) VALUES ('peach',1, 'fruit', NULL, 1, '30/09/2020', 'Fruit')");
            }

            // Create schema and data for table that saves ingredients within app's inventory
            if (taskProvider.checkForTableNotExists(sqLiteDatabase, "DimIngredient")) {
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
//            sqLiteDatabase.execSQL("DROP TABLE DimRecipe");
            if (taskProvider.checkForTableNotExists(sqLiteDatabase, "DimRecipe")) {
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS DimRecipe (id INTEGER, title VARCHAR, image VARCHAR, imageType VARCHAR, usedIngredientCount INTEGER, missedIngredientCount INTEGER, likes INTEGER)");
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 47950);
                contentValues.put("title", "Cinnamon Apple Crisp");
                contentValues.put("image", "https://spoonacular.com/recipeImages/47950-312x231.jpg");
                contentValues.put("imageType", "jpg");
                contentValues.put("usedIngredientCount", 1);
                contentValues.put("missedIngredientCount", 2);
                contentValues.put("likes", 35);
                sqLiteDatabase.insert("DimRecipe", null, contentValues);

            }

            // Create view showing all items expiring within 3 days
            sqLiteDatabase.execSQL("DROP VIEW IF EXISTS ItemsExpDays");
            sqLiteDatabase.execSQL("CREATE VIEW IF NOT EXISTS ItemsExpDays (ID, IngredientName, TimeDelta, ImageBP, Amount, Category) AS SELECT ID, IngredientName, JulianDay(substr(ExpirationDate, 7) || \"-\" || substr(ExpirationDate,4,2)  || \"-\" || substr(ExpirationDate, 1,2)) - JulianDay('now'), ImageBP, Amount, Category FROM FactFridge WHERE InFridge = 1;");

            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM DimRecipe", null);
            int IngredientNameIndex = c.getColumnIndex("title");
//            int TimeDeltaIndex = c.getColumnIndex("TimeDelta");
            c.moveToFirst();

            while (c != null) {
                Log.i("Recipe Title ", c.getString(IngredientNameIndex));
//                Log.i("TimeDelta ", Integer.toString(c.getInt(TimeDeltaIndex)));

                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}