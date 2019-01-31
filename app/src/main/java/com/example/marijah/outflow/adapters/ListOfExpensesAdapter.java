package com.example.marijah.outflow.adapters;

import android.content.Context;
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
import com.example.marijah.outflow.helpers.ListOfExpensesOpenHelper;
import com.example.marijah.outflow.models.Category;
import com.example.marijah.outflow.models.ListOfExpensesItem;

import java.util.ArrayList;

public class ListOfExpensesAdapter extends RecyclerView.Adapter<ListOfExpensesAdapter.ViewHolder> {


    private Context mContext;
    ListOfExpensesOpenHelper mDB;


    // Pass in the contact array into the constructor
    public ListOfExpensesAdapter(Context context, ListOfExpensesOpenHelper db) {

        mContext = context;
        mDB = db;

    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewCostList;
        public TextView txtViewCategoryList;
        public TextView txtViewCommentList;
        public ImageView imgViewDeleteItem;

        public ViewHolder(View itemView) {

            super(itemView);
            txtViewCostList = itemView.findViewById(R.id.txtViewCostList);
            HelperManager.setTypefaceLight(getContext().getAssets(), txtViewCostList);

            txtViewCategoryList = itemView.findViewById(R.id.txtViewCategoryList);
            HelperManager.setTypefaceLight(getContext().getAssets(), txtViewCategoryList);

            txtViewCommentList = itemView.findViewById(R.id.txtViewCommentList);
            HelperManager.setTypefaceLight(getContext().getAssets(), txtViewCommentList);

            imgViewDeleteItem = itemView.findViewById(R.id.imgViewDeleteItem);
        }
    }


    @Override
    public ListOfExpensesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_list_of_expenses, parent, false);

        // Return a new holder instance
        ListOfExpensesAdapter.ViewHolder viewHolder = new ListOfExpensesAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListOfExpensesAdapter.ViewHolder holder, final int position) {

        final ListOfExpensesItem current = mDB.query(position);
        holder.txtViewCostList.setText(current.getmCost());


        final ImageView imgViewDeleteItemTemp = holder.imgViewDeleteItem;


        imgViewDeleteItemTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // String choosenCategory = categoryElement.getCategoryName();
                Toast.makeText(mContext, "You have picked " + current.getmCost(), Toast.LENGTH_SHORT).show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

}
