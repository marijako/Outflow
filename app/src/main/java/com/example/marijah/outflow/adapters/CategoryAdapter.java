package com.example.marijah.outflow.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marijah.outflow.R;
import com.example.marijah.outflow.helpers.HelperManager;
import com.example.marijah.outflow.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {


    ArrayList<Category> categoriesArray;
    private Context mContext;
    private ImageView imgViewPreviouslyClicked;
    private TextView txtViewPreviouslyClicked;
    private int positionOfPreviouslyClicked = -1;

    // Pass in the contact array into the constructor
    public CategoryAdapter(Context context, ArrayList<Category> categoriesTemp) {

        categoriesArray = categoriesTemp;
        mContext = context;

    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewCategory;
        public ImageView imgViewCategory;


        public ViewHolder(View itemView) {

            super(itemView);
            txtViewCategory = (TextView) itemView.findViewById(R.id.txtViewCategory);
            HelperManager.setTypefaceLight(getContext().getAssets(), txtViewCategory);
            imgViewCategory = itemView.findViewById(R.id.imgViewCategory);

        }
    }


    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_category, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, final int position) {

        /** Postavljamo buttone recyclerView-a Category */
        final Category categoryElement = categoriesArray.get(position);

        // Set item views based on your views and data model
        final TextView txtViewCategory = holder.txtViewCategory;
        txtViewCategory.setText(categoryElement.getCategoryName());
        final ImageView imgButton = holder.imgViewCategory;


        int resID = getContext().getResources().getIdentifier(categoryElement.getCategoryImageName(), "drawable", getContext().getPackageName());
        imgButton.setImageDrawable(getContext().getResources().getDrawable(resID));


         imgButton.setColorFilter(Color.parseColor("#ffffff"));
         txtViewCategory.setTextColor(Color.parseColor("#ffffff"));

        // zbog problema sa skrolovanjem postavljamo samo jedan item kao zut
        if (imgViewPreviouslyClicked != null && imgButton == imgViewPreviouslyClicked && positionOfPreviouslyClicked == position) {
            imgViewPreviouslyClicked.setColorFilter(Color.parseColor("#ffff00"));
            txtViewPreviouslyClicked.setTextColor(Color.parseColor("#00ffff"));
        }
        else {
            imgButton.setColorFilter(Color.parseColor("#ffffff"));
            txtViewCategory.setTextColor(Color.parseColor("#ffffff"));
        }


        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imgViewPreviouslyClicked == null && positionOfPreviouslyClicked == -1) {
                    imgViewPreviouslyClicked = imgButton;
                    txtViewPreviouslyClicked = txtViewCategory;
                    imgViewPreviouslyClicked.setColorFilter(Color.parseColor("#ffff00"));
                    txtViewPreviouslyClicked.setTextColor(Color.parseColor("#00ffff"));
                    positionOfPreviouslyClicked = position;
                } else {
                    imgViewPreviouslyClicked.setColorFilter(Color.parseColor("#ffffff"));
                    txtViewPreviouslyClicked.setTextColor(Color.parseColor("#ffffff"));
                    imgViewPreviouslyClicked = imgButton;
                    txtViewPreviouslyClicked = txtViewCategory;
                    imgViewPreviouslyClicked.setColorFilter(Color.parseColor("#ffff00"));
                    txtViewPreviouslyClicked.setTextColor(Color.parseColor("#00ffff"));
                    positionOfPreviouslyClicked = position;
                }

                String choosenCategory = categoryElement.getCategoryName();
                Toast.makeText(mContext, "You have picked " + choosenCategory, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {


        return categoriesArray.size();
    }


}
