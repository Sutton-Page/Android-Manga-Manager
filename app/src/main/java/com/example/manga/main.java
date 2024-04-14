package com.example.manga;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import com.bumptech.glide.Glide;
import com.example.manga.databinding.FragmentMainBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class main extends Fragment {

    private NavController nav;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentMainBinding binding;
    DBHelper helper;
    private SQLiteDatabase db;

    private CustomAdapter adapter;

    private GridLayoutManager grid;

    private ArrayList<MangaItem> comicData = new ArrayList<>();

    public main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment main.
     */
    // TODO: Rename and change types and number of parameters
    public static main newInstance(String param1, String param2) {
        main fragment = new main();
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

        this.helper = new DBHelper(getContext());
        this.db = helper.getReadableDatabase();




    }


    public void dbTest(CustomAdapter adapter){

        String[] empty = new String[0];
        Cursor cursor = db.rawQuery("select * from manga",empty);

        String format = "id: %d title: %s url: %s";

        //StringBuilder result = new StringBuilder();


        ArrayList<MangaItem> temp = new ArrayList<>();


        if(cursor.moveToFirst()){

            temp.add(new MangaItem(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));

            while(cursor.moveToNext()){

                temp.add(new MangaItem(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
            }
        }

        cursor.close();

        this.comicData.clear();
        this.comicData.addAll(temp);

        adapter.notifyDataSetChanged();





    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view,savedInstanceState);

        binding = FragmentMainBinding.bind(view);
        this.nav = Navigation.findNavController(view);

        this.adapter = new CustomAdapter(getContext(),this.nav,this.comicData);

        binding.floatAdd.setOnClickListener(v -> {


            this.nav.navigate(R.id.add_manga3);
        });


        /*
        ArrayList<String> testData = new ArrayList<>();


        testData.add("https://mangadex.org/covers/077a3fed-1634-424f-be7a-9a96b7f07b78/f3fbdcfe-83a8-4852-a676-ccabd299a013.jpg.512.jpg");
        testData.add("https://mangadex.org/covers/077a3fed-1634-424f-be7a-9a96b7f07b78/f3fbdcfe-83a8-4852-a676-ccabd299a013.jpg.512.jpg");
        testData.add("https://mangadex.org/covers/077a3fed-1634-424f-be7a-9a96b7f07b78/f3fbdcfe-83a8-4852-a676-ccabd299a013.jpg.512.jpg");
        testData.add("https://mangadex.org/covers/077a3fed-1634-424f-be7a-9a96b7f07b78/f3fbdcfe-83a8-4852-a676-ccabd299a013.jpg.512.jpg");

        testData.add("https://mangadex.org/covers/37f5cce0-8070-4ada-96e5-fa24b1bd4ff9/6bfc8f2a-7510-4746-90d6-b97d01c20796.jpg.512.jpg");
        testData.add("https://mangadex.org/covers/37f5cce0-8070-4ada-96e5-fa24b1bd4ff9/6bfc8f2a-7510-4746-90d6-b97d01c20796.jpg.512.jpg");
        testData.add("https://mangadex.org/covers/37f5cce0-8070-4ada-96e5-fa24b1bd4ff9/6bfc8f2a-7510-4746-90d6-b97d01c20796.jpg.512.jpg");
        testData.add("https://mangadex.org/covers/37f5cce0-8070-4ada-96e5-fa24b1bd4ff9/6bfc8f2a-7510-4746-90d6-b97d01c20796.jpg.512.jpg");

        testData.add("https://mangadex.org/covers/077a3fed-1634-424f-be7a-9a96b7f07b78/f3fbdcfe-83a8-4852-a676-ccabd299a013.jpg.512.jpg");
        testData.add("https://mangadex.org/covers/077a3fed-1634-424f-be7a-9a96b7f07b78/f3fbdcfe-83a8-4852-a676-ccabd299a013.jpg.512.jpg");
        testData.add("https://mangadex.org/covers/077a3fed-1634-424f-be7a-9a96b7f07b78/f3fbdcfe-83a8-4852-a676-ccabd299a013.jpg.512.jpg");
        testData.add("https://mangadex.org/covers/077a3fed-1634-424f-be7a-9a96b7f07b78/f3fbdcfe-83a8-4852-a676-ccabd299a013.jpg.512.jpg");*/





        //CustomAdapter adapter = new CustomAdapter(getContext(),this.nav,this.comicData);

        this.dbTest(adapter);

        RecyclerView re = binding.mangaItems;

        GridLayoutManager grid = new GridLayoutManager(getActivity(),2);

        re.setLayoutManager(grid);

        re.setAdapter(adapter);


        binding.initalAdd.setOnClickListener(f ->{

                this.nav.navigate(R.id.add_manga3);
        });



        if(!this.comicData.isEmpty()){

            binding.emptyMessage.setVisibility(View.GONE);
            binding.initalAdd.setVisibility(View.GONE);

        }




    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);




    }
}