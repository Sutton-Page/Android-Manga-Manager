package com.example.manga;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.manga.databinding.FragmentAddMangaBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link add_manga#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add_manga extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DBHelper helper;
    private SQLiteDatabase db;

    private Handler handle;

    FragmentAddMangaBinding binding;
    NavController nav;


    public add_manga() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment add_manga.
     */
    // TODO: Rename and change types and number of parameters
    public static add_manga newInstance(String param1, String param2) {
        add_manga fragment = new add_manga();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }*/


    public void addManga(){

        ContentValues row = new ContentValues();
        row.put("title",this.binding.titleInput.getText().toString());
        row.put("url",this.binding.urlInput.getText().toString());

        this.db.insert("manga",null,row);

        //Toast.makeText(getContext(),"Added Manga",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onViewCreated(View view ,Bundle savedInstanceState ){

        super.onViewCreated(view,savedInstanceState);



        binding = FragmentAddMangaBinding.bind(view);
        this.helper = new DBHelper(getContext());

        this.handle = new Handler(Looper.getMainLooper());

        this.db = helper.getWritableDatabase();
        this.nav = Navigation.findNavController(view);


        binding.submit.setOnClickListener(l -> {

                this.addManga();
                Snackbar.make(getContext(),view,"Added Manga",Snackbar.LENGTH_LONG).show();
        });

        binding.back.setOnClickListener(l -> {

            this.nav.popBackStack();

        });


        binding.searchSubmit.setOnClickListener(l ->{

            String query = binding.titleInput.getText().toString();

            Thread thread = new Thread( () -> {

                try{

                    MangaRequest req = new MangaRequest(query);
                    String mangaUrl = req.getMangaUrl();


                    handle.post( () -> {

                        binding.urlInput.setText(mangaUrl);
                        Glide.with(getContext()).load(mangaUrl).into(binding.displayCover);
                    });

                } catch (Exception e) {

                    Log.d("URL", e.getMessage());
                    throw new RuntimeException(e);
                }

            });

            thread.start();
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_manga, container, false);
    }
}
