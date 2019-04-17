package com.example.marijah.outflow.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.categoryPickedObject
import com.example.marijah.outflow.models.Category
import java.util.*

class CategoryAdapter// Pass in the contact array into the constructor
(// Easy access to the context object in the recyclerview
        private val context: Context, private var categoriesArray: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var imgViewPreviouslyClicked: ImageView? = null
    private var txtViewPreviouslyClicked: TextView? = null
    private var positionOfPreviouslyClicked = -1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtViewCategory: TextView = itemView.findViewById<View>(R.id.txtViewCategory) as TextView
        var imgViewCategory: ImageView = itemView.findViewById(R.id.imgViewCategory)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.item_category, parent, false)

        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {

        /** Postavljamo buttone recyclerView-a Category  */
        val categoryElement = categoriesArray[position]

        // Set item views based on your views and data model
        val txtViewCategory = holder.txtViewCategory
        txtViewCategory.text = categoryElement.categoryName
        val imgButton = holder.imgViewCategory


        val resID = context.resources.getIdentifier(categoryElement.categoryImageName, "drawable", context.packageName)
        imgButton.setImageDrawable(context.resources.getDrawable(resID))


        imgButton.setColorFilter(Color.parseColor("#ffffff"))
        txtViewCategory.setTextColor(Color.parseColor("#ffffff"))

        // zbog problema sa skrolovanjem postavljamo samo jedan item kao zut
        if (imgViewPreviouslyClicked != null && imgButton === imgViewPreviouslyClicked && positionOfPreviouslyClicked == position) {
            imgViewPreviouslyClicked!!.setColorFilter(Color.parseColor("#ffff00"))
            txtViewPreviouslyClicked!!.setTextColor(Color.parseColor("#00ffff"))
        } else {
            imgButton.setColorFilter(Color.parseColor("#ffffff"))
            txtViewCategory.setTextColor(Color.parseColor("#ffffff"))
        }


        imgButton.setOnClickListener {
            if (imgViewPreviouslyClicked == null && positionOfPreviouslyClicked == -1) {
                imgViewPreviouslyClicked = imgButton
                txtViewPreviouslyClicked = txtViewCategory
                imgViewPreviouslyClicked!!.setColorFilter(Color.parseColor("#ffff00"))
                txtViewPreviouslyClicked!!.setTextColor(Color.parseColor("#00ffff"))
                positionOfPreviouslyClicked = position
            } else {
                imgViewPreviouslyClicked!!.setColorFilter(Color.parseColor("#ffffff"))
                txtViewPreviouslyClicked!!.setTextColor(Color.parseColor("#ffffff"))
                imgViewPreviouslyClicked = imgButton
                txtViewPreviouslyClicked = txtViewCategory
                imgViewPreviouslyClicked!!.setColorFilter(Color.parseColor("#ffff00"))
                txtViewPreviouslyClicked!!.setTextColor(Color.parseColor("#00ffff"))
                positionOfPreviouslyClicked = position
            }

            val choosenCategory = categoryElement.categoryName
            categoryPickedObject.categoryPicked = choosenCategory
            Toast.makeText(context, "You have picked $choosenCategory", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {


        return categoriesArray.size
    }


}
