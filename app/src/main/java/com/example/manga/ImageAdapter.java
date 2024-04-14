package com.example.manga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<String> imageUrls;
    private int selectedItemPosition = RecyclerView.NO_POSITION;

    private CoverPickerDialogue.OnImageSelectedListener listener;

    public ImageAdapter(Context context, List<String> imageUrls,CoverPickerDialogue.OnImageSelectedListener listener) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comic_item_large, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // Load image using Glide or Picasso
        Glide.with(context)
                .load(imageUrls.get(position))
                .into(holder.imageView);

        // Set selected item background
        if (selectedItemPosition == position) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }

        // Handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onImageSelected(imageUrls.get(holder.getAdapterPosition()));
                }

                /*
                // Update selected item position
                selectedItemPosition = holder.getAdapterPosition();
                // Notify adapter about the item selection change
                notifyDataSetChanged();*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public String getSelectedImageUrl() {
        if (selectedItemPosition != RecyclerView.NO_POSITION) {
            return imageUrls.get(selectedItemPosition);
        }
        return null;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cover);
        }
    }
}
