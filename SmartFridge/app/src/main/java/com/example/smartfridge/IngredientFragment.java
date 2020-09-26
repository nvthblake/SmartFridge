package com.example.smartfridge;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientFragment extends Fragment {
    GridView gridView;
    String[] name = new String[]{"Steak", "Carrot", "Onion", "Mushroom", "Chicken", "Shit", "Rice","Carrot","Carrot","Carrot","Carrot","Carrot","Carrot","Carrot","Carrot"};
    int[] image = new int[]{R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50,
            R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50,
            R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50, R.drawable.ic_baseline_fastfood_50,};
    int[] expDate = new int[]{5, 8, 3, 4, 10, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1,};
    int[] qty = new int[]{1, 2, 3, 10, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,};

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

        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);

        gridView = (GridView) view.findViewById(R.id.ingredientGrid);
        gridView.setAdapter(new IngredientAdapter(name, image, qty, expDate, getActivity()));

        // Inflate the layout for this fragment
        return view;
    }
}