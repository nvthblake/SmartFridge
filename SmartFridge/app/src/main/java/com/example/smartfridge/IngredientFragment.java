package com.example.smartfridge;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientFragment extends Fragment {
    HorizontalScrollView scrollView;
    GridView gridView;
    SQLiteDatabase sqLiteDatabase;
    int fridgeCap;
    int fridgeCapExp;


//    String[] name = new String[]{"Steak", "Carrot", "Onion", "Mushroom", "Chicken", "Shit", "Rice","Carrot","Carrot","Carrot","Carrot","Carrot","Carrot","Carrot","Carrot"};
//    int[] image = new int[]{R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50,
//            R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50,
//            R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50,};
//    int[] expDate = new int[]{5, 8, 3, 4, 10, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1,};
//    int[] qty = new int[]{1, 2, 3, 10, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,};
    String[] name;
    int[] image;
    int[] qty;
    int[] expDate;
    String[] nameExp;
    int[] imageExp;
    private Button btnAll;
    private Button btnVegetable;
    private Button btnMeat;
    private Button btnFruit;
    private Button btnCondiment;
    private Button btnSnack;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IngredientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientFragment newInstance(String param1, String param2) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void filterIngredient(String ingreCategory, SQLiteDatabase sqLiteDatabase) {
        String sql;
        String sqlExpSoon;

        if (ingreCategory == "All") {
            sql = "SELECT * FROM ItemsExpDays";
            sqlExpSoon = "SELECT * FROM ItemsExpDays WHERE TimeDelta <= 7 ORDER BY TimeDelta";
        } else {
            sql = "SELECT * FROM ItemsExpDays WHERE Category = '" + ingreCategory + "'";
            sqlExpSoon = "SELECT * FROM ItemsExpDays WHERE TimeDelta <= 7 AND Category = '" + ingreCategory + "' ORDER BY TimeDelta";
        }

        // Update items regardless of expiration date
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        int IngredientNameIndex = c.getColumnIndex("IngredientName");
        int TimeDeltaIndex = c.getColumnIndex("TimeDelta");
        int ImageIndex = c.getColumnIndex("ImageID");
        int QuantityIndex = c.getColumnIndex("Amount");
        int CategoryIndex = c.getColumnIndex("Category");

        fridgeCap = c.getCount();

        name = new String[fridgeCap];
        image = new int[fridgeCap];
        expDate = new int[fridgeCap];
        qty = new int[fridgeCap];

        int i = 0;
        c.moveToFirst();

        while (!c.isAfterLast()) {
            Log.i("Food ", c.getString(IngredientNameIndex));
//            Log.i("Category ", c.getString(CategoryIndex));
            name[i] = c.getString(IngredientNameIndex);
            image[i] = R.drawable.ic_baseline_fastfood_50;
            expDate[i] = c.getInt(TimeDeltaIndex);
            qty[i] = c.getInt(QuantityIndex);
            i++;
            c.moveToNext();
        }
        c.close();

        // Update items expiring soon
        Cursor cExp = sqLiteDatabase.rawQuery(sqlExpSoon, null);
        int IngredientNameIndexExp = c.getColumnIndex("IngredientName");
        int ImageIndexExp = c.getColumnIndex("ImageID");

        fridgeCapExp = cExp.getCount();

        nameExp = new String[fridgeCapExp];
        imageExp = new int[fridgeCapExp];

        int iExp = 0;
        cExp.moveToFirst();

        while (!cExp.isAfterLast()) {
            nameExp[iExp] = cExp.getString(IngredientNameIndexExp);
            imageExp[iExp] = R.drawable.ic_baseline_fastfood_50;
            iExp++;
            cExp.moveToNext();
        }
        cExp.close();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_ingredient, container, false);

        sqLiteDatabase = sqLiteDatabase.openDatabase("/data/data/com.example.smartfridge/databases/smartfridge", null, 0);
        filterIngredient("All", sqLiteDatabase);

        gridView = (GridView) view.findViewById(R.id.ingredientGrid);
        gridView.setAdapter(new IngredientAdapter(name, image, qty, expDate, getActivity()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(nameExp, imageExp, getActivity());
        recyclerView.setAdapter(adapter);

        btnAll = (Button) view.findViewById(R.id.btnAll);
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterIngredient("All", sqLiteDatabase);
                gridView = (GridView) view.findViewById(R.id.ingredientGrid);
                gridView.setAdapter(new IngredientAdapter(name, image, qty, expDate, getActivity()));
            }
        });
        btnVegetable = (Button) view.findViewById(R.id.btnVegetable);
        btnVegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterIngredient(btnVegetable.getText().toString(), sqLiteDatabase);
                gridView = (GridView) view.findViewById(R.id.ingredientGrid);
                gridView.setAdapter(new IngredientAdapter(name, image, qty, expDate, getActivity()));
            }
        });
        btnMeat = (Button) view.findViewById(R.id.btnMeat);
        btnMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterIngredient(btnMeat.getText().toString(), sqLiteDatabase);
                gridView = (GridView) view.findViewById(R.id.ingredientGrid);
                gridView.setAdapter(new IngredientAdapter(name, image, qty, expDate, getActivity()));
            }
        });
        btnCondiment = (Button) view.findViewById(R.id.btnCondiment);
        btnCondiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterIngredient(btnCondiment.getText().toString(), sqLiteDatabase);
                gridView = (GridView) view.findViewById(R.id.ingredientGrid);
                gridView.setAdapter(new IngredientAdapter(name, image, qty, expDate, getActivity()));
            }
        });
        btnSnack = (Button) view.findViewById(R.id.btnSnack);
        btnSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterIngredient(btnSnack.getText().toString(), sqLiteDatabase);
                gridView = (GridView) view.findViewById(R.id.ingredientGrid);
                gridView.setAdapter(new IngredientAdapter(name, image, qty, expDate, getActivity()));
            }
        });
        btnFruit = (Button) view.findViewById(R.id.btnFruit);
        btnFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterIngredient(btnFruit.getText().toString(), sqLiteDatabase);
                gridView = (GridView) view.findViewById(R.id.ingredientGrid);
                gridView.setAdapter(new IngredientAdapter(name, image, qty, expDate, getActivity()));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}