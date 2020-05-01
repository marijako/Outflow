package com.example.marijah.outflow.fragments

import android.app.Activity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marijah.outflow.R
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.popups.CreateAGroupPopup
import com.example.marijah.outflow.popups.JoinAGroupPopup
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    var i = 0

    var isUserInSingleMode = AppManager.getInstance(context).hasUserPickedSingleMode

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutAndListeners()
    }

    private fun setLayoutAndListeners() {


        context?.let {context ->

            if (isUserInSingleMode) {
                holderGroupMode.visibility = View.GONE
                guidelineChangeModeTop.setGuidelinePercent(0.2f)
            }


            txtViewReceiveDailyNotifications.setOnClickListener {
                i++
                if (i % 2 == 0) {
                    imgViewReceiveNotificationsSwitch.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.switch_on))
                } else
                    imgViewReceiveNotificationsSwitch.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.switch_off))
            }


            txtViewCheckYourGroups.setOnClickListener {
                //val intent = Intent(this, ListOfGroupsActivity::class.java)
                //startActivity(intent)
            }



            txtViewCreateAGroup.setOnClickListener {

                val createAGroupPopup = CreateAGroupPopup(context as Activity, R.layout.popup_create_a_group)
                createAGroupPopup.show()

            }


            txtViewJoinAGroup.setOnClickListener {

                val joinAGroupPopup = JoinAGroupPopup(context as Activity, R.layout.popup_join_a_group)
                joinAGroupPopup.show()

            }


            txtViewSignOut.setOnClickListener {
                if(!isUserInSingleMode)
                    AuthUI.getInstance().signOut(context)
                findNavController().popBackStack(R.id.homeFragment, false)

            }

            txtViewChangeMode.setOnClickListener {

                if (!isUserInSingleMode) {
                    AuthUI.getInstance().signOut(context)
                }
                findNavController().popBackStack(R.id.homeFragment, false)

            }
        }
    }

}
