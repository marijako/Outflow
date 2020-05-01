package com.example.marijah.outflow.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marijah.outflow.R
import com.example.marijah.outflow.models.AppManager
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutAndListeners()
    }

    // setting up the listeners on our activity
    private fun setLayoutAndListeners() {

        imgViewTrackYourExpensesButton.setOnClickListener {
            AppManager.getInstance(context).hasUserPickedSingleMode = true
            findNavController().navigate(R.id.action_homeFragment_to_newEntrySingleFragment)
        }


        imgViewTrackGroupExpensesButton.setOnClickListener {
            AppManager.getInstance(context).hasUserPickedSingleMode = false
            findNavController().navigate(R.id.action_homeFragment_to_newEntrySingleFragment)

        }

    }

}
