package com.example.manga;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    //private ArrayList<String> localDataSet;
    private Context context;
    private NavController nav;

    private ArrayList<MangaItem> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View


            image = view.findViewById(R.id.cover);


        }


        public ImageView getImageView(){

            return image;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public CustomAdapter(Context context, NavController nav, ArrayList<MangaItem> dataSet) {
        this.context = context;
        localDataSet = dataSet;
        this.nav = nav;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comic_item_large, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //viewHolder.getTextView().setText(localDataSet[position]);

        String url  = localDataSet.get(position).imageUrl;

        Glide.with(context).load(url).into(viewHolder.getImageView());

        viewHolder.getImageView().setOnClickListener(l -> {

            int index = localDataSet.get(position).id;

            Bundle bundle = new Bundle();

            bundle.putInt("comicId",index);this.nav.navigate(R.id.comic_Item,bundle);

        });



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

