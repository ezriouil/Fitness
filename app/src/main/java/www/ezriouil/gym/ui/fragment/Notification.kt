package www.ezriouil.gym.ui.fragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import www.ezriouil.gym.R
import www.ezriouil.gym.databinding.NotificationBinding
import www.ezriouil.gym.local.model.Client
import www.ezriouil.gym.local.model.sale_notification
import www.ezriouil.gym.local.sql.DataBase
import www.ezriouil.gym.recyclerView.MyArrayAdapterForListView

class Notification : Fragment() {

    private lateinit var binding: NotificationBinding
    private lateinit var myNotification: Notification.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NotificationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myAdapter = MyArrayAdapterForListView(requireContext(), sale_notification)
        binding.myListView.adapter = myAdapter
        for (item in sale_notification) {
            notification(item)
        }
    }

    private fun notification(item: Client) {
        val CHANNEL_NAME = "my channel name"
        val CHANNEL_ID = "my channel id"
        myNotification = Notification.Builder(context, CHANNEL_ID)
        myNotification.apply {
            setSmallIcon(R.drawable.person)
            setContentTitle(item.fullName)
            setContentText("client must be paid")
            setColor(Color.RED)
            setSubText("paid")
        }.build()
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationService =
            requireContext().getSystemService(FragmentActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationService.createNotificationChannel(notificationChannel)
        notificationService.notify(item.time.toInt(), myNotification.build())
    }
}