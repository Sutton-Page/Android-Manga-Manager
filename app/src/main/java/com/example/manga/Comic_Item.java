package com.example.manga;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.manga.databinding.FragmentAddMangaBinding;
import com.example.manga.databinding.FragmentComicItemBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Comic_Item#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Comic_Item extends Fragment implements  CoverPickerDialogue.OnImageSelectedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int idArg = -1;

    DBHelper helper;
    private SQLiteDatabase db;

    private Handler handle;

    FragmentComicItemBinding binding;
    NavController nav;

    public Comic_Item() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Comic_Item.
     */
    // TODO: Rename and change types and number of parameters
    public static Comic_Item newInstance(String param1, String param2) {
        Comic_Item fragment = new Comic_Item();
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

    public MangaItem getComic(int id){

        this.db = this.helper.getReadableDatabase();

        String [] temp = {String.valueOf(id)};
        Cursor cursor = db.rawQuery("select * from manga where id = ?",temp);

        MangaItem item = new MangaItem();

        if(cursor.moveToFirst()){

            item = new MangaItem(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
        }

        return item;

    }

    public void deleteComic(int id){

        this.db = this.helper.getWritableDatabase();
        String[] values = {String.valueOf(id)};

        this.db.delete("manga","id = ?", values);




    }




    private void openImagePickerDialog(ArrayList<String> data) {

        /*
        ArrayList<String> test = new ArrayList<>();

        test.add("https://mangadex.org/covers/37f5cce0-8070-4ada-96e5-fa24b1bd4ff9/6bfc8f2a-7510-4746-90d6-b97d01c20796.jpg.512.jpg");
        test.add("https://mangadex.org/covers/37f5cce0-8070-4ada-96e5-fa24b1bd4ff9/6bfc8f2a-7510-4746-90d6-b97d01c20796.jpg.512.jpg");

        test.add("https://mangadex.org/covers/37f5cce0-8070-4ada-96e5-fa24b1bd4ff9/6bfc8f2a-7510-4746-90d6-b97d01c20796.jpg.512.jpg");
        test.add("https://mangadex.org/covers/37f5cce0-8070-4ada-96e5-fa24b1bd4ff9/6bfc8f2a-7510-4746-90d6-b97d01c20796.jpg.512.jpg");

        */



        CoverPickerDialogue dialogFragment = new CoverPickerDialogue(data);

        dialogFragment.setOnImageSelectedListener(this);

        // Show the dialog fragment
        dialogFragment.show(getChildFragmentManager(), "ImagePickerDialog");
    }


    private void copyTitletoClip(MangaItem item){

        ClipboardManager clipBoard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clipData = ClipData.newPlainText("Manga Title",item.title);

        clipBoard.setPrimaryClip(clipData);
    }

    @Override
    public void onViewCreated(View view ,Bundle savedInstanceState ){

        super.onViewCreated(view,savedInstanceState);

        binding = FragmentComicItemBinding.bind(view);
        this.helper = new DBHelper(getContext());

        this.db = helper.getWritableDatabase();
        this.nav = Navigation.findNavController(view);

        this.handle = new Handler(Looper.getMainLooper());

        if(getArguments() != null){

            idArg = getArguments().getInt("comicId");
        }



        if(idArg != -1){

            String value = "The comicId is " + idArg;
           // binding.argTest.setText(value);


            MangaItem item = getComic(idArg);

            this.copyTitletoClip(item);

            binding.mangaTitle.setText(item.title);

            Glide.with(getContext()).load(item.imageUrl).into(binding.mangaCover);



            binding.mangaCover.setOnClickListener( l ->{

                    Thread thread = new Thread(() -> {

                        try{

                            MangaRequest req = new MangaRequest(item.title);
                            ArrayList<String> mangaUrls = req.getMangaUrl();

                            handle.post( () -> {

                                this.openImagePickerDialog(mangaUrls);
                            });

                        } catch(Exception e){

                            Log.d("URL", e.getMessage());
                            throw new RuntimeException(e);
                        }

                    });

                    thread.start();
            });

            binding.deleteManga.setOnClickListener(l ->{

                this.deleteComic(idArg);

                Snackbar.make(view,"Deleted Manga",Snackbar.LENGTH_LONG).show();

                this.nav.popBackStack();

            });









        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comic__item, container, false);
    }

    @Override
    public void onImageSelected(String imageUrl) {
        // Handle the selected image URL here in the parent fragment
        // For example, load the image into an ImageView
        // Glide.with(requireContext()).load(imageUrl).into(imageView);

        Glide.with(requireContext()).load(imageUrl).into(binding.mangaCover);

        // Close the dialog
        Fragment dialogFragment = getChildFragmentManager().findFragmentByTag("ImagePickerDialog");
        if (dialogFragment != null) {
            DialogFragment df = (DialogFragment) dialogFragment;
            df.dismiss();
        }
    }
}