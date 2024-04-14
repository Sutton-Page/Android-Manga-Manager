package com.example.manga;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CoverPickerDialogue extends DialogFragment {

    private ArrayList<String> imageUrls;

    // Define a listener interface to handle image selection events
    public interface OnImageSelectedListener {
        void onImageSelected(String imageUrl);
    }

    private OnImageSelectedListener listener;



    // Constructor to pass image URLs
    public CoverPickerDialogue(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    // Set the listener
    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);




        // Set the dialog window attributes to position it at the bottom
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Clear default background
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.menu_rounded);
        dialog.getWindow().setElevation(getResources().getDimension(R.dimen.dialog_elevation));




        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image_picker, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.coverChoose);
        //Button selectButton = view.findViewById(R.id.btnSelect);
        //
        // Button cancelButton = view.findViewById(R.id.btnCancel);

        // Set up RecyclerView and adapter
        ImageAdapter adapter = new ImageAdapter(getContext(), imageUrls,this.listener);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        recyclerView.setAdapter(adapter);

        /*

        // Set click listener for select button
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected image URL from RecyclerView adapter
                if (listener != null) {
                    // Call the listener method to handle image selection
                    listener.onImageSelected(adapter.getSelectedImageUrl());
                }
                // Dismiss the dialog
                dismiss();
            }
        });

        // Set click listener for cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog
                dismiss();
            }
        });*/

        return view;
    }
}
