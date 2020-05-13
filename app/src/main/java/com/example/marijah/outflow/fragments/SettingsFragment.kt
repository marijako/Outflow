package com.example.marijah.outflow.fragments

import android.app.Activity
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marijah.outflow.R
import com.example.marijah.outflow.helpers.NotificationPublisher
import com.example.marijah.outflow.models.AppManager
import com.example.marijah.outflow.popups.CreateAGroupPopup
import com.example.marijah.outflow.popups.JoinAGroupPopup
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    var isUserInSingleMode = AppManager.getInstance(context).hasUserPickedSingleMode

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutAndListeners()
    }

    private fun setLayoutAndListeners() {

        context?.let { context ->

            if (AppManager.getInstance(context).notificationStatus) {
                imgViewReceiveNotificationsSwitch.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.switch_on))


            } else {
                imgViewReceiveNotificationsSwitch.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.switch_off))
            }


            if (isUserInSingleMode) {
                holderGroupMode.visibility = View.GONE
                guidelineChangeModeTop.setGuidelinePercent(0.2f)
            }


            txtViewReceiveDailyNotifications.setOnClickListener {
                if (AppManager.getInstance(context).notificationStatus) {
                    AppManager.getInstance(context).notificationStatus = false
                    imgViewReceiveNotificationsSwitch.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.switch_off))
                } else {
                    AppManager.getInstance(context).notificationStatus = true
                    imgViewReceiveNotificationsSwitch.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.switch_on))
                }
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
                if (!isUserInSingleMode)
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


    private fun scheduleNotification(notification: Notification, delay: Int) {
        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
    }

    private fun getNotification(content: String): Notification? {
        val builder: Notification.Builder = Notification.Builder(context)
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_launcher)
        return builder.build()
    }


}
