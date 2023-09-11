package www.ezriouil.gym.recyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import www.ezriouil.gym.*
import www.ezriouil.gym.local.model.Client
import www.ezriouil.gym.local.model.sale_notification
import java.util.concurrent.TimeUnit

class Adapter(
    val context: Context,
    private var myListOfClient: List<Client>,
    private val myListener: Listener
) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount() = myListOfClient.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(inflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myListOfClient[holder.adapterPosition]
        holder.cardViewBtn.setOnClickListener {
            myListener.cardOfClient(
                item,
                holder.adapterPosition
            )
        }
        holder.cardViewFullName.text = "- User : " + item.fullName.uppercase()
        holder.cardViewPrice.text = "- Price : " + item.price.toString() + " DH"
        holder.cardViewGender.text = "- Gender : " + item.gender
        val timeLeft = item.time - System.currentTimeMillis()
        val days = TimeUnit.MILLISECONDS.toDays(timeLeft)
        val hours = TimeUnit.MILLISECONDS.toHours(timeLeft) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60
        if (timeLeft <= 0) {
            //if (!sale_notification.contains(item)) {
              //  sale_notification.add(item)
            //}
            holder.cardViewTime.text = "- Timer : DONE!"
        } else {
            holder.cardViewTime.text = "- Timer : " + String.format(
                "%d DAYS %d HOURS %d MINUTES",
                days,
                hours,
                minutes
            )
        }
    }

    fun updateMyData(myNewListClient: List<Client>) {
        val myDiffUtil = DiffUtil.calculateDiff(MyDiffUtil(myListOfClient, myNewListClient))
        myListOfClient = myNewListClient
        myDiffUtil.dispatchUpdatesTo(this)
    }

    fun updateData(myNewListClient: List<Client>) {
        myListOfClient = myNewListClient
        this.notifyDataSetChanged()
    }

}